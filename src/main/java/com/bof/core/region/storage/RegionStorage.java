package com.bof.core.region.storage;

import com.bof.barn.world_generator.data.SchematicsStorage;
import com.bof.core.region.BarnRegion;
import com.bof.core.region.event.RegionCreatedEvent;
import lombok.Data;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class RegionStorage {
    public Set<BarnRegion> regions = new HashSet<>();

    public void convertToRegions() {
        this.regions = SchematicsStorage.getPastedRegions().stream()
                .map(BarnRegion::new)
                .collect(Collectors.toSet());

        // run the events
        this.regions.forEach(region -> Bukkit.getPluginManager().callEvent(new RegionCreatedEvent(region)));
    }
}
