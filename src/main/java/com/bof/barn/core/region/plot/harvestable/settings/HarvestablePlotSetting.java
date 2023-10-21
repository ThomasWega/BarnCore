package com.bof.barn.core.region.plot.harvestable.settings;

import com.bof.barn.core.region.plot.harvestable.AbstractHarvestablePlot;
import com.bof.barn.core.region.plot.setting.PlotSetting;
import com.bof.barn.core.region.setting.SettingState;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Settings specific to {@link AbstractHarvestablePlot}
 */
public abstract class HarvestablePlotSetting extends PlotSetting {
    public HarvestablePlotSetting(@NotNull String settingName, @NotNull ItemStack item, float price, @NotNull SettingState state) {
        super(settingName, item, price, state);
    }
}
