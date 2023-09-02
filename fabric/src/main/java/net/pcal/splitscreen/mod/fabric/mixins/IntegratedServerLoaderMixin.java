package net.pcal.splitscreen.mod.fabric.mixins;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.server.integrated.IntegratedServerLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Disables the annoying 'I know what I'm Doing!' prompt screen.
 *
 * @author pcal
 * @since 0.0.2
 */
@Mixin(IntegratedServerLoader.class)
public abstract class IntegratedServerLoaderMixin {
    @Shadow protected abstract void start(Screen parent, String levelName, boolean safeMode, boolean canShowBackupPrompt);

    @Overwrite
    public void start(Screen parent, String levelName) {
        final boolean safeMode = false;
        this.start(parent, levelName, safeMode, false);
    }
}
