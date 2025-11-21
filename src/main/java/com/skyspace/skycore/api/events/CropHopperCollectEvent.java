package com.skyspace.skycore.api.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Fired when a Crop Hopper collects resources.
 * SkyHoppers (or any other plugin) can call this,
 * and other plugins can listen for it.
 */
public class CropHopperCollectEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Location hopperLocation;
    private final Material cropType;
    private long amount;
    private final @Nullable Player owner;

    public CropHopperCollectEvent(@Nullable Player owner,
                                  @NotNull Location hopperLocation,
                                  @NotNull Material cropType,
                                  long amount) {
        this.owner = owner;
        this.hopperLocation = hopperLocation;
        this.cropType = cropType;
        this.amount = amount;
    }

    @Nullable
    public Player getOwner() {
        return owner;
    }

    @NotNull
    public Location getHopperLocation() {
        return hopperLocation;
    }

    @NotNull
    public Material getCropType() {
        return cropType;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
