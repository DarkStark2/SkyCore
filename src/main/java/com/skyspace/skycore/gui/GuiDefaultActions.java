package com.skyspace.skycore.gui;

import org.bukkit.entity.Player;

public class GuiDefaultActions {

    public static void registerDefaults() {

        // Closes the GUI
        GuiActionRegistry.register("close", (player, event) -> {
            player.closeInventory();
        });

        // Does nothing (safe filler)
        GuiActionRegistry.register("filler", (player, event) -> {
            // no-op
        });

        // Goes back to the previous GUI if implemented later
        GuiActionRegistry.register("back", (player, event) -> {
            player.closeInventory(); // placeholder
        });

        // Pagination "next"
        GuiActionRegistry.register("next", (player, event) -> {
            // Placeholder until pagination system is added
        });
    }
}
