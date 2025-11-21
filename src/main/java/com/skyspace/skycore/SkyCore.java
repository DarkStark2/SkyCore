package com.skyspace.skycore;

import com.skyspace.skycore.api.SkyCoreAPI;
import com.skyspace.skycore.config.ConfigManager;
import com.skyspace.skycore.config.Messages;
import com.skyspace.skycore.data.DataManager;
import com.skyspace.skycore.gui.GuiDefaultActions;
import com.skyspace.skycore.gui.GuiManager;
import com.skyspace.skycore.gui.GuiPlaceholderEngine;
import com.skyspace.skycore.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyCore extends JavaPlugin {

    private static SkyCore instance;

    private ConfigManager configManager;
    private Messages messages;
    private DataManager dataManager;
    private GuiManager guiManager;

    public static SkyCore getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        // Ensure default config exists
        saveDefaultConfig();

        // Core components
        this.configManager = new ConfigManager(this);
        this.messages = new Messages(this);
        this.dataManager = new DataManager(this);
        this.guiManager = new GuiManager(this);

        // Initialize static API facade
        SkyCoreAPI.init(this);

        GuiDefaultActions.registerDefaults();
        registerDefaultGuiPlaceholders();

        Utils.debug("[SkyCore] Enabled successfully.");
    }

    @Override
    public void onDisable() {
        if (dataManager != null) {
            dataManager.saveAll();
        }
        if (guiManager != null) {
            guiManager.closeAll();
        }
        Utils.debug("[SkyCore] Disabled.");
        instance = null;
    }
    private void registerDefaultGuiPlaceholders() {
        GuiPlaceholderEngine.register("player", p -> p.getName());
        GuiPlaceholderEngine.register("uuid", p -> p.getUniqueId().toString());
        GuiPlaceholderEngine.register("world", p -> p.getWorld().getName());
        GuiPlaceholderEngine.register("x", p -> String.valueOf(p.getLocation().getBlockX()));
        GuiPlaceholderEngine.register("y", p -> String.valueOf(p.getLocation().getBlockY()));
        GuiPlaceholderEngine.register("z", p -> String.valueOf(p.getLocation().getBlockZ()));
        GuiPlaceholderEngine.register("online", p -> String.valueOf(p.getServer().getOnlinePlayers().size()));
        GuiPlaceholderEngine.register("server_time", p -> java.time.LocalTime.now().toString());
    }
    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Messages getMessages() {
        return messages;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }
}
