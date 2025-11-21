package com.skyspace.skycore.gui;

import com.skyspace.skycore.SkyCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Basic inventory GUI manager.
 * Other plugins can use this instead of rolling their own.
 */
public class GuiManager implements Listener {

    private final SkyCore plugin;
    private final Map<UUID, GuiLayout> openGuis = new HashMap<>();

    public GuiManager(SkyCore plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void openGui(Player player, GuiLayout layout) {
        Inventory inv = Bukkit.createInventory(player, layout.getSize(), layout.getTitle());

        layout.getItems().forEach(inv::setItem);

        openGuis.put(player.getUniqueId(), layout);
        player.openInventory(inv);
    }

    public void closeAll() {
        for (UUID uuid : openGuis.keySet()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null && p.isOnline()) {
                p.closeInventory();
            }
        }
        openGuis.clear();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        GuiLayout layout = openGuis.get(player.getUniqueId());
        if (layout == null) return;
        if (event.getView().getTitle() == null || !event.getView().getTitle().equals(layout.getTitle())) return;

        event.setCancelled(true);

        Consumer<InventoryClickEvent> action = layout.getActions().get(event.getSlot());
        if (action != null) {
            action.accept(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        GuiLayout layout = openGuis.get(player.getUniqueId());
        if (layout == null) return;
        if (event.getView().getTitle() == null || !event.getView().getTitle().equals(layout.getTitle())) return;

        openGuis.remove(player.getUniqueId());
    }
}
