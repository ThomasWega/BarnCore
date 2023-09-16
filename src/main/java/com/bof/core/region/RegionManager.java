package com.bof.core.region;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static com.bof.core.region.storage.RegionsStorage.regions;

public class RegionManager {

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
            // TODO: uncomment, but the region either needs to have a spawn position or center
           // player.teleport(region.getBox().getCenter().toLocation(player.getWorld()));
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
}
