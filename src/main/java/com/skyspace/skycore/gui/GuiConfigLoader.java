package com.skyspace.skycore.gui;

import com.skyspace.skycore.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;

/**
 * Loads GUIs from YAML config files using a pattern-based system.
 *
 * Supports:
 *  - rows, title
 *  - pattern (like "@ @ % * $ ^ ! @ @")
 *  - items with {type, material, name, lore, sound}
 *  - static placeholders (%amount%)
 *  - dynamic placeholders (%player%, %world%, etc.) via GuiPlaceholderEngine
 *  - per-GUI handler + global action registry
 */
public class GuiConfigLoader {

    /**
     * Loads a GUI layout from a YAML file.
     *
     * @param owner              The plugin owning the file (path is relative to its data folder)
     * @param relativePath       Path like "guis/crop_hopper.yml"
     * @param player             Player opening the GUI (dynamic placeholders)
     * @param staticPlaceholders Map like %amount% -> "42"
     * @param handler            Per-GUI click handler (optional)
     */
    public static GuiLayout load(Plugin owner,
                                 String relativePath,
                                 Player player,
                                 Map<String, String> staticPlaceholders,
                                 GuiClickHandler handler) {

        // -------------------------------------
        // 1. Load YAML file
        // -------------------------------------
        File file = new File(owner.getDataFolder(), relativePath);
        if (!file.exists()) {
            owner.getLogger().severe("[SkyCore GUI] File not found: " + file.getPath());
            return null;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection guiSec = config.getConfigurationSection("gui");
        if (guiSec == null) {
            owner.getLogger().severe("[SkyCore GUI] Missing 'gui' section in " + file.getPath());
            return null;
        }

        // -------------------------------------
        // 2. GUI title + rows
        // -------------------------------------
        String title = guiSec.getString("title", "Menu");
        int rows = guiSec.getInt("rows", 1);

        if (rows < 1) rows = 1;
        if (rows > 6) rows = 6;

        List<String> pattern = guiSec.getStringList("pattern");
        if (pattern == null || pattern.isEmpty()) {
            owner.getLogger().severe("[SkyCore GUI] Missing 'pattern' in " + file.getPath());
            return null;
        }

        ConfigurationSection itemsSec = guiSec.getConfigurationSection("items");
        if (itemsSec == null) {
            owner.getLogger().severe("[SkyCore GUI] Missing 'items' section in " + file.getPath());
            return null;
        }

        // -------------------------------------
        // 3. Parse item definitions: '@', '%', etc.
        // -------------------------------------
        Map<Character, GuiItemDefinition> definitions = new HashMap<>();

        for (String key : itemsSec.getKeys(false)) {
            if (key == null || key.isEmpty()) continue;
            char symbol = key.charAt(0);

            ConfigurationSection itemSec = itemsSec.getConfigurationSection(key);
            if (itemSec == null) continue;

            String type = itemSec.getString("type", "none");

            String matName = itemSec.getString("material", "STONE");
            Material mat = Material.matchMaterial(matName);
            if (mat == null) mat = Material.STONE;

            String name = itemSec.getString("name", "");
            List<String> lore = itemSec.getStringList("lore");

            String sound = itemSec.getString("sound", null);

            GuiItemDefinition def = new GuiItemDefinition(symbol, type, mat, name, lore, sound);
            definitions.put(symbol, def);
        }

        // -------------------------------------
        // 4. Build the GuiLayout
        // -------------------------------------
        GuiLayout layout = new GuiLayout(title, rows * 9);

        // -------------------------------------
        // 5. Fill pattern
        // -------------------------------------
        for (int row = 0; row < pattern.size() && row < rows; row++) {
            String line = pattern.get(row);
            if (line == null) continue;

            String[] tokens = line.split("\\s+");

            for (int col = 0; col < tokens.length && col < 9; col++) {
                String token = tokens[col];
                if (token.isEmpty()) continue;

                char symbol = token.charAt(0);
                GuiItemDefinition def = definitions.get(symbol);
                if (def == null) continue; // unknown symbol → skip

                int slot = row * 9 + col;

                // -------------------------------------
                // 6. Apply static placeholders
                // -------------------------------------
                String staticName = applyStatic(def.getName(), staticPlaceholders);
                List<String> staticLore = applyStatic(def.getLore(), staticPlaceholders);

                // -------------------------------------
                // 7. Apply dynamic placeholders
                // -------------------------------------
                String finalName = GuiPlaceholderEngine.apply(player, staticName);
                List<String> finalLore = applyDynamic(player, staticLore);

                // -------------------------------------
                // 8. Build the item
                // -------------------------------------
                ItemBuilder builder = new ItemBuilder(def.getMaterial());

                if (finalName != null && !finalName.isEmpty())
                    builder.name(finalName);

                if (finalLore != null && !finalLore.isEmpty())
                    builder.lore(finalLore);

                layout.setItem(slot, builder.build(), (InventoryClickEvent event) -> {

                    if (!(event.getWhoClicked() instanceof Player clicker))
                        return;

                    // PS: If event is disabled, these all run fine because clicking is cancelled by GuiManager

                    // (1) Per-GUI handler
                    if (handler != null) {
                        handler.handle(clicker, def.getType(), event);
                    }

                    // (2) Global registry (close, filler, back, next, etc.)
                    GuiActionRegistry.execute(def.getType(), clicker, event);

                    // (3) Play sound, if defined
                    if (def.getSound() != null && !def.getSound().isEmpty()) {
                        clicker.playSound(clicker.getLocation(), def.getSound(), 1f, 1f);
                    }
                });
            }
        }

        return layout;
    }

    // ------------------------------------------------
    // STATIC PLACEHOLDERS (%amount%, %price%, etc.)
    // ------------------------------------------------
    private static String applyStatic(String input, Map<String, String> placeholders) {
        if (input == null || placeholders == null) return input;

        String out = input;
        for (Map.Entry<String, String> e : placeholders.entrySet()) {
            out = out.replace(e.getKey(), e.getValue());
        }
        return out;
    }

    private static List<String> applyStatic(List<String> lines, Map<String, String> placeholders) {
        if (lines == null) return null;

        List<String> result = new ArrayList<>();
        for (String line : lines) {
            result.add(applyStatic(line, placeholders));
        }
        return result;
    }

    // ------------------------------------------------
    // DYNAMIC PLACEHOLDERS (%player%, %world%, etc.)
    // ------------------------------------------------
    private static List<String> applyDynamic(Player p, List<String> lines) {
        if (lines == null) return null;

        List<String> out = new ArrayList<>();
        for (String line : lines) {
            out.add(GuiPlaceholderEngine.apply(p, line));
        }
        return out;
    }
}
