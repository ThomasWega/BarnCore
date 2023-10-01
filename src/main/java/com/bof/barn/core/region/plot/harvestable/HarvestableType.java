package com.bof.barn.core.region.plot.harvestable;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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
    Component getDisplayName();
}
