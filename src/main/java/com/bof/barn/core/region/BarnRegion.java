package com.bof.barn.core.region;


import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalType;
import com.bof.barn.core.region.plot.harvestable.farm.CropType;
import com.bof.barn.core.region.plot.container.barn.BarnPlot;
import com.bof.barn.core.region.plot.container.silo.SiloPlot;
import com.bof.barn.core.region.plot.setting.PlotSetting;
import com.bof.barn.core.region.setting.SettingState;
import com.bof.barn.core.utils.HarvestableUtils;
import com.bof.toolkit.utils.NumberUtils;
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
    // otherwise stackoverflow is produced (https://github.com/projectlombok/lombok/issues/993)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Map<PlotType, Set<AbstractPlot>> plots;
    private HologramPool hologramPool;
    private InteractiveHologramPool interactiveHologramPool;
    private float farmCoins = 10000;

    public float getFarmCoinsRounded(int roundNum) {
        return NumberUtils.roundBy(this.farmCoins, roundNum);
    }

    /**
     * @param value Value to check against
     * @return Whether the region has enough balance
     */
    public boolean hasEnoughCoins(float value) {
        return this.farmCoins >= value;
    }

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
     * Retrieves the number of plots that have the {@link PlotSetting} set to {@link SettingState#ON}
     *
     * @param settingClazz The class representing the type of PlotSetting.
     * @return The number of plots associated with the specified PlotSetting.
     * @see #getSettingPlotsCount(Class, SettingState)
     */
    public int getSettingPlotsCount(@NotNull Class<? extends PlotSetting> settingClazz) {
        return this.getSettingPlotsCount(settingClazz, SettingState.ON);
    }

    /**
     * Retrieves the number of plots that have the {@link PlotSetting} set to specified value
     *
     * @param settingClazz The class representing the type of PlotSetting.
     * @param state        toggle state to check against
     * @return The number of plots associated with the specified PlotSetting.
     * @see #getSettingPlotsCount(Class)
     */
    public int getSettingPlotsCount(@NotNull Class<? extends PlotSetting> settingClazz, @NotNull SettingState state) {
        return this.getToggledSettingPlots(settingClazz, state).size();
    }

    /**
     * @param settingClazz The class representing the type of PlotSetting.
     * @return plots with specified PlotSetting set to {@link SettingState#ON}
     */
    public @NotNull Set<AbstractPlot> getToggledSettingPlots(@NotNull Class<? extends PlotSetting> settingClazz) {
        return this.getToggledSettingPlots(settingClazz, SettingState.ON);
    }


    /**
     * @param settingClazz The class representing the type of PlotSetting.
     * @return plots with specified PlotSetting set to {@link SettingState#OFF}
     */
    public @NotNull Set<AbstractPlot> getUnToggledSettingPlots(@NotNull Class<? extends PlotSetting> settingClazz) {
        return this.getToggledSettingPlots(settingClazz, SettingState.OFF);
    }

    /**
     * @param settingClazz The class representing the type of PlotSetting.
     * @return plots with specified PlotSetting set to {@link SettingState#LOCKED}
     */
    public @NotNull Set<AbstractPlot> getLockedSettingPlots(@NotNull Class<? extends PlotSetting> settingClazz) {
        return this.getToggledSettingPlots(settingClazz, SettingState.LOCKED);
    }

    /**
     * @param settingClazz The class representing the type of PlotSetting.
     * @return plots with specified PlotSetting not set to {@link SettingState#LOCKED}
     */
    public @NotNull Set<AbstractPlot> getUnlockedSettingPlots(@NotNull Class<? extends PlotSetting> settingClazz) {
        return this.plots.values().stream()
                .flatMap(Set::stream)
                .filter(plot -> plot.hasSetting(settingClazz))
                .filter(plot -> plot.getSetting(settingClazz).isUnlocked())
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves the plots that have the {@link PlotSetting} set to specified value
     *
     * @param settingClazz The class representing the type of PlotSetting.
     * @param state        toggle state to check against
     * @return plots with specified PlotSetting and value
     * @see #getToggledSettingPlots(Class)
     */
    public @NotNull Set<AbstractPlot> getToggledSettingPlots(@NotNull Class<? extends PlotSetting> settingClazz, @NotNull SettingState state) {
        return this.plots.values().stream()
                .flatMap(Set::stream)
                .filter(plot -> plot.hasSetting(settingClazz))
                .filter(plot -> plot.isSetting(settingClazz, state))
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
    public @NotNull Set<AbstractPlot> getLockedPlots(@NotNull PlotType type) {
        // TODO get actual locked plots when locked plots mechanism exists
        return this.plots.get(type);
    }

    /**
     * @return Silo that is not full and has the largest capacity out of the free silos
     */
    public @NotNull Optional<SiloPlot> getFreeSilo() {
        return this.plots.get(PlotType.SILO).stream()
                .map(plot -> (SiloPlot) plot)
                .filter(siloPlot -> !siloPlot.isFull())
                .max(Comparator.comparingInt(SiloPlot::getCapacity));
    }

    /**
     * @return Barn that is not full and has the largest capacity out of the free silos
     */
    public @NotNull Optional<BarnPlot> getFreeBarn() {
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
    public @NotNull Optional<AbstractPlot> getPlot(@NotNull PlotType type, int id) {
        return this.plots.get(type).stream()
                .filter(plot -> plot.getId() == id)
                .findAny();
    }

    /**
     * @return Set of all currently online members + owner (if online)
     */
    public @NotNull Set<Player> getAllOnlinePlayers() {
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
