package net.pcal.splitscreen.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.pcal.splitscreen.Mod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ConfigHandler {
    private static final Logger logger = LoggerFactory.getLogger("config");
    private static final File file = new File(net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().toFile(), "splitscreen.json");
    private static ConfigHandler INSTANCE = new ConfigHandler();

    public int screenGap = 0;
    public String mode = "TOP";

    public static void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        file.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(INSTANCE, writer);
        } catch (IOException e) {
            logger.error("Splitscreen mod couldn't save config", e);
        }

        Mod.mod().onUpdateConfig();
    }

    public static void load() {
        Gson gson = new Gson();
        if (!file.exists()) return;
        try (Reader reader = new FileReader(file)) {
            INSTANCE = gson.fromJson(reader, ConfigHandler.class);
        } catch (Exception e) {
            logger.error("Splitscreen mod couldn't load config, deleting it...", e);
            file.delete();
        }
    }

    public static ConfigHandler get() {
        return INSTANCE;
    }

}