package com.skyspace.skycore.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Generic per-player data container.
 * You can expand this with whatever SkyCore-level state you want.
 */
public class PlayerData {

    private final UUID uuid;
    private final Map<String, Object> values = new HashMap<>();

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void set(String key, Object value) {
        values.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, T def) {
        return (T) values.getOrDefault(key, def);
    }

    public Map<String, Object> getValues() {
        return values;
    }
}
