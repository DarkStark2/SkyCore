package com.skyspace.skycore.gui;

import org.bukkit.Material;

import java.util.List;

public class GuiItemDefinition {

    private final char symbol;
    private final String type;
    private final Material material;
    private final String name;
    private final List<String> lore;
    private final String sound;

    public GuiItemDefinition(char symbol, String type, Material material, String name, List<String> lore, String sound) {
        this.symbol = symbol;
        this.type = type;
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.sound = sound;
    }

    public char getSymbol() { return symbol; }

    public String getType() { return type; }

    public Material getMaterial() { return material; }

    public String getName() { return name; }

    public List<String> getLore() { return lore; }

    public String getSound() { return sound; }
}
