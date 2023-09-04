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

package net.pcal.splitscreen;

/**
 * A named, user-selectable rule for sizing and positioning the main minecraft window.  It can describe what the
 * window shape ought to be given a screen resolution.
 *
 * @author pcal
 * @since 0.0.1
 */
public interface WindowMode {

    String getName();

    /**
     * Describes to Minecraft what we want the window to look like in the given context.
     */
    WindowDescription getFor(MinecraftWindowContext resolution);

    enum WindowStyle {
        FULLSCREEN, WINDOWED, SPLITSCREEN
    }

    /**
     * Describes to Minecraft what we want the window to look like.
     */
    record WindowDescription(WindowStyle style, int x, int y, int width, int height) {
    }

    /**
     * Lets Minecraft describe to us the current window and its context.
     */
    record MinecraftWindowContext(int screenWidth, int screenHeight, Rectangle windowRect) {
    }


    record WindowContext(MinecraftWindowContext mcContext, Rectangle savedWindowSize) {
    }

    record Rectangle(int x, int y, int width, int height) {
    }

}
