package net.pcal.splitscreen.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

import static net.pcal.splitscreen.Mod.mod;

@Mod("splitscreen-support")
public class SplitscreenNeoforgeMod {

    private static final Logger LOGGER = LogManager.getLogger(SplitscreenNeoforgeMod.class);
    private static final Path CONFIG_DIR_PATH = Path.of("config");

    public SplitscreenNeoforgeMod(IEventBus modBus) {
        LOGGER.info("[Splitscreen] Mod constructor called");
        modBus.addListener(SplitscreenNeoforgeMod::onCommonSetup);
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("[Splitscreen] FMLCommonSetupEvent fired - runs on both client and server");
        mod().onInitialize(CONFIG_DIR_PATH);
    }

}

