package com.bof.core.region.plot;

import com.bof.core.region.BarnRegion;
import com.bof.core.region.event.RegionCreatedEvent;
import com.bof.core.region.plot.event.PlotCreatedEvent;
import com.bof.core.utils.BoxUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles creation of the {@link Plot} instance
 */
@RequiredArgsConstructor
public class PlotManager implements Listener {
    @EventHandler
    private void onRegionCreate(RegionCreatedEvent event) {
        this.handlePlotsCreation(event.getRegion());
    }

    private void handlePlotsCreation(BarnRegion region) {
        Map<PlotType, Set<Plot>> plots = new HashMap<>();
        Arrays.stream(PlotType.values()).forEach(plotType -> plots.put(plotType, BoxUtils.identifyPlots(plotType, region.getBox()).entrySet().stream()
                .map(entry -> Plot.newPlot(plotType, region, entry.getValue(), Integer.parseInt(entry.getKey())))
                .peek(plot -> Bukkit.getPluginManager().callEvent(new PlotCreatedEvent(plot)))
                .collect(Collectors.toSet())
        ));
        region.setPlots(plots);
    }
}
