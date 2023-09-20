package com.bof.core.region;

import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static com.bof.core.region.storage.RegionsStorage.regions;

public class RegionManager implements Listener {

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
        return regions.stream()
                .filter(barnRegion -> barnRegion.getOwner() == player)
                .peek(barnRegion -> {
                    barnRegion.setOwner(null);
                    barnRegion.setAssigned(false);
                })
                .findAny()
                .isPresent();
    }

    public Optional<BarnRegion> getRegionOf(@NotNull Player player) {
        return regions.stream()
                .filter(barnRegion -> barnRegion.getOwner() == player)
                .findAny();
    }


    public boolean isFreeRegionAvailable() {
        return getFreeRegion().isPresent();
    }

    public Optional<BarnRegion> getFreeRegion() {
        return regions.stream()
                .filter(barnRegion -> !barnRegion.isAssigned())
                .findFirst();
    }

    /**
     * @return List of all free regions, or empty list if none are free
     */
    public @NotNull List<BarnRegion> getAllFreeRegions() {
        return regions.stream()
                .filter(barnRegion -> !barnRegion.isAssigned())
                .toList();
    }

    public @NotNull List<BarnRegion> getAllOccupiedRegions() {
        return regions.stream()
                .filter(BarnRegion::isAssigned)
                .toList();
    }

    @EventHandler
    private void onHologramInteract(PlayerHologramInteractEvent event) {
        getAllOccupiedRegions().forEach(region -> region.getPlots().values().forEach(plots -> plots.forEach(plot ->
                plot.getHologramAction().accept(event))));
    }
}
