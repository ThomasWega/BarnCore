package com.bof.core.region;


import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import javax.annotation.Nullable;

@Data
public class BarnRegion {
    private final BoundingBox box;
    private boolean isAssigned = false;
    private @Nullable Player owner;
}
