package com.skyspace.skycore.config;

import com.skyspace.skycore.SkyCore;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Simple message accessor.
 * Uses SkyCore's main config (config.yml) under "messages.*".
 */
public class Messages {

    private final SkyCore plugin;
    private FileConfiguration config;

    public Messages(SkyCore plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public String get(String key) {
        return config.getString("messages." + key, "&cMessage missing for: " + key);
    }

    public String prefix() {
        return config.getString("messages.prefix", "<#00b7ff>&lSkyCore &r<#808080>» ");
    }
}
