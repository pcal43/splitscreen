package net.pcal.splitscreen.mod.fabric.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Display the username in the upper left corner so you can tell whose screen is whose.
 *
 * @author pcal
 * @since 0.0.2
 */
@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Text text) {
        super(text);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void addText(CallbackInfo ci) {
        final Text text = Text.literal(MinecraftClient.getInstance().getSession().getUsername());
        addDrawableChild(new TextWidget(4, 4, textRenderer.getWidth(text), 10, text, this.textRenderer));
    }
}
