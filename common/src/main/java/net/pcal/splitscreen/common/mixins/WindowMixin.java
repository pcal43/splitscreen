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

package net.pcal.splitscreen.common.mixins;

import com.mojang.blaze3d.platform.DisplayData;
import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.ScreenManager;
import com.mojang.blaze3d.platform.VideoMode;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.platform.WindowEventHandler;
import net.pcal.splitscreen.common.MinecraftWindow;
import net.pcal.splitscreen.common.WindowStyle;
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

import static net.pcal.splitscreen.common.Mod.mod;
import static net.pcal.splitscreen.common.logging.SystemLogger.syslog;
import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;

/**
 * @author pcal
 * @since 0.0.1
 */
@Mixin(Window.class)
public abstract class WindowMixin implements MinecraftWindow {

    @Final
    @Shadow
    private long handle;
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

    @Inject(method = "<init>", at = @At(value = "TAIL"), remap = false)
    private void Window(WindowEventHandler eventHandler, ScreenManager monitorTracker, DisplayData settings, String videoMode, String title, CallbackInfo ci) {
        mod().onWindowCreate(this);
    }

    @Inject(method = "toggleFullScreen()V", at = @At("HEAD"), cancellable = true, remap = false)
    public void splitscreen_toggleFullScreen(CallbackInfo ci) {
        mod().onToggleFullscreen(this);
        ci.cancel();
    }

    @Inject(method = "onFramebufferResize(JII)V", at = @At("HEAD"), remap = false)
    private void splitscreen_onFramebufferSizeChanged(long handle, int width, int height, CallbackInfo ci) {
        if (handle == this.handle) mod().onResolutionChange(this);
    }

    @Inject(method = "setMode()V", at = @At("HEAD"), remap = false)
    private void splitscreen_setMode(CallbackInfo ci) {
        mod().onSetMode(this);
    }

    // ======================================================================
    // RepositionableWindow implementation

    @Override
    @Unique
    public Rectangle getWindowBounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    @Unique
    public Rectangle getScreenBounds() {
        final Monitor monitor = this.findBestMonitor();
        if (monitor == null) {
            syslog().warn("Could not determine Monitor");
            return null;
        } else {
            final VideoMode videoMode = monitor.getPreferredVidMode(this.preferredFullscreenVideoMode);
            if (videoMode == null) {
                syslog().warn("Could not determine VideoMode");
                return null;
            } else {
                return new Rectangle(0, 0, videoMode.getWidth(), videoMode.getHeight());
            }
        }
    }

    @Override
    @Unique
    public void reposition(WindowStyle style, Rectangle newBounds) {
        switch (style) {
            case FULLSCREEN:
                this.fullscreen = true;
                break;
            case WINDOWED:
            case SPLITSCREEN:
                this.fullscreen = false;
                this.windowedX = newBounds.x();
                this.windowedY = newBounds.y();
                this.windowedWidth = newBounds.width();
                this.windowedHeight = newBounds.height();
                this.x = this.windowedX;
                this.y = this.windowedY;
                this.width = this.windowedWidth;
                this.height = this.windowedHeight;
                GLFW.glfwSetWindowMonitor(this.handle, 0L, this.x, this.y, this.width, this.height, -1);
                GLFW.glfwSetWindowAttrib(this.handle, GLFW_DECORATED, style == WindowStyle.WINDOWED ? GLFW_TRUE : GLFW_FALSE);
        }
    }
}
