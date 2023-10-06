package com.bof.barn.core.region.plot;

import com.bof.barn.core.region.plot.event.PlotCreatedEvent;
import com.bof.barn.core.region.plot.harvestable.settings.AutoStoreSetting;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

/**
 * Handles creation of the {@link Plot} instance
 */
@RequiredArgsConstructor
public class PlotSettingsManager implements Listener {
    @EventHandler
    private void onRegionCreate(PlotCreatedEvent event) {
        this.handleSettingsSet(event.getPlot());
    }

    /**
     * Put all settings into the plot
     * @param plot Plot to place settings into
     */
    private void handleSettingsSet(Plot plot) {
        plot.getSettings().putAll(Map.of(
                AutoStoreSetting.class, new AutoStoreSetting()
        ));
    }
}
