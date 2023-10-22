package com.bof.barn.core.region;

import com.bof.barn.core.Core;
import com.bof.barn.core.region.events.RegionAssignedEvent;
import com.bof.barn.core.region.events.RegionCreatedEvent;
import com.bof.barn.core.region.events.RegionDeassignedEvent;
import com.bof.barn.core.region.storage.RegionStorage;
import com.github.unldenis.hologram.HologramPool;
import com.github.unldenis.hologram.InteractiveHologramPool;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles the assigning and de-assigning of regions for players
 */
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
        Optional<BarnRegion> optRegion = this.getFreeRegion();
        if (optRegion.isPresent()) {
            BarnRegion region = optRegion.get();
            region.setOwner(player.getUniqueId());
            region.setAssigned(true);
            player.teleport(region.getSpawnLocation());
            player.sendMessage("TO ADD - assigned region to you");
            Bukkit.getPluginManager().callEvent(new RegionAssignedEvent(region, player));
            return true;
        }
        return false;
    }

    /**
     * De-assign region from player
     *
     * @param player Player to de-assign the region for
     * @return true if de-assign was successful, false otherwise
     */
    public boolean deAssignRegion(@NotNull Player player) {
        return this.storage.getRegions().stream()
                .filter(barnRegion -> barnRegion.getOwner() == player.getUniqueId())
                .peek(barnRegion -> {
                    barnRegion.setOwner(null);
                    barnRegion.setAssigned(false);
                    Bukkit.getPluginManager().callEvent(new RegionDeassignedEvent(barnRegion, player));
                })
                .findAny()
                .isPresent();
    }

    /**
     * Get region that the player owns
     *
     * @param player Player to check for
     * @return Optional of owned region or empty if none was found
     */
    public Optional<BarnRegion> getRegionOf(@NotNull Player player) {
        return this.storage.getRegions().stream()
                .filter(barnRegion -> barnRegion.getOwner() == player.getUniqueId())
                .findAny();
    }


    /**
     * @return If any free region is available
     */
    public boolean isFreeRegionAvailable() {
        return getFreeRegion().isPresent();
    }

    /**
     * @return A free region (un-assigned)
     */
    public Optional<BarnRegion> getFreeRegion() {
        return this.storage.getRegions().stream()
                .filter(barnRegion -> !barnRegion.isAssigned())
                .findFirst();
    }

    /**
     * @return Set of all free regions, or empty list if none are free
     */
    public @NotNull Set<BarnRegion> getAllFreeRegions() {
        return this.storage.getRegions().stream()
                .filter(barnRegion -> !barnRegion.isAssigned())
                .collect(Collectors.toSet());
    }

    /**
     * @return Set of all regions that are currently occupied
     */
    public @NotNull Set<BarnRegion> getAllOccupiedRegions() {
        return this.storage.getRegions().stream()
                .filter(BarnRegion::isAssigned)
                .collect(Collectors.toSet());
    }

    @EventHandler
    private void onHologramInteract(PlayerHologramInteractEvent event) {
        // apply the action the plot has set for the hologram
        getAllOccupiedRegions().forEach(region -> region.getPlots().values().forEach(plots -> plots.forEach(plot ->
                plot.getHologramAction().accept(event))));
    }

    @EventHandler(priority = EventPriority.LOW)
    private void onRegionCreate(RegionCreatedEvent event) {
        this.createHoloPools(event.getRegion());
    }

    private void createHoloPools(BarnRegion region) {
        HologramPool holoPool = new HologramPool(plugin, 100);
        region.setHologramPool(holoPool);

        InteractiveHologramPool interactiveHoloPool = new InteractiveHologramPool(holoPool, 0f, 5f);
        region.setInteractiveHologramPool(interactiveHoloPool);
    }
}
