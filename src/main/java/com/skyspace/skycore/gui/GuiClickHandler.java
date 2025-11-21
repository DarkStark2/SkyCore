package com.skyspace.skycore.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Called when a player clicks an item in a GUI loaded from config.
 * "type" is the logical type from the config (e.g. "withdraw-1").
 */
@FunctionalInterface
public interface GuiClickHandler {

    void handle(Player player, String type, InventoryClickEvent event);
}
