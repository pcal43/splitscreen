package net.pcal.splitscreen.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder.CellCreatorBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.pcal.splitscreen.WindowModeImpl;

public class ConfigScreen {

    private static final ConfigHandler config = ConfigHandler.get();

    public static Screen getConfigScreenByCloth(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("splitscreen.config.title"));

        ConfigEntryBuilder eb = builder.entryBuilder();

        ConfigCategory settings = builder.getOrCreateCategory(Component.translatable("config.settings"));

        settings.addEntry(
                eb.startDropdownMenu(
                        Component.translatable("splitscreen.config.window_mode.label"),
                        config.mode,
                        mode -> mode.toString(),
                        CellCreatorBuilder.of(mode -> Component
                                .translatable("splitscreen.config.modes." + mode.toString().toLowerCase())))
                        .setDefaultValue("WINDOWED")
                        .setSelections(WindowModeImpl.getModes().stream().map((mode) -> mode.getName()).toList())
                        .setSuggestionMode(false)
                        .setSaveConsumer(modeName -> {
                            config.mode = modeName;
                        })
                        .build());

        settings.addEntry(
                eb.startIntField(
                        Component.translatable("splitscreen.config.gap.label"),
                        config.screenGap)
                        .setDefaultValue(0)
                        .setSaveConsumer(val -> config.screenGap = val)
                        .build());

        builder.setSavingRunnable(ConfigHandler::save);

        return builder.build();

    }

}
