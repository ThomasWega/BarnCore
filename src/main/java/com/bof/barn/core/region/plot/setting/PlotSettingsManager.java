package com.bof.barn.core.region.plot.setting;

import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.event.PlotCreatedEvent;
import com.bof.barn.core.region.plot.harvestable.settings.AutoHarvestSetting;
import com.bof.barn.core.region.plot.harvestable.settings.AutoStoreSetting;
import com.bof.barn.core.region.plot.harvestable.settings.ReplantAllSetting;
import com.bof.barn.core.region.plot.container.settings.AutoSellSetting;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

/**
 * Handles loading the setting of the {@link AbstractPlot} instance
 */
@RequiredArgsConstructor
public class PlotSettingsManager implements Listener {
    @EventHandler
    private void onRegionCreate(PlotCreatedEvent event) {
        this.handleSettingsSet(event.getPlot());
    }

    /**
     * Put all settings into the plot
     *
     * @param plot Plot to place settings into
     */
    private void handleSettingsSet(AbstractPlot plot) {
        switch (plot.getType()) {
            case FARM, ANIMAL:
                plot.getSettings().putAll(Map.of(
                        AutoStoreSetting.class, new AutoStoreSetting(),
                        ReplantAllSetting.class, new ReplantAllSetting(),
                        AutoHarvestSetting.class, new AutoHarvestSetting()
                ));
                break;
            case BARN, SILO:
                plot.getSettings().put(
                        AutoSellSetting.class, new AutoSellSetting()
                );
                break;
        }
    }
}
