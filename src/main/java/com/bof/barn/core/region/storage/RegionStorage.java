package com.bof.barn.core.region.storage;

import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.events.RegionCreatedEvent;
import com.bof.barn.world_generator.data.SchematicsStorage;
import lombok.Data;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Holds all the {@link BarnRegion}s
 */
@Data
public class RegionStorage {
    public Set<BarnRegion> regions = new HashSet<>();

    /**
     * Converts the {@link org.bukkit.util.BoundingBox} from schematics to {@link BarnRegion} instance
     * and calls the {@link RegionCreatedEvent}
     */
    public void convertToRegions() {
        this.regions = SchematicsStorage.getPastedRegions().stream()
                .map(BarnRegion::new)
                .collect(Collectors.toSet());

        // run the events
        this.regions.forEach(region -> Bukkit.getPluginManager().callEvent(new RegionCreatedEvent(region)));
    }
}
