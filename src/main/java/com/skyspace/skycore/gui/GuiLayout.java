package com.skyspace.skycore.gui;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Represents a GUI layout:
 * - title
 * - size (must be multiple of 9, 9–54)
 * - items and click actions by slot
 */
public class GuiLayout {

    private final String title;
    private final int size;

    private final Map<Integer, ItemStack> items = new HashMap<>();
    private final Map<Integer, Consumer<InventoryClickEvent>> actions = new HashMap<>();

    public GuiLayout(String title, int size) {
        this.title = title;
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public GuiLayout setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> action) {
        items.put(slot, item);
        if (action != null) {
            actions.put(slot, action);
        }
        return this;
    }

    public Map<Integer, ItemStack> getItems() {
        return items;
    }

    public Map<Integer, Consumer<InventoryClickEvent>> getActions() {
        return actions;
    }
}
