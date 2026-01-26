package net.pcal.splitscreen.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

import static net.pcal.splitscreen.common.Mod.mod;

@Mod("splitscreen")
public class SplitscreenNeoforgeMod {

    private static final Logger LOGGER = LogManager.getLogger(SplitscreenNeoforgeMod.class);
    private static final Path CONFIG_DIR_PATH = Path.of("config");
    private static boolean initialized = false;

    public SplitscreenNeoforgeMod(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener(this::onClientTick);
    }

    /**
     * With NeoForge, mod initialization is not on the main thread (I guess),
     * which causes Minecraft to complain when we try to reposition the
     * window in a thread other than main.  This is sort of a dumb workaround
     * for that.  It seems to not be an issue in later versions (e.g., 1.21.11)
     * where we didn't have to do this.
     */
    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Pre event) {
        if (!initialized) {
            initialized = true;
            mod().onModInitialize(CONFIG_DIR_PATH);
            LOGGER.info("[Splitscreen] Mod initialized");
        }
    }
}
