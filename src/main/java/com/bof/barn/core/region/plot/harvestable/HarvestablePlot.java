package com.bof.barn.core.region.plot.harvestable;


import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.Plot;
import com.bof.barn.core.region.plot.harvestable.settings.ReplantAllSetting;
import com.bof.barn.core.region.plot.harvestable.settings.AutoStoreSetting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;


public interface HarvestablePlot<T extends HarvestableType> extends Plot {

    /**
     * @return Type the plot is currently farming
     */
    T getCurrentlyHarvesting();

    /**
     * Changed the type and handles the changes and updates
     *
     * @param type Type to change to
     */
    void changeType(@NotNull T type);

    /**
     * Handles the harvest of all at once
     *
     * @param player Player that initiated the harvest
     * @return amount of harvested things
     */
    int harvest(@NotNull Player player);

    /**
     * @return The amount of harvestable objects present on the plot
     */
    int getRemainingHarvestables();

    /**
     * @return if any harvestable objest is present on the plot
     */
    boolean isHarvestablePresent();

    /**
     * Tries putting the items to the {@link BarnRegion#getCropsInventory()}
     *
     * @param items Items to put to the inventory
     * @return Items that couldn't be added to the inventory
     */
    @NotNull List<ItemStack> addToInventory(@NotNull ItemStack... items);

    /**
     * Tries putting the items to the {@link BarnRegion#getCropsInventory()}
     *
     * @param items Items to put to the inventory
     * @return Items that couldn't be added to the inventory
     */
    @NotNull List<ItemStack> addToInventory(@NotNull Collection<ItemStack> items);

    /**
     * Tries putting the items to the next free container.
     * If no container is free, tries putting it into the inventory
     *
     * @param items Items to put to the silo
     * @return Items that couldn't be added to the silo
     */
    @NotNull List<ItemStack> addToContainer(@NotNull ItemStack... items);

    /**
     * Tries putting the items to the next free container.
     * If no container is free, tries putting it into the inventory
     *
     * @param items Items to put to the silo
     * @return Items that couldn't be added to the silo
     */
    @NotNull List<ItemStack> addToContainer(@NotNull Collection<ItemStack> items);


    /**
     * Handles the addition of an ItemStack to the designated {@link com.bof.barn.core.region.plot.selling.ContainerPlot}
     *
     * @param item The ItemStack to be added.
     * @return An AdditionResult enum indicating the outcome of the addition operation.
     */
    default AdditionResult handleAddition(@NotNull ItemStack item) {
        if (this.isSetting(AutoStoreSetting.class)) {
            if (!this.addToContainer(item).isEmpty()) {
                if (!this.addToInventory(item).isEmpty()) {
                    return AdditionResult.INV_FULL;
                }
                return AdditionResult.CONTAINER_FULL;
            }
        } else {
            if (!this.addToInventory(item).isEmpty()) {
                return AdditionResult.INV_FULL;
            }
        }

        return AdditionResult.SUCCESS;
    }

    default void handleAutoReplant() {
        // handle auto replant all
        if (this.hasSetting(ReplantAllSetting.class)) {
            T harvesting = this.getCurrentlyHarvesting();
            Bukkit.getScheduler().runTaskLater(this.getPlugin(), () -> this.changeType(harvesting), 20L);
        }
    }
}
