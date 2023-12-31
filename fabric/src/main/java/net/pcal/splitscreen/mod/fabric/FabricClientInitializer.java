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

package net.pcal.splitscreen.mod.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.pcal.splitscreen.logging.SystemLogger;
import org.slf4j.LoggerFactory;

import static net.pcal.splitscreen.Mod.mod;


/**
 * Initializer that runs in a client.
 *
 * @author pcal
 * @since 0.0.1
 */
public class FabricClientInitializer implements ClientModInitializer {

    private static final String MOD_ID = "splitscreen";

    @Override
    public void onInitializeClient() {
        SystemLogger.Singleton.register(LoggerFactory.getLogger(MOD_ID));
        mod().onInitialize(FabricLoader.getInstance().getConfigDir());
        ClientLifecycleEvents.CLIENT_STOPPING.register(
                minecraftClient -> {
                    mod().onStopping();
                }
        );
    }
}
