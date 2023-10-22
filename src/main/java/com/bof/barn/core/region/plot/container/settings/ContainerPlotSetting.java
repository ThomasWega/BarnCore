package com.bof.barn.core.region.plot.container.settings;

import com.bof.barn.core.region.plot.setting.PlotSetting;
import com.bof.barn.core.region.setting.SettingState;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Settings specific to {@link com.bof.barn.core.region.plot.container.ContainerPlot}
 */
public abstract class ContainerPlotSetting extends PlotSetting {
    public ContainerPlotSetting(@NotNull String settingName, @NotNull ItemStack item, float price, @NotNull SettingState state) {
        super(settingName, item, price, state);
    }

    public ContainerPlotSetting(@NotNull String settingName, @NotNull ItemStack item, float price, @NotNull SettingState initialState, int maxLevel) {
        super(settingName, item, price, initialState, maxLevel);
    }
}
