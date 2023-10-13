package com.bof.barn.core.region.plot.selling.settings;

import com.bof.barn.core.region.plot.PlotSetting;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Settings specific to {@link com.bof.barn.core.region.plot.selling.ContainerPlot}
 */
public abstract class ContainerPlotSetting extends PlotSetting {
    public ContainerPlotSetting(@NotNull String settingName, @NotNull ItemStack item, boolean toggled) {
        super(settingName, item, toggled);
    }
}
