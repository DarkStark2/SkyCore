package com.skyspace.skycore.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class GuiActionRegistry {

    private static final Map<String, BiConsumer<Player, InventoryClickEvent>> actions = new HashMap<>();

    /**
     * Register a new GUI action type globally.
     */
    public static void register(String type, BiConsumer<Player, InventoryClickEvent> handler) {
        actions.put(type.toLowerCase(), handler);
    }

    /**
     * Executes the action if one is registered.
     */
    public static void execute(String type, Player player, InventoryClickEvent event) {
        BiConsumer<Player, InventoryClickEvent> action =
                actions.get(type == null ? "" : type.toLowerCase());

        if (action != null) {
            action.accept(player, event);
        }
    }

    public static boolean exists(String type) {
        return actions.containsKey(type.toLowerCase());
    }
}
