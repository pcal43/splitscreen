/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023 pcal.net
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.pcal.splitscreen.mod.fabric.mixins;

import net.pcal.splitscreen.WindowMode.MinecraftWindowContext;
import net.pcal.splitscreen.WindowMode.Rectangle;
import net.pcal.splitscreen.WindowMode.WindowDescription;
import net.pcal.splitscreen.WindowMode.WindowStyle;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

import static net.pcal.splitscreen.Mod.mod;
import static net.pcal.splitscreen.logging.SystemLogger.syslog;
import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;

import com.mojang.blaze3d.platform.DisplayData;
import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.ScreenManager;
import com.mojang.blaze3d.platform.VideoMode;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.platform.WindowEventHandler;

/**
 * @author pcal
 * @since 0.0.1
 */
@Mixin(Window.class)
public abstract class WindowMixin {

    @Final
    @Shadow
    private long window;
    @Shadow
    private int windowedX;
    @Shadow
    private int windowedY;
    @Shadow
    private int windowedWidth;
    @Shadow
    private int windowedHeight;
    @Shadow
    private int x;
    @Shadow
    private int y;
    @Shadow
    private int width;
    @Shadow
    private int height;
    @Shadow
    private boolean fullscreen;
    @Shadow
    private Optional<VideoMode> preferredFullscreenVideoMode;


    @Shadow
    public abstract boolean isFullscreen();

    @Shadow
    protected abstract void setMode();

    @Shadow
    @Nullable
    public abstract Monitor findBestMonitor();

    // ======================================================================
    // Mixins

    //@Shadow
    public abstract void updateDisplay();

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void Window(WindowEventHandler eventHandler, ScreenManager monitorTracker, DisplayData settings, String videoMode, String title, CallbackInfo ci) {
        // ok so the issue seems to be that this triggers a framebuffersizechanged when it normally wouldn't
        // minecraftclient is listening for it on resolutionChanged and it isn't ready.  so we probably just need to find
        // a later time to move the window.
        //splitscreen_repositionWindow(mod().onWindowCreate(splitscreen_getWindowContext()));
    }

    @Inject(method = "toggleFullScreen()V", at = @At("HEAD"), cancellable = true)
    public void splitscreen_toggleFullScreen(CallbackInfo ci) {
        final MinecraftWindowContext res = splitscreen_getWindowContext();
        if (res != null) {
            splitscreen_repositionWindow(mod().onToggleFullscreen(res));
            setMode();
        }
        ci.cancel();
    }

    @Inject(method = "onFramebufferResize(JII)V", at = @At("HEAD"))
    private void onFramebufferSizeChanged(long window, int width, int height, CallbackInfo ci) {
        if (window == this.window) {
            final WindowDescription wd = mod().onResolutionChange(splitscreen_getWindowContext());
            if (wd.style() == WindowStyle.SPLITSCREEN) {
                splitscreen_repositionWindow(wd);
            }
            // if it's not a splitscreen mode, lets just let minecraft handle it
        }
    }

    @Inject(method = "setMode()V", at = @At("HEAD"))
    private void splitscreen_updateWindowRegion(CallbackInfo ci) {
        final WindowDescription wd = mod().onWindowCreate(splitscreen_getWindowContext());
        splitscreen_repositionWindow(wd);
    }

    // ======================================================================
    // Private

    @Unique
    private MinecraftWindowContext splitscreen_getWindowContext() {
        final Monitor monitor = this.findBestMonitor();
        if (monitor == null) {
            syslog().warn("could not determine monitor");
            return null;
        }
        final VideoMode videoMode = findBestMonitor().getPreferredVidMode(this.preferredFullscreenVideoMode);
        final Rectangle currentWindow = new Rectangle(x, y, width, height);
        return new MinecraftWindowContext(videoMode.getWidth(), videoMode.getHeight(), currentWindow);
    }

    @Unique
    private void splitscreen_repositionWindow(WindowDescription wd) {
        switch (wd.style()) {
            case FULLSCREEN:
                this.fullscreen = true;
                break;
            case WINDOWED:
            case SPLITSCREEN:
                this.fullscreen = false;
                this.windowedX = wd.x();
                this.windowedY = wd.y();
                this.windowedWidth = wd.width();
                this.windowedHeight = wd.height();
                this.x = this.windowedX;
                this.y = this.windowedY;
                this.width = this.windowedWidth;
                this.height = this.windowedHeight;
                GLFW.glfwSetWindowMonitor(this.window, 0L, this.x, this.y, this.width, this.height, -1);
                GLFW.glfwSetWindowAttrib(this.window, GLFW_DECORATED, wd.style() == WindowStyle.WINDOWED ? GLFW_TRUE : GLFW_FALSE);
        }
    }
}
