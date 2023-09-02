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

import net.pcal.splitscreen.WindowMode.MinecraftWindowContext;
import net.pcal.splitscreen.WindowMode.Rectangle;
import net.pcal.splitscreen.WindowMode.WindowDescription;
import net.pcal.splitscreen.WindowMode.WindowStyle;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import static net.pcal.splitscreen.logging.SystemLogger.syslog;

/**
 * @author pcal
 * @since 0.0.1
 */
class ModImpl implements Mod {

    // ======================================================================
    // Fields

    private static final String MODE_PROP = "mode";

    private final List<WindowMode> modes;
    private Properties config;
    private Path configPath;

    private int currentModeIndex = 0;
    private Rectangle savedWindowSize;

    ModImpl() {
        this.modes = WindowModeImpl.getModes();
    }

    // ======================================================================
    // Mod implementation

    @Override
    public void onInitialize(Path configDirPath) {
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
        } catch (Exception e) {
            syslog().error(e);
        }
    }

    @Override
    public WindowDescription onWindowCreate(final MinecraftWindowContext res) {
        return this.modes.get(this.currentModeIndex).getFor(res);
    }

    @Override
    public WindowDescription onToggleFullscreen(final MinecraftWindowContext res) {
        if (this.savedWindowSize == null || modes.get(currentModeIndex).getFor(res).style() == WindowStyle.WINDOWED) {
            this.savedWindowSize = res.savedWindowSize();
        }
        currentModeIndex = (currentModeIndex + 1) % modes.size();
        return this.modes.get(currentModeIndex).getFor(new MinecraftWindowContext(res.screenWidth(), res.screenHeight(), savedWindowSize));
    }

    @Override
    public WindowDescription onResolutionChange(final MinecraftWindowContext res) {
        return this.modes.get(this.currentModeIndex).getFor(res);
    }

    @Override
    public void onStopping() {
        try {
            this.config.put(MODE_PROP, this.modes.get(this.currentModeIndex).getName());
            try (final FileWriter fw = new FileWriter(this.configPath.toFile())) {
                this.config.store(fw, null);
            }
        } catch (Exception e) {
            syslog().error(e);
        }
    }
}
