package com.bof.barn.core.region.plot.container;


import com.bof.barn.core.Core;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.HarvestableType;
import com.bof.barn.core.region.plot.container.settings.impl.AutoSellSetting;
import com.bof.barn.core.utils.HarvestableUtils;
import com.bof.toolkit.utils.NumberUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public abstract class ContainerPlot<T extends HarvestableType> extends AbstractPlot {
    /**
     * @return Type of harvestables that the plot stores
     */
    private final @NotNull Class<T> storeType;
    /**
     * @return ItemStacks that are stored in this container
     */
    private final @NotNull List<ItemStack> storedItems = new ArrayList<>();
    /**
     * @return Max capacity the container can take
     */
    private int capacity = 1000;

    public ContainerPlot(@NotNull Core plugin, @NotNull PlotType type, @NotNull BarnRegion owningRegion, @NotNull BoundingBox box, int id, @NotNull Class<T> storeType) {
        super(plugin, type, owningRegion, box, id);
        this.storeType = storeType;
    }

    /**
     * @return Whether the {@link #getFilledAmount()} exceeds or equals {@link #getCapacity()}
     */
    public boolean isFull() {
        return this.getFilledAmount() >= this.capacity;
    }

    /**
     * @return Get amount of crops stored
     */
    public int getFilledAmount() {
        return this.storedItems.stream()
                .mapToInt(ItemStack::getAmount)
                .sum();
    }


    /**
     * @return Filled amount in percentage
     * @see #getFilledPercentageRounded(int)
     */
    public float getFilledPercentage() {
        float percentage = (float) this.getFilledAmount() / this.capacity * 100;
        return Math.min(100, percentage); // Ensure the percentage doesn't exceed 100
    }

    /**
     * @param roundNum Decimal numbers to round up to
     * @return Filled amount in percentage rounded up
     * @see #getFilledPercentage()
     */
    public float getFilledPercentageRounded(int roundNum) {
        return NumberUtils.roundBy(this.getFilledPercentage(), roundNum);
    }

    /**
     * Tries to add the items to the silo. Handles the {@link AutoSellSetting}
     *
     * @param harvestables Harvestable items to try to add
     * @return List of items that couldn't be added
     */
    public @NotNull List<ItemStack> addHarvestablesToContainer(@NotNull ItemStack... harvestables) {
        return this.addHarvestablesToContainer(Arrays.asList(harvestables));
    }

    /**
     * Tries to add the items to the silo. Handles the {@link AutoSellSetting}
     *
     * @param harvestables Harvestable items to try to add
     * @return List of items that couldn't be added
     */
    public @NotNull List<ItemStack> addHarvestablesToContainer(@NotNull Collection<ItemStack> harvestables) {
        List<ItemStack> unAdded = new ArrayList<>();
        for (ItemStack itemStack : harvestables) {
            if (this.isFull()) {
                unAdded.add(itemStack);
                continue;
            }

            if (this.isSetting(AutoSellSetting.class)) {
                this.sellHarvestables(itemStack);
            } else {
                this.getStoredItems().add(itemStack);
            }
        }

        this.updateHologram();
        return unAdded;
    }

    /**
     * Remove the harvestables from the container and calculate the value of them.
     * Then add that value in farm coins to the regions balance
     *
     * @param harvestables Harvestable items to sell
     * @return Value of the harvestables sold
     */
    public float sellHarvestables(@NotNull ItemStack... harvestables) {
        return this.sellHarvestables(Arrays.asList(harvestables));
    }

    /**
     * Remove the harvestables from the container and calculate the value of them.
     * Then add that value in farm coins to the regions balance
     *
     * @param harvestables Harvestable items to sell
     * @return Value of the harvestables sold
     */
    public float sellHarvestables(@NotNull Collection<ItemStack> harvestables) {
        float value = HarvestableUtils.getValue(this.getStoreType(), harvestables);
        this.removeHarvestablesFromContainer(harvestables);
        this.getOwningRegion().addFarmCoins(value);
        this.updateHologram();
        return value;
    }

    /**
     * Remove the given harvestables from the container
     *
     * @param harvestables Harvestables to remove
     */
    public void removeHarvestablesFromContainer(@NotNull Collection<ItemStack> harvestables) {
        List<ItemStack> harvestablesToRemove = new ArrayList<>(harvestables);
        harvestablesToRemove.forEach(this.getStoredItems()::remove);
    }
}
