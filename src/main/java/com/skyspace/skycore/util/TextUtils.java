package com.skyspace.skycore.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    private static final TextUtils instance = new TextUtils();
    private static final LegacyComponentSerializer legacy = LegacyComponentSerializer.legacyAmpersand();

    // Matches <#RRGGBB>
    private static final Pattern HEX_PATTERN = Pattern.compile("<#([A-Fa-f0-9]{6})>");

    public static TextUtils get() {
        return instance;
    }

    /**
     * Fully upgraded formatter:
     *  - Converts <#RRGGBB> → &x&r&r&g&g&b&b
     *  - Converts MiniMessage-like tags (<red>, <bold>, etc.) → legacy & codes
     *  - Then applies full &-color parsing through Adventure
     */
    public Component format(String text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }

        // -----------------------------------------
        // 1) Convert MiniMessage-like tags -> & codes
        // -----------------------------------------
        text = applySimpleTags(text);

        // -----------------------------------------
        // 2) Convert <#hex> -> Bukkit legacy hex
        // -----------------------------------------
        text = convertHexTags(text);

        // -----------------------------------------
        // 3) Parse final &-coded string into Component
        // -----------------------------------------
        return legacy.deserialize(text)
                .decoration(TextDecoration.ITALIC, false);
    }

    private String applySimpleTags(String text) {
        return text
                // Basic colors
                .replace("<black>", "&0")
                .replace("<dark_blue>", "&1")
                .replace("<dark_green>", "&2")
                .replace("<dark_aqua>", "&3")
                .replace("<dark_red>", "&4")
                .replace("<dark_purple>", "&5")
                .replace("<gold>", "&6")
                .replace("<gray>", "&7")
                .replace("<dark_gray>", "&8")
                .replace("<blue>", "&9")
                .replace("<green>", "&a")
                .replace("<aqua>", "&b")
                .replace("<red>", "&c")
                .replace("<light_purple>", "&d")
                .replace("<yellow>", "&e")
                .replace("<white>", "&f")

                // Common formatting
                .replace("<bold>", "&l")
                .replace("</bold>", "&r")
                .replace("<italic>", "&o")
                .replace("</italic>", "&r")
                .replace("<underlined>", "&n")
                .replace("</underlined>", "&r")
                .replace("<strikethrough>", "&m")
                .replace("</strikethrough>", "&r")
                .replace("<obfuscated>", "&k")
                .replace("</obfuscated>", "&r")

                // Reset
                .replace("<reset>", "&r");
    }

    private String convertHexTags(String text) {
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(1);
            StringBuilder legacyHex = new StringBuilder("&x");

            for (char c : hex.toCharArray()) {
                legacyHex.append("&").append(c);
            }

            matcher.appendReplacement(sb, legacyHex.toString());
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    // -------------------------------------------------------
    // Public API (unchanged from what SkyHoppers expects)
    // -------------------------------------------------------
    public Component mm(String text) {
        return format(text);
    }

    public void send(Player player, String message) {
        player.sendMessage(format(message));
    }

    public void send(CommandSender sender, String message) {
        sender.sendMessage(format(message));
    }

    public void sendPrefixed(Player player, String prefix, String message) {
        player.sendMessage(
                format(prefix + " " + message)
        );
    }

    public void sendPrefixed(CommandSender sender, String prefix, String message) {
        sender.sendMessage(
                format(prefix + " " + message)
        );
    }
}
