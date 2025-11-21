package com.skyspace.skycore.gui;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GuiPaginatedLayout extends GuiLayout {

    private final List<ItemStack> elements;
    private int page = 0;

    public GuiPaginatedLayout(String title, int size, List<ItemStack> elements) {
        super(title, size);
        this.elements = elements;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = Math.max(page, 0);
    }

    public boolean hasNext() {
        return (page + 1) * 45 < elements.size();
    }

    public boolean hasPrevious() {
        return page > 0;
    }

    public List<ItemStack> getPageItems() {
        int start = page * 45;
        int end = Math.min(start + 45, elements.size());
        return elements.subList(start, end);
    }
}
