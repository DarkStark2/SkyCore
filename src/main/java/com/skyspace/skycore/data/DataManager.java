package com.skyspace.skycore.data;

import com.skyspace.skycore.SkyCore;
import com.skyspace.skycore.util.Utils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Simple YAML-based player data manager for SkyCore.
 * This is generic and can be used by any plugin via SkyCoreAPI.
 */
public class DataManager {

    private final SkyCore plugin;
    private final Map<UUID, PlayerData> cache = new HashMap<>();

    private final File dataFile;
    private final YamlConfiguration dataConfig;

    public DataManager(SkyCore plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "playerdata.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.getParentFile().mkdirs();
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("[SkyCore] Failed to create playerdata.yml: " + e.getMessage());
            }
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        loadAll();
    }

    public PlayerData getPlayerData(UUID uuid) {
        return cache.computeIfAbsent(uuid, PlayerData::new);
    }

    public void saveAll() {
        for (Map.Entry<UUID, PlayerData> entry : cache.entrySet()) {
            String key = entry.getKey().toString();
            dataConfig.set("players." + key, entry.getValue().getValues());
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("[SkyCore] Failed to save playerdata.yml: " + e.getMessage());
        }
        Utils.debug("[SkyCore] Saved " + cache.size() + " players to playerdata.yml");
    }

    @SuppressWarnings("unchecked")
    private void loadAll() {
        if (!dataConfig.isConfigurationSection("players")) return;

        Set<String> keys = Objects.requireNonNull(dataConfig.getConfigurationSection("players")).getKeys(false);
        for (String key : keys) {
            try {
                UUID uuid = UUID.fromString(key);
                Map<String, Object> map = (Map<String, Object>) dataConfig.get("players." + key, new HashMap<>());
                PlayerData data = new PlayerData(uuid);
                data.getValues().putAll(map);
                cache.put(uuid, data);
            } catch (IllegalArgumentException ignored) {
            }
        }

        Utils.debug("[SkyCore] Loaded " + cache.size() + " players from playerdata.yml");
    }
}
