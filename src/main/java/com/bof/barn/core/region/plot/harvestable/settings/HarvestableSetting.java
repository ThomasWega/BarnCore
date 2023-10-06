package com.bof.barn.core.region.plot.harvestable.settings;

import com.bof.barn.core.region.plot.PlotSetting;
import org.jetbrains.annotations.NotNull;

/**
 * Settings specific to {@link com.bof.barn.core.region.plot.harvestable.HarvestablePlot}
 */
public abstract class HarvestableSetting extends PlotSetting {
    public HarvestableSetting(@NotNull String settingName, boolean toggled) {
        super(settingName, toggled);
    }
}
