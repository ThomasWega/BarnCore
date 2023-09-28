package com.bof.core.region;

import com.bof.core.Core;
import com.bof.core.region.event.RegionCreatedEvent;
import com.bof.core.region.storage.RegionStorage;
import com.github.unldenis.hologram.HologramPool;
import com.github.unldenis.hologram.InteractiveHologramPool;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class RegionManager implements Listener {
    private final Core plugin;
    private final RegionStorage storage;

    public RegionManager(@NotNull Core plugin) {
        this.plugin = plugin;
        this.storage = plugin.getRegionStorage();
    }

    /**
     * Try assigning region to player
     *
     * @param player Player to assign the region for
     * @return true if assign was successful, false otherwise
     */
    public boolean assignRegion(@NotNull Player player) {
        Optional<BarnRegion> optRegion = getFreeRegion();
        if (optRegion.isPresent()) {
            BarnRegion region = optRegion.get();
            region.setOwner(player);
            region.setAssigned(true);
            player.teleport(region.getSpawnLocation());
            player.sendMessage("TO ADD - assigned region to you");
            return true;
        }
        return false;
    }

    public boolean deAssignRegion(@NotNull Player player) {
        return this.storage.getRegions().stream()
                .filter(barnRegion -> barnRegion.getOwner() == player)
                .peek(barnRegion -> {
                    barnRegion.setOwner(null);
                    barnRegion.setAssigned(false);

                })
                .findAny()
                .isPresent();
    }

    public Optional<BarnRegion> getRegionOf(@NotNull Player player) {
        return this.storage.getRegions().stream()
                .filter(barnRegion -> barnRegion.getOwner() == player)
                .findAny();
    }


    public boolean isFreeRegionAvailable() {
        return getFreeRegion().isPresent();
    }

    public Optional<BarnRegion> getFreeRegion() {
        return this.storage.getRegions().stream()
                .filter(barnRegion -> !barnRegion.isAssigned())
                .findFirst();
    }

    /**
     * @return List of all free regions, or empty list if none are free
     */
    public @NotNull List<BarnRegion> getAllFreeRegions() {
        return this.storage.getRegions().stream()
                .filter(barnRegion -> !barnRegion.isAssigned())
                .toList();
    }

    public @NotNull List<BarnRegion> getAllOccupiedRegions() {
        return this.storage.getRegions().stream()
                .filter(BarnRegion::isAssigned)
                .toList();
    }

    @EventHandler
    private void onHologramInteract(PlayerHologramInteractEvent event) {
        getAllOccupiedRegions().forEach(region -> region.getPlots().values().forEach(plots -> plots.forEach(plot ->
                plot.getHologramAction().accept(event))));
    }

    @EventHandler(priority = EventPriority.LOW)
    private void onRegionCreate(RegionCreatedEvent event) {
        this.handleHologram(event.getRegion());
    }

    private void handleHologram(BarnRegion region) {
        HologramPool holoPool = new HologramPool(plugin, 100);
        region.setHologramPool(holoPool);

        InteractiveHologramPool interactiveHoloPool = new InteractiveHologramPool(holoPool, 0f, 5f);
        region.setInteractiveHologramPool(interactiveHoloPool);
    }
}
