package com.bof.core.region.plot.harvestable;

import net.kyori.adventure.text.Component;

public interface HarvestableType {
    /**
     * @return Value of the item
     */
    float getValue();

    /**
     * @return Display name of the item
     */
    Component getDisplayName();
}
