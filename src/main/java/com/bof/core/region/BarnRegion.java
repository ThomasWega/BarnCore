package com.bof.core.region;


import com.bof.core.plots.Plot;
import com.bof.core.plots.PlotType;
import com.github.unldenis.hologram.HologramPool;
import com.github.unldenis.hologram.InteractiveHologramPool;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class BarnRegion {
    private final BoundingBox box;
    private boolean isAssigned = false;
    private @Nullable Player owner;
    private Location spawnLocation;
    private Map<PlotType, Set<Plot>> plots;
    private final Set<UUID> members = new HashSet<>();
    private HologramPool hologramPool;
    private InteractiveHologramPool interactiveHologramPool;

    public Set<Player> getAllPlayers() {
        Set<Player> onlineMembers = members.stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        onlineMembers.add(owner);
        return onlineMembers;
    }
}
