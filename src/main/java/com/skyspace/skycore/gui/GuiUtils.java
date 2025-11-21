package com.skyspace.skycore.gui;

import com.skyspace.skycore.util.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple helpers for GUI item creation.
 */
public class GuiUtils {

    public static ItemStack createItem(Material mat, String name, List<String> lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        if (name != null) {
            meta.displayName(Utils.mm(name));
        }
        if (lore != null && !lore.isEmpty()) {
            meta.lore(lore.stream().map(Utils::mm).collect(Collectors.toList()));
        }

        item.setItemMeta(meta);
        return item;
    }
}
