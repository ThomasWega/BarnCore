package com.bof.core.region.storage;

import com.bof.barn.world_generator.data.SchematicsStorage;
import com.bof.core.region.BarnRegion;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RegionsStorage {
    public static List<BarnRegion> regions = new ArrayList<>();

    public static void convertToRegions() {
        regions = SchematicsStorage.getPastedRegions().stream()
                .map(BarnRegion::new)
                .toList();
    }
}
