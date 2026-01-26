package net.pcal.splitscreen.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

import static net.pcal.splitscreen.common.Mod.mod;

@Mod("splitscreen")
public class SplitscreenNeoforgeMod {

    private static final Logger LOGGER = LogManager.getLogger(SplitscreenNeoforgeMod.class);
    private static final Path CONFIG_DIR_PATH = Path.of("config");

    public SplitscreenNeoforgeMod(IEventBus modBus) {
        // NOTE: we can't do this in onClientSetup because neoforge does
        // some early window initialization before that, and that initialization
        // will break our mixins if the config isn't set up.  It seems
        // fairly safe to do it here.
        mod().onModInitialize(CONFIG_DIR_PATH);
        LOGGER.info("[Splitscreen] Mod initialized");
    }
}
