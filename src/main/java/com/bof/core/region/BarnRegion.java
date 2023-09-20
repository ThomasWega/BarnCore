package com.bof.core.region;


import com.bof.core.plots.Plot;
import com.bof.core.plots.PlotType;
import com.github.unldenis.hologram.HologramPool;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
public class BarnRegion {
    private final BoundingBox box;
    private boolean isAssigned = false;
    private @Nullable Player owner;
    private Location spawnLocation;
    private Map<PlotType, Set<Plot>> plots;
    private final Set<UUID> members = new HashSet<>();
    private HologramPool hologramPool;
}
