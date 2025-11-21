package com.skyspace.skycore.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Misc utility methods shared across plugins.
 */
public class Utils {

    private static final MiniMessage mm = MiniMessage.miniMessage();

    public static Component mm(String text) {
        return mm.deserialize(text == null ? "" : text);
    }

    public static String chunkKey(Location loc) {
        return loc.getWorld().getName() + ":" + (loc.getBlockX() >> 4) + "," + (loc.getBlockZ() >> 4);
    }

    public static void debug(String msg) {
        Bukkit.getLogger().info("[DEBUG] " + msg);
    }

    public static void send(Player player, String prefix, String msg) {
        player.sendMessage(mm.deserialize((prefix == null ? "" : prefix) + (msg == null ? "" : msg)));
    }
}
