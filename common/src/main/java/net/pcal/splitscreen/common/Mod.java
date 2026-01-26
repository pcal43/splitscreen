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

package net.pcal.splitscreen.common;

import net.pcal.splitscreen.common.MinecraftWindow.Rectangle;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import static java.util.Objects.requireNonNull;
import static net.pcal.splitscreen.common.logging.SystemLogger.syslog;

/**
 * @author pcal
 * @since 0.0.1
 */
public class Mod {

    // ======================================================================
    // Singleton

    private static class SingletonHolder {
        private static Mod INSTANCE = new Mod();
    }

    public static Mod mod() {
        return SingletonHolder.INSTANCE;
    }

    private Mod() {}

    // ======================================================================
    // Fields

    private static final String MODE_PROP = "mode";
    private static final String GAP_PROP = "gap";

    private MinecraftWindow unpositionedWindow;
    private List<WindowMode> modes;
    private int gap = 1;
    private Properties config;
    private Path configPath;
    private int currentModeIndex = 0;
    private Rectangle savedWindowRect;

    // ======================================================================
    // Public methods

    /**
     * Called when the mod is initialized.  Loads the config and attemptes
     * to position the initial window.
     */
    public void onModInitialize(final Path configDirPath) {
        this.configPath = configDirPath.resolve("splitscreen.properties");
        this.config = new Properties();
        if (this.configPath.toFile().exists()) {
            try (final FileReader fr = new FileReader(this.configPath.toFile())) {
                this.config.load(fr);
            } catch (IOException e) {
                syslog().error(e);
            }
        }
        try {
            String gapProp = config.getProperty(GAP_PROP);
            if (gapProp != null) {
                this.gap = Integer.parseInt(gapProp);
            }
            this.modes = WindowMode.getModes(gap);
            String modeConfig = config.getProperty(MODE_PROP);
            if (modeConfig != null) {
                modeConfig = modeConfig.trim();
                for (this.currentModeIndex = 0; this.currentModeIndex < this.modes.size(); this.currentModeIndex++) {
                    if (modeConfig.equals(modes.get(currentModeIndex).getName())) break;
                }
                if (currentModeIndex >= this.modes.size()) {
                    syslog().warn("unknown mode " + modeConfig);
                    currentModeIndex = 0;
                }
            }
            if (this.unpositionedWindow != null) {
                // Deal with timing challenge in NeoForge
                repositionWindow(this.unpositionedWindow);
                unpositionedWindow = null;
            }
        } catch (Exception e) {
            syslog().error(e);
        }
    }

    /**
     * Called when the minecraft window is created.
     */
    public void onWindowCreate(final MinecraftWindow window) {
        if (this.modes == null) {
            // NeoForge creates the window before the mods are initialized, which makes
            // our life slightly harder.
            if (this.unpositionedWindow != null) syslog().error("Multiple windows created?");
            this.unpositionedWindow = requireNonNull(window);
        } else {
            repositionWindow(window);
        }
    }

    /**
     * When they press F11, we want to cycle through the screen modes.
     */
    public void onToggleFullscreen(final MinecraftWindow window) {
        if (this.savedWindowRect == null || modes.get(currentModeIndex).getStyle() == WindowStyle.WINDOWED) {
            // if we're currently in windowed mode, remember the window size for next time
            this.savedWindowRect = window.getWindowBounds();
        }
        currentModeIndex = (currentModeIndex + 1) % modes.size();
        saveConfig();
        repositionWindow(window);
    }

    /**
     * If the resolution changes, we want need to reposition the window.
     */
    public void onResolutionChange(final MinecraftWindow window) {
        repositionWindow(window);
    }

    public void onSetMode(final MinecraftWindow window) {
        repositionWindow(window);
    }

    // ======================================================================
    // Private methods

    private void repositionWindow(final MinecraftWindow window) {
        if (window == null) {
            syslog().error("Window is null");
            return;
        }
        if (this.modes == null) {
            syslog().error("Modes is null");
            return;
        }
        final WindowMode mode = modes.get(this.currentModeIndex);
        if (mode == null) {
            syslog().error("Failed to determine mode.");
        } else {
            window.reposition(mode.getStyle(), mode.getRepositionedBoundsFor(window));
        }
    }

    private synchronized void saveConfig() {
        try {
            this.config.put(MODE_PROP, this.modes.get(this.currentModeIndex).getName());
            this.config.put(GAP_PROP, String.valueOf(this.gap));
            try (final FileWriter fw = new FileWriter(this.configPath.toFile())) {
                this.config.store(fw, null);
            }
        } catch (Exception e) {
            syslog().error(e);
        }
    }
}
