package com.bof.core.region;


import com.bof.core.region.plots.HarvestablePlot;
import com.bof.core.region.plots.PlotType;
import com.github.unldenis.hologram.HologramPool;
import com.github.unldenis.hologram.InteractiveHologramPool;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class BarnRegion {
    private final BoundingBox box;
    private final Set<UUID> members = new HashSet<>();
    private final List<ItemStack> cropsInventory = new ArrayList<>();
    private final int cropsInventorySize = 10;
    // TODO get actual value
    private final int autoHarvestSlots = 2;
    private boolean isAssigned = false;
    private Player owner;
    private Location spawnLocation;
    private Map<PlotType, Set<HarvestablePlot>> plots;
    private HologramPool hologramPool;
    private InteractiveHologramPool interactiveHologramPool;
    private float farmCoins = 0;

    public void addFarmCoins(float addValue) {
        this.farmCoins += addValue;
    }

    public void removeFarmCoins(float removeValue) {
        this.farmCoins -= removeValue;
    }

    public boolean isCropsInvFull() {
        return cropsInventory.size() >= cropsInventorySize;
    }

    public boolean addCrops(ItemStack... itemStack) {
        return this.addCrops(Arrays.stream(itemStack).toList());
    }

    public boolean addCrops(Collection<ItemStack> itemStacks) {
        if (isCropsInvFull()) return false;

        this.cropsInventory.addAll(itemStacks);
        return true;
    }

    public int getAutoHarvestPlotsCount() {
        return (int) plots.values().stream()
                .flatMap(Set::stream)
                .filter(HarvestablePlot::isAutoHarvest)
                .count();
    }

    public boolean hasFreeAutoHarvestSlots() {
        return this.getAutoHarvestPlotsCount() < this.autoHarvestSlots;
    }

    public Optional<HarvestablePlot> getPlot(@NotNull PlotType type, int id) {
        return this.plots.get(type).stream()
                .filter(plot -> plot.getId() == id)
                .findAny();
    }

    public Set<Player> getAllPlayers() {
        Set<Player> onlineMembers = members.stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        onlineMembers.add(owner);

        return onlineMembers;
    }
}
