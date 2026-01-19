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

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import net.pcal.splitscreen.WindowMode.MinecraftWindowContext;
import net.pcal.splitscreen.WindowMode.Rectangle;
import net.pcal.splitscreen.WindowMode.WindowDescription;
import net.pcal.splitscreen.WindowMode.WindowStyle;
import static net.pcal.splitscreen.logging.SystemLogger.syslog;

/**
 * @author pcal
 * @since 0.0.1
 */
class ModImpl implements Mod {

    // ======================================================================
    // Fields

    private static final String MODE_PROP = "mode";
    private static final String GAP_PROP = "gap";

    private List<WindowMode> modes;
    private int gap = 1;
    private Properties config;
    private Path configPath;

    private int currentModeIndex = 0;
    private Rectangle savedWindowRect;

    ModImpl() {
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
            String gapProp = config.getProperty(GAP_PROP);
            if (gapProp != null) {
                this.gap = Integer.parseInt(gapProp);
            }
            this.modes = WindowModeImpl.getModes(gap);
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
    public WindowDescription onToggleFullscreen(final MinecraftWindowContext mcContext) {
        if (this.savedWindowRect == null || modes.get(currentModeIndex).getFor(mcContext).style() == WindowStyle.WINDOWED) {
            this.savedWindowRect = mcContext.windowRect();
        }
        currentModeIndex = (currentModeIndex + 1) % modes.size();
        final MinecraftWindowContext ctx = new MinecraftWindowContext(mcContext.screenWidth(), mcContext.screenHeight(), this.savedWindowRect);
        return this.modes.get(currentModeIndex).getFor(ctx);
    }

    @Override
    public WindowDescription onResolutionChange(final MinecraftWindowContext res) {
        return this.modes.get(this.currentModeIndex).getFor(res);
    }

    @Override
    public void onStopping() {
        try {
            this.config.put(MODE_PROP, this.modes.get(this.currentModeIndex).getName());
            this.config.put(GAP_PROP, String.valueOf(String.valueOf(this.gap)));
            try (final FileWriter fw = new FileWriter(this.configPath.toFile())) {
                this.config.store(fw, null);
            }
        } catch (Exception e) {
            syslog().error(e);
        }
    }
}
