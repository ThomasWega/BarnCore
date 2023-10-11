package com.bof.barn.core.region.plot.selling.settings;

import com.bof.barn.core.region.plot.PlotSetting;
import org.jetbrains.annotations.NotNull;

/**
 * Settings specific to {@link com.bof.barn.core.region.plot.selling.ContainerPlot}
 */
public abstract class ContainerPlotSetting extends PlotSetting {
    public ContainerPlotSetting(@NotNull String settingName, boolean toggled) {
        super(settingName, toggled);
    }
}
