package com.bof.barn.core.region.storage;

import com.bof.barn.core.Core;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.RegionUUIDGenerator;
import com.bof.barn.core.region.events.RegionCreatedEvent;
import com.bof.barn.world_generator.data.SchematicsStorage;
import lombok.Data;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Holds all the {@link BarnRegion}s
 */
@Data
public class RegionStorage {
    private final Core plugin;
    private final RegionUUIDGenerator regionUUIDGenerator = new RegionUUIDGenerator();
    public Set<BarnRegion> regions = new HashSet<>();

    /**
     * Converts the {@link org.bukkit.util.BoundingBox} from schematics to {@link BarnRegion} instance
     * and calls the {@link RegionCreatedEvent}
     */
    public void convertToRegions() {
        this.regions = SchematicsStorage.getPastedRegions().stream()
                .map(boundingBox -> new BarnRegion(this.plugin, this.regionUUIDGenerator.generateUniqueUUID(), boundingBox))
                .collect(Collectors.toSet());

        // run the events
        this.regions.forEach(region -> Bukkit.getPluginManager().callEvent(new RegionCreatedEvent(region)));
    }

    public @NotNull Optional<BarnRegion> getByUUID(@NotNull UUID uuid) {
        return this.regions.stream()
                .filter(barnRegion -> barnRegion.getUuid().equals(uuid))
                .findAny();
    }
}
