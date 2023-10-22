package com.bof.barn.core.region;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Used to generate random unique uuid for {@link BarnRegion}
 */
public class RegionUUIDGenerator {
    public static final Set<UUID> usedUUIDs = new HashSet<>();

    public UUID generateUniqueUUID() {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (usedUUIDs.contains(uuid));

        usedUUIDs.add(uuid);
        return uuid;
    }

    // You can also have a method to release a UUID when it's no longer needed
    public void releaseUUID(UUID uuid) {
        usedUUIDs.remove(uuid);
    }
}
