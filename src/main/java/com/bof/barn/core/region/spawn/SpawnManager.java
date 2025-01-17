package com.bof.barn.core.region.spawn;

import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.events.RegionCreatedEvent;
import com.bof.barn.core.utils.BoxUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

/**
 * Handles setting the spawn location for {@link BarnRegion}
 */
public class SpawnManager implements Listener {

    @EventHandler
    private void onRegionCreate(RegionCreatedEvent event) {
        this.handleSpawnLocation(event.getRegion());
    }

    private void handleSpawnLocation(BarnRegion region) {
        Optional<Location> optSpawnLoc = BoxUtils.identifySpawn(region.getBox());
        // remove the sign
        optSpawnLoc.ifPresent(loc -> loc.getBlock().setType(Material.AIR));
        region.setSpawnLocation(optSpawnLoc.map(loc -> loc.add(0, 2, 0))
                .orElse(BoxUtils.getCenterLocation(region.getBox())));
    }
}
