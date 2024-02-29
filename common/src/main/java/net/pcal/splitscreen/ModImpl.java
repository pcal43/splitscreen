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

import net.minecraft.client.Minecraft;
import net.pcal.splitscreen.WindowMode.MinecraftWindowContext;
import net.pcal.splitscreen.WindowMode.Rectangle;
import net.pcal.splitscreen.WindowMode.WindowDescription;
import net.pcal.splitscreen.WindowMode.WindowStyle;
import net.pcal.splitscreen.config.ConfigHandler;

import java.nio.file.Path;
import java.util.List;

import com.mojang.blaze3d.platform.Window;

import java.lang.reflect.Method;

import static net.pcal.splitscreen.logging.SystemLogger.syslog;

/**
 * @author pcal
 * @since 0.0.1
 */
class ModImpl implements Mod {

    // ======================================================================
    // Fields

    private List<WindowMode> modes;

    private int currentModeIndex = 0;
    private Rectangle savedWindowRect;

    ModImpl() {
        this.modes = WindowModeImpl.getModes();
    }

    // ======================================================================
    // Mod implementation

    @Override
    public void onInitialize(Path configDirPath) {
        ConfigHandler.load();
        updateModeIndex(ConfigHandler.get().mode);
    }

    public void updateModeIndex(String modeName) {
        try {
            if (modeName != null) {
                modeName = modeName.trim();
                for (this.currentModeIndex = 0; this.currentModeIndex < this.modes.size(); this.currentModeIndex++) {
                    if (modeName.equals(modes.get(currentModeIndex).getName())) break;
                }
                if (currentModeIndex >= this.modes.size()) {
                    syslog().warn("unknown mode " + modeName);
                    currentModeIndex = 0;
                }
            }
        } catch (Exception e) {
            syslog().error(e);
        }
    }

    private void updateWindow() {
        try {
            Method m = Window.class.getDeclaredMethod("setMode");
            m.setAccessible(true);
            m.invoke(Minecraft.getInstance().getWindow());
        } catch (Exception e) {
            syslog().error(e);
        }
    }

    @Override
    public void onUpdateConfig() {
        this.modes = WindowModeImpl.getModes();
        updateModeIndex(ConfigHandler.get().mode);
        updateWindow();
    }

    @Override
    public WindowDescription onWindowCreate(final MinecraftWindowContext res) {
        return this.modes.get(this.currentModeIndex).getFor(res);
    }

    @Override
    public WindowDescription onToggleFullscreen(final MinecraftWindowContext mcContext) {
        if (this.savedWindowRect == null || modes.get(currentModeIndex).getFor(mcContext).style() == WindowStyle.WINDOWED) {
            this.savedWindowRect = mcContext.windowRect();
        }
        currentModeIndex = (currentModeIndex + 1) % modes.size();
        final MinecraftWindowContext ctx = new MinecraftWindowContext(mcContext.screenWidth(), mcContext.screenHeight(), this.savedWindowRect);
        saveMode();
        return this.modes.get(currentModeIndex).getFor(ctx);
    }

    @Override
    public WindowDescription onResolutionChange(final MinecraftWindowContext res) {
        return this.modes.get(this.currentModeIndex).getFor(res);
    }

    @Override
    public void onStopping() {
        saveMode();
    }

    public void saveMode() {
        ConfigHandler.get().mode = this.modes.get(this.currentModeIndex).getName();
        ConfigHandler.save();
    }
}
