package com.skyspace.skycore.config;

import com.skyspace.skycore.SkyCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple multi-file config manager.
 * - SkyCore's main config = plugin.getConfig()
 * - Extra files: via getConfig("fileName") (without .yml)
 */
public class ConfigManager {

    private final SkyCore plugin;
    private final Map<String, FileConfiguration> configs = new HashMap<>();

    public ConfigManager(SkyCore plugin) {
        this.plugin = plugin;
    }

    public FileConfiguration getMainConfig() {
        return plugin.getConfig();
    }

    public FileConfiguration getConfig(String name) {
        String key = name.toLowerCase();
        return configs.computeIfAbsent(key, this::loadConfig);
    }

    public void reloadMainConfig() {
        plugin.reloadConfig();
    }

    public void reloadConfig(String name) {
        String key = name.toLowerCase();
        configs.put(key, loadConfig(key));
    }

    public void saveConfig(String name) {
        String key = name.toLowerCase();
        FileConfiguration cfg = configs.get(key);
        if (cfg == null) return;

        File file = new File(plugin.getDataFolder(), key + ".yml");
        try {
            cfg.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("[SkyCore] Failed to save " + file.getName() + ": " + e.getMessage());
        }
    }

    private FileConfiguration loadConfig(String key) {
        File file = new File(plugin.getDataFolder(), key + ".yml");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("[SkyCore] Failed to create " + file.getName() + ": " + e.getMessage());
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }
}
