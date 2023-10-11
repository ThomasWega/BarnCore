package com.bof.barn.core.region.plot.selling.settings;

/**
 * Whether harvestables put into the container should be automatically sold.
 * Will sell only harvestables put into the silo while the setting is true.
 * Old harvestables won't be sold
 */
public class AutoSellSetting extends ContainerPlotSetting {
    public AutoSellSetting() {
        super("Auto Sell", false);
    }
}
