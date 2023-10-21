package com.bof.barn.core.region.plot.harvestable;


import com.bof.barn.core.Core;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.settings.AutoStoreSetting;
import com.bof.barn.core.region.plot.harvestable.settings.ReplantAllSetting;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * Plot which grows/raises specific thing that can be harvested by the player
 *
 * @param <T> Types the plot is allowed to harvest
 */
@Getter
@Setter
public abstract class AbstractHarvestablePlot<T extends HarvestableType> extends AbstractPlot {
    /**
     * @return Type the plot is currently farming
     */
    public T currentlyHarvesting;


    public AbstractHarvestablePlot(@NotNull Core plugin, @NotNull PlotType type, @NotNull BarnRegion owningRegion, @NotNull BoundingBox box, int id, @NotNull T harvesting) {
        super(plugin, type, owningRegion, box, id);
        this.currentlyHarvesting = harvesting;
    }

    /**
     * Changes the type and updates the hologram
     * @param type Type to change to
     */
    public void setCurrentlyHarvesting(@NotNull T type) {
        this.currentlyHarvesting = type;
        this.updateHologram();
    }

    /**
     * Changed the type and handles the changes and updates
     *
     * @param type Type to change to
     */
    public abstract void changeType(@NotNull T type);

    /**
     * Handles the harvest of all at once
     *
     * @param player Player that initiated the harvest
     * @return amount of harvested things
     */
    public abstract int harvest(@NotNull Player player);

    /**
     * @return The amount of harvestable objects present on the plot
     */
    public abstract int getRemainingHarvestablesCount();

    /**
     * @return if any harvestable objest is present on the plot
     */
    public boolean isHarvestablePresent() {
        return this.getRemainingHarvestablesCount() > 0;
    }

    /**
     * Tries putting the items to the {@link BarnRegion#getCropsInventory()}
     *
     * @param items Items to put to the inventory
     * @return Items that couldn't be added to the inventory
     */
    public abstract @NotNull List<ItemStack> addToInventory(@NotNull ItemStack... items);

    /**
     * Tries putting the items to the {@link BarnRegion#getCropsInventory()}
     *
     * @param items Items to put to the inventory
     * @return Items that couldn't be added to the inventory
     */
    public abstract @NotNull List<ItemStack> addToInventory(@NotNull Collection<ItemStack> items);

    /**
     * Tries putting the items to the next free container.
     * If no container is free, tries putting it into the inventory
     *
     * @param items Items to put to the silo
     * @return Items that couldn't be added to the silo
     */
    public abstract @NotNull List<ItemStack> addToContainer(@NotNull ItemStack... items);

    /**
     * Tries putting the items to the next free container.
     * If no container is free, tries putting it into the inventory
     *
     * @param items Items to put to the silo
     * @return Items that couldn't be added to the silo
     */
    public abstract @NotNull List<ItemStack> addToContainer(@NotNull Collection<ItemStack> items);


    /**
     * Handles the addition of an ItemStack to the designated {@link com.bof.barn.core.region.plot.container.ContainerPlot}
     *
     * @param item The ItemStack to be added.
     * @return An AdditionResult enum indicating the outcome of the addition operation.
     */
    public @NotNull AdditionResult handleAddition(@NotNull ItemStack item) {
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

    public void handleAutoReplant() {
        // handle auto replant all
        if (this.isSetting(ReplantAllSetting.class)) {
            T harvesting = this.getCurrentlyHarvesting();
            Bukkit.getScheduler().runTaskLater(this.getPlugin(), () -> this.changeType(harvesting), 20L);
        }
    }
}
