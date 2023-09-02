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

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.WindowSettings;
import net.minecraft.client.util.Monitor;
import net.minecraft.client.util.MonitorTracker;
import net.minecraft.client.util.VideoMode;
import net.minecraft.client.util.Window;
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

/**
 * @author pcal
 * @since 0.0.1
 */
@Mixin(Window.class)
public abstract class WindowMixin {

    @Final @Shadow private long handle;
    @Shadow private int windowedX;
    @Shadow private int windowedY;
    @Shadow private int windowedWidth;
    @Shadow private int windowedHeight;
    @Shadow private int x;
    @Shadow private int y;
    @Shadow private int width;
    @Shadow private int height;
    @Shadow private boolean fullscreen;
    @Shadow private Optional<VideoMode> videoMode;


    @Shadow public abstract boolean isFullscreen();
    @Shadow protected abstract void updateWindowRegion();

    @Shadow @Nullable public abstract Monitor getMonitor();

    // ======================================================================
    // Mixins

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void Window(WindowEventHandler eventHandler, MonitorTracker monitorTracker, WindowSettings settings, String videoMode, String title, CallbackInfo ci) {
        splitscreen_repositionWindow(mod().onWindowCreate(splitscreen_getWindowContext()));
    }

    @Inject(method = "toggleFullscreen()V", at = @At("HEAD"), cancellable = true)
    public void splitscreen_toggleFullScreen(CallbackInfo ci) {
        final MinecraftWindowContext res = splitscreen_getWindowContext();
        if (res != null) {
            splitscreen_repositionWindow(mod().onToggleFullscreen(res));
        }
        ci.cancel();
    }


    @Inject(method = "onFramebufferSizeChanged(JII)V", at = @At("HEAD"))
    private void onFramebufferSizeChanged(long window, int width, int height, CallbackInfo ci) {
        if (window == this.handle) {
            final WindowDescription wd = mod().onResolutionChange(splitscreen_getWindowContext());
            if (wd.style() == WindowStyle.SPLITSCREEN) {
                splitscreen_repositionWindow(wd);
            }
            // if it's not a splitscreen mode, lets just let minecraft handle it
        }
    }

    // ======================================================================
    // Private

    @Unique
    private MinecraftWindowContext splitscreen_getWindowContext() {
        final Monitor monitor = this.getMonitor();
        if (monitor == null) {
            syslog().warn("could not determine monitor");
            return null;
        }
        final VideoMode videoMode = getMonitor().findClosestVideoMode(this.videoMode);
        final Rectangle currentWindow = new Rectangle(x, y, width, height);
        return new MinecraftWindowContext(videoMode.getWidth(), videoMode.getHeight(), currentWindow);
    }

    @Unique
    private void splitscreen_repositionWindow(WindowDescription wd) {
        switch(wd.style()) {
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
                GLFW.glfwSetWindowAttrib(this.handle, GLFW_DECORATED, wd.style() == WindowStyle.WINDOWED ? GLFW_TRUE : GLFW_FALSE);
        }
        if (MinecraftClient.getInstance().getWindow() != null) { // true if the game is starting up, will NPE if so
            updateWindowRegion();
        }
    }
}
