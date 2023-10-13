package com.bof.barn.core.region.plot.harvestable.setting;

import com.bof.barn.core.region.plot.PlotSetting;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Settings specific to {@link com.bof.barn.core.region.plot.harvestable.HarvestablePlot}
 */
public abstract class HarvestablePlotSetting extends PlotSetting {
    public HarvestablePlotSetting(@NotNull String settingName, @NotNull ItemStack item, boolean toggled) {
        super(settingName, item, toggled);
    }
}
