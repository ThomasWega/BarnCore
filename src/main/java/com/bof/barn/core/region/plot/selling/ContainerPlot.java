package com.bof.barn.core.region.plot.selling;


import com.bof.barn.core.region.plot.Plot;
import com.bof.barn.core.region.plot.harvestable.HarvestableType;
import com.bof.barn.core.region.plot.selling.settings.AutoSellSetting;
import com.bof.barn.core.utils.HarvestableUtils;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface ContainerPlot<T extends HarvestableType> extends Plot {

    /**
     * @return Type of harvestables that the plot stores
     */
    @NotNull Class<T> getStoreType();

    /**
     * @return ItemStacks that are stored in this container
     */
    @NotNull List<ItemStack> getStored();

    /**
     * @return Max capacity the container can take
     */
    int getCapacity();

    /**
     * @return Whether the {@link #getFilledAmount()} exceeds or equals {@link #getCapacity()}
     */
    boolean isFull();

    /**
     * @return Get amount of crops stored
     */
    int getFilledAmount();

    /**
     * @return Filled amount in percentage
     * @see #getFilledPercentageRounded(int)
     */
    float getFilledPercentage();

    /**
     * @param roundNum Decimal numbers to round up to
     * @return Filled amount in percentage rounded up
     * @see #getFilledPercentage()
     */
    float getFilledPercentageRounded(int roundNum);

    /**
     * Tries to add the items to the silo. Handles the {@link com.bof.barn.core.region.plot.selling.settings.AutoSellSetting}
     *
     * @param harvestables Harvestable items to try to add
     * @return List of items that couldn't be added
     */
    default @NotNull List<ItemStack> addHarvestablesToContainer(@NotNull ItemStack... harvestables) {
        return this.addHarvestablesToContainer(Arrays.asList(harvestables));
    }

    /**
     * Tries to add the items to the silo. Handles the {@link com.bof.barn.core.region.plot.selling.settings.AutoSellSetting}
     *
     * @param harvestables Harvestable items to try to add
     * @return List of items that couldn't be added
     */
    default @NotNull List<ItemStack> addHarvestablesToContainer(@NotNull Collection<ItemStack> harvestables) {
        List<ItemStack> unAdded = new ArrayList<>();
        for (ItemStack itemStack : harvestables) {
            if (this.isFull()) {
                unAdded.add(itemStack);
                continue;
            }

            if (this.isSetting(AutoSellSetting.class)) {
                this.sellHarvestables(itemStack);
            } else {
                this.getStored().add(itemStack);
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
    default float sellHarvestables(@NotNull ItemStack... harvestables) {
        return this.sellHarvestables(Arrays.asList(harvestables));
    }

    /**
     * Remove the harvestables from the container and calculate the value of them.
     * Then add that value in farm coins to the regions balance
     *
     * @param harvestables Harvestable items to sell
     * @return Value of the harvestables sold
     */
    default float sellHarvestables(@NotNull Collection<ItemStack> harvestables) {
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
    default void removeHarvestablesFromContainer(@NotNull Collection<ItemStack> harvestables) {
        List<ItemStack> harvestablesToRemove = new ArrayList<>(harvestables);
        harvestablesToRemove.forEach(this.getStored()::remove);
    }
}
