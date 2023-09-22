package com.bof.core.region.spawn;

import com.bof.core.region.BarnRegion;
import com.bof.core.region.event.RegionCreatedEvent;
import com.bof.core.utils.BoxUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

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
                .orElse(region.getBox().getCenter().toLocation(Bukkit.getWorld("world"))));
    }
}
