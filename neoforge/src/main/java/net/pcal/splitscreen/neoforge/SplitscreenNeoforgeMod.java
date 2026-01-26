package net.pcal.splitscreen.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

import static net.pcal.splitscreen.common.Mod.mod;

@Mod("splitscreen")
public class SplitscreenNeoforgeMod {

    private static final Logger LOGGER = LogManager.getLogger(SplitscreenNeoforgeMod.class);
    private static final Path CONFIG_DIR_PATH = Path.of("config");

    public SplitscreenNeoforgeMod(IEventBus modBus) {
        modBus.addListener(this::onClientSetup);
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            mod().onModInitialize(CONFIG_DIR_PATH);
            LOGGER.info("[Splitscreen] Mod initialized");
        });
    }
}
