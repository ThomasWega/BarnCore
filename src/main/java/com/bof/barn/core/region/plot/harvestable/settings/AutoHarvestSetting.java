package com.bof.barn.core.region.plot.harvestable.settings;

import lombok.Getter;

/**
 * Whether harvestables should be auto harvested every given period
 */
@Getter
public class AutoHarvestSetting extends HarvestablePlotSetting {
    private final long tickSpeed = 100;

    public AutoHarvestSetting() {
        super("Auto Harvest", false);
    }
}
