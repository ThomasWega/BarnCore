package com.bof.core.region;


import com.bof.core.region.plots.HarvestablePlot;
import com.bof.core.region.plots.Plot;
import com.bof.core.region.plots.PlotType;
import com.bof.core.region.plots.silo.SiloPlot;
import com.bof.core.utils.CropUtils;
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
    private final int cropsInventorySize = 100;
    private final int autoStoreSlots = 2;
    private boolean isAssigned = false;
    private Player owner;
    private Location spawnLocation;
    private Map<PlotType, Set<Plot>> plots;
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

    public @NotNull List<ItemStack> addCropsToInventory(@NotNull ItemStack... itemStack) {
        return this.addCropsToInventory(Arrays.asList(itemStack));
    }

    public @NotNull List<ItemStack> addCropsToInventory(@NotNull Collection<ItemStack> itemStacks) {
        List<ItemStack> unAdded = new ArrayList<>();
        for (ItemStack itemStack : itemStacks) {
            if (this.isCropsInvFull()) {
                unAdded.add(itemStack);
                continue;
            }

            this.cropsInventory.add(itemStack);
        }

        return unAdded;
    }

    public float removeCropsFromInventory(@NotNull ItemStack... crops) {
        return this.removeCropsFromInventory(Arrays.asList(crops));
    }

    public float removeCropsFromInventory(@NotNull Collection<ItemStack> crops) {
        // Create a copy of the collection to avoid ConcurrentModificationException
        // (the crops can be referenced from cropStored)
        List<ItemStack> cropsToRemove = new ArrayList<>(crops);
        // can't use removeAll, because that will remove all crops of the same type
        cropsToRemove.forEach(this.cropsInventory::remove);

        return CropUtils.getValueOf(cropsToRemove);
    }

    public int getAutoStorePlotsCount() {
        return (int) plots.values().stream()
                .flatMap(Set::stream)
                .filter(plot -> plot instanceof HarvestablePlot)
                .filter(plot -> ((HarvestablePlot) plot).isAutoStore())
                .count();
    }

    public boolean hasFreeAutoStoreSlots() {
        return this.getAutoStorePlotsCount() < this.autoStoreSlots;
    }

    /**
     * @return Silo that is not full and has the largest capacity out of the free silos
     */
    public Optional<SiloPlot> getFreeSilo() {
        return plots.get(PlotType.SILO).stream()
                .filter(plot -> plot instanceof SiloPlot)
                .map(plot -> (SiloPlot) plot)
                .filter(siloPlot -> !siloPlot.isFull())
                .max(Comparator.comparingInt(SiloPlot::getCapacity));
    }


    public boolean hasFreeSiloPlot() {
        return this.getFreeSilo().isPresent();
    }

    public Optional<Plot> getPlot(@NotNull PlotType type, int id) {
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
