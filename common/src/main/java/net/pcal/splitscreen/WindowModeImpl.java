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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static net.pcal.splitscreen.WindowMode.WindowStyle.FULLSCREEN;
import static net.pcal.splitscreen.WindowMode.WindowStyle.SPLITSCREEN;
import static net.pcal.splitscreen.WindowMode.WindowStyle.WINDOWED;

/**
 * @author pcal
 * @since 0.0.1
 */
record WindowModeImpl(
        String name,
        WindowStyle style,
        Function<MinecraftWindowContext, Integer> xFn,
        Function<MinecraftWindowContext, Integer> yFn,
        Function<MinecraftWindowContext, Integer> widthFn,
        Function<MinecraftWindowContext, Integer> heightFn) implements WindowMode {

    @Override
    public String getName() {
        return name;
    }

    @Override
    public WindowDescription getFor(MinecraftWindowContext res) {
        return new WindowDescription(style, xFn.apply(res), yFn.apply(res), widthFn.apply(res), heightFn.apply(res));
    }

    static List<WindowMode> getModes(int gap) {
        final List<WindowMode> modes = new ArrayList<>();
        addMode(modes, "WINDOWED", WINDOWED, r -> r.windowRect().x(), r -> r.windowRect().y(),
                r -> r.windowRect().width(), r -> r.windowRect().height());
        addMode(modes, "LEFT", SPLITSCREEN, r -> 0, r -> 0, r -> r.screenWidth() / 2 - gap, r -> r.screenHeight());
        addMode(modes, "RIGHT", SPLITSCREEN, r -> r.screenWidth() / 2 + gap, r -> 0, r -> r.screenWidth() / 2 - gap, r -> r.screenHeight());
        addMode(modes, "TOP", SPLITSCREEN, r -> 0, r -> 0, r -> r.screenWidth(), r -> r.screenHeight() / 2 - gap);
        addMode(modes, "BOTTOM", SPLITSCREEN, r -> 0, r -> r.screenHeight() / 2 + gap, r -> r.screenWidth(), r -> r.screenHeight() / 2 - gap);
        addMode(modes, "TOP_LEFT", SPLITSCREEN, r -> 0, r -> 0,
                r -> r.screenWidth() / 2 - gap, r -> r.screenHeight() / 2 - gap);
        addMode(modes, "TOP_RIGHT", SPLITSCREEN, r -> r.screenWidth() / 2 + gap, r -> 0,
                r -> r.screenWidth() / 2 - gap, r -> r.screenHeight() / 2 - gap);
        addMode(modes, "BOTTOM_LEFT", SPLITSCREEN, r -> 0, r -> r.screenHeight() / 2 + gap,
                r -> r.screenWidth() / 2 - gap, r -> r.screenHeight() / 2 - gap);
        addMode(modes, "BOTTOM_RIGHT", SPLITSCREEN, r -> r.screenWidth() / 2 + gap, r -> r.screenHeight() / 2 + gap,
                r -> r.screenWidth() / 2 - gap, r -> r.screenHeight() / 2 - gap);
        addMode(modes, "FULLSCREEN", FULLSCREEN, no(), no(), no(), no());
        return modes;
    }

    // ======================================================================
    // Private

    private static void addMode(List<WindowMode> modes,
                                String name,
                                WindowStyle style,
                                Function<MinecraftWindowContext, Integer> xFn,
                                Function<MinecraftWindowContext, Integer> yFn,
                                Function<MinecraftWindowContext, Integer> widthFn,
                                Function<MinecraftWindowContext, Integer> heightFn) {
        modes.add(new WindowModeImpl(name, style, xFn, yFn, widthFn, heightFn));
    }

    private static Function<MinecraftWindowContext, Integer> no() {
        return r -> -1;
    }
}
