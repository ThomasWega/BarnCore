package com.bof.barn.core.region;


import com.bof.barn.core.region.plot.Plot;
import com.bof.barn.core.region.plot.PlotSetting;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalType;
import com.bof.barn.core.region.plot.harvestable.farm.CropType;
import com.bof.barn.core.region.plot.selling.barn.BarnPlot;
import com.bof.barn.core.region.plot.selling.silo.SiloPlot;
import com.bof.barn.core.utils.HarvestableUtils;
import com.github.unldenis.hologram.HologramPool;
import com.github.unldenis.hologram.InteractiveHologramPool;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
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
    private final int cropsInventoryCapacity = 100;
    private final List<ItemStack> animalInventory = new ArrayList<>();
    private final int animalInventoryCapacity = 100;
    private boolean isAssigned = false;
    private Player owner;
    private Location spawnLocation;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Map<PlotType, Set<Plot>> plots;
    private HologramPool hologramPool;
    private InteractiveHologramPool interactiveHologramPool;
    private float farmCoins = 0;

    /**
     * @param addValue Amount to be added to the total
     */
    public void addFarmCoins(float addValue) {
        this.farmCoins += addValue;
    }

    /**
     * @param removeValue Amount to remove from the total
     */
    public void removeFarmCoins(float removeValue) {
        this.farmCoins -= removeValue;
    }

    /**
     * @return Whether the items in {@link #getCropsInventory()} count exceeds or equals the {@link #getCropsInventoryCapacity()}
     */
    public boolean isCropsInvFull() {
        return this.cropsInventory.size() >= this.cropsInventoryCapacity;
    }

    /**
     * @return Whether the items in {@link #getAnimalInventory()} count exceeds or equals the {@link #getAnimalInventoryCapacity()}
     */
    public boolean isAnimalInvFull() {
        return this.animalInventory.size() >= this.animalInventoryCapacity;
    }

    /**
     * @param crops Crop items to try to add
     * @return List of items that couldn't be added to the inventory
     */
    public @NotNull List<ItemStack> addCropsToInventory(@NotNull ItemStack... crops) {
        return this.addCropsToInventory(Arrays.asList(crops));
    }

    /**
     * @param itemStacks Crop items to try to add
     * @return List of items that couldn't be added to the inventory
     */
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

    /**
     * Removes the given crops items from the regions inventory
     *
     * @param crops Crops to remove
     * @return Value of the crops removed
     */
    public float removeCropsFromInventory(@NotNull ItemStack... crops) {
        return this.removeCropsFromInventory(Arrays.asList(crops));
    }

    /**
     * Removes the given crops items from the regions inventory
     *
     * @param crops Crops to remove
     * @return Value of the crops removed
     */
    public float removeCropsFromInventory(@NotNull Collection<ItemStack> crops) {
        // Create a copy of the collection to avoid ConcurrentModificationException
        // (the crops can be referenced from cropStored)
        List<ItemStack> cropsToRemove = new ArrayList<>(crops);
        // can't use removeAll, because that will remove all crops of the same type
        cropsToRemove.forEach(this.cropsInventory::remove);

        return HarvestableUtils.getValue(CropType.class, cropsToRemove);
    }

    /**
     * @param animals Animal items to try to add
     * @return List of items that couldn't be added to the inventory
     */
    public @NotNull List<ItemStack> addAnimalsToInventory(@NotNull ItemStack... animals) {
        return this.addAnimalsToInventory(Arrays.asList(animals));
    }


    /**
     * @param itemStacks Animal items to try to add
     * @return List of items that couldn't be added to the inventory
     */
    public @NotNull List<ItemStack> addAnimalsToInventory(@NotNull Collection<ItemStack> itemStacks) {
        List<ItemStack> unAdded = new ArrayList<>();
        for (ItemStack itemStack : itemStacks) {
            if (this.isAnimalInvFull()) {
                unAdded.add(itemStack);
                continue;
            }

            this.animalInventory.add(itemStack);
        }

        return unAdded;
    }

    /**
     * Removes the given animal items from the regions inventory
     *
     * @param animals Animals to remove
     * @return Value of the animals removed
     */
    public float removeAnimalsFromInventory(@NotNull ItemStack... animals) {
        return this.removeAnimalsFromInventory(Arrays.asList(animals));
    }

    /**
     * Removes the given animal items from the regions inventory
     *
     * @param animals Animals to remove
     * @return Value of the animals removed
     */
    public float removeAnimalsFromInventory(@NotNull Collection<ItemStack> animals) {
        // Create a copy of the collection to avoid ConcurrentModificationException
        // (the animals can be referenced from animalsStored)
        List<ItemStack> animalsToRemove = new ArrayList<>(animals);
        // can't use removeAll, because that will remove all animals of the same type
        animalsToRemove.forEach(this.animalInventory::remove);

        return HarvestableUtils.getValue(AnimalType.class, animalsToRemove);
    }

    /**
     * Retrieves the number of plots that have the {@link PlotSetting} set to true
     *
     * @param settingClazz The class representing the type of PlotSetting.
     * @return The number of plots associated with the specified PlotSetting.
     * @see #getSettingPlotsCount(Class, boolean)
     */
    public int getSettingPlotsCount(Class<? extends PlotSetting> settingClazz) {
        return this.getSettingPlotsCount(settingClazz, true);
    }

    /**
     * Retrieves the number of plots that have the {@link PlotSetting} set to specified value
     *
     * @param settingClazz The class representing the type of PlotSetting.
     * @param value value to check against
     * @return The number of plots associated with the specified PlotSetting.
     * @see #getSettingPlotsCount(Class)
     */
    public int getSettingPlotsCount(Class<? extends PlotSetting> settingClazz, boolean value) {
        return this.getSettingPlots(settingClazz, value).size();
    }

    /**
     * Retrieves the plots that have the {@link PlotSetting} set to true
     *
     * @param settingClazz The class representing the type of PlotSetting.
     * @return plots with specified PlotSetting set to true
     * @see #getSettingPlots(Class, boolean)
     */
    public Set<Plot> getSettingPlots(Class<? extends PlotSetting> settingClazz) {
        return this.getSettingPlots(settingClazz, true);
    }

    /**
     * Retrieves the plots that have the {@link PlotSetting} set to specified value
     *
     * @param settingClazz The class representing the type of PlotSetting.
     * @param value value to check against
     * @return plots with specified PlotSetting and value
     * @see #getSettingPlots(Class)
     */
    public Set<Plot> getSettingPlots(Class<? extends PlotSetting> settingClazz, boolean value) {
        return plots.values().stream()
                .flatMap(Set::stream)
                .filter(plot -> plot.hasSetting(settingClazz))
                .filter(plot -> plot.isSetting(settingClazz, value))
                .collect(Collectors.toSet());
    }



    /**
     * @return Filled amount of all silos combined
     */
    public int getAllSilosFilledAmount() {
        return this.plots.get(PlotType.SILO).stream()
                .map(plot -> ((SiloPlot) plot))
                .mapToInt(SiloPlot::getFilledAmount)
                .sum();
    }

    /**
     * @return Capacity of all silos combined
     */
    public int getAllSilosCapacityAmount() {
        return this.plots.get(PlotType.SILO).stream()
                .map(plot -> ((SiloPlot) plot))
                .mapToInt(SiloPlot::getCapacity)
                .sum();
    }

    /**
     * @return Filled amount of all barns combined
     */
    public int getAllBarnsFilledAmount() {
        return this.plots.get(PlotType.BARN).stream()
                .map(plot -> ((BarnPlot) plot))
                .mapToInt(BarnPlot::getFilledAmount)
                .sum();
    }

    /**
     * @return Capacity of all barns combined
     */
    public int getAllBarnsCapacityAmount() {
        return this.plots.get(PlotType.BARN).stream()
                .map(plot -> ((BarnPlot) plot))
                .mapToInt(BarnPlot::getCapacity)
                .sum();
    }

    /**
     * @param type Type of the plot
     * @return Plot that is locked
     */
    public Set<Plot> getLockedPlots(@NotNull PlotType type) {
        // TODO get actual locked plots when locked plots mechanism exists
        return this.plots.get(type);
    }

    /**
     * @return Silo that is not full and has the largest capacity out of the free silos
     */
    public Optional<SiloPlot> getFreeSilo() {
        return this.plots.get(PlotType.SILO).stream()
                .map(plot -> (SiloPlot) plot)
                .filter(siloPlot -> !siloPlot.isFull())
                .max(Comparator.comparingInt(SiloPlot::getCapacity));
    }

    /**
     * @return Barn that is not full and has the largest capacity out of the free silos
     */
    public Optional<BarnPlot> getFreeBarn() {
        return this.plots.get(PlotType.BARN).stream()
                .map(plot -> (BarnPlot) plot)
                .filter(barnPlot -> !barnPlot.isFull())
                .max(Comparator.comparingInt(BarnPlot::getCapacity));
    }


    /**
     * @return if any silo is not fully filled
     */
    public boolean hasFreeSiloPlot() {
        return this.getFreeSilo().isPresent();
    }

    /**
     * @param type Type of the plot
     * @param id   ID of the plot
     * @return Option if any plot was found, empty otherwise
     */
    public Optional<Plot> getPlot(@NotNull PlotType type, int id) {
        return this.plots.get(type).stream()
                .filter(plot -> plot.getId() == id)
                .findAny();
    }

    /**
     * @return Set of all currently online members + owner (if online)
     */
    public Set<Player> getAllOnlinePlayers() {
        Set<Player> onlineMembers = members.stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (owner != null && owner.isOnline()) {
            onlineMembers.add(owner);
        }

        return onlineMembers;
    }
}
