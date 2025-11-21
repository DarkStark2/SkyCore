package com.skyspace.skycore.gui;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GuiPlaceholderEngine {

    @FunctionalInterface
    public interface PlaceholderProvider {
        String get(Player player);
    }

    private static final Map<String, PlaceholderProvider> providers = new HashMap<>();

    /**
     * Registers a dynamic placeholder.
     * Use the name *without* % (e.g. "player" -> %player%).
     */
    public static void register(String name, PlaceholderProvider provider) {
        providers.put(name.toLowerCase(), provider);
    }

    /**
     * Applies all registered placeholders to a string for a given player.
     */
    public static String apply(Player player, String text) {
        if (text == null) return null;

        String result = text;
        for (Map.Entry<String, PlaceholderProvider> entry : providers.entrySet()) {
            String token = "%" + entry.getKey() + "%";
            String value = entry.getValue().get(player);
            result = result.replace(token, value != null ? value : "");
        }
        return result;
    }
}
