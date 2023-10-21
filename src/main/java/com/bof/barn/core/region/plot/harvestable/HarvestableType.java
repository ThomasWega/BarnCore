package com.bof.barn.core.region.plot.harvestable;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Things that can be harvested on {@link AbstractHarvestablePlot}
 */
public interface HarvestableType {
    /**
     * @return Item to use in menus
     */
    @NotNull ItemStack getItem();

    /**
     * @return Value of the item
     */
    float getValue();

    /**
     * @return Display name of the item
     */
    TextComponent getDisplayName();
}
