package com.bof.barn.core.region.plot.harvestable.setting;

import com.bof.barn.core.region.plot.harvestable.AbstractHarvestablePlot;
import com.bof.barn.core.region.plot.setting.PlotSetting;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Settings specific to {@link AbstractHarvestablePlot}
 */
public abstract class HarvestablePlotSetting extends PlotSetting {
    public HarvestablePlotSetting(@NotNull String settingName, @NotNull ItemStack item, float price, boolean toggled) {
        super(settingName, item, price, toggled);
    }
}
