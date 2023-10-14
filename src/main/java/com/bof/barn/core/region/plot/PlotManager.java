package com.bof.barn.core.region.plot;

import com.bof.barn.core.Core;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.event.RegionCreatedEvent;
import com.bof.barn.core.region.plot.event.PlotCreatedEvent;
import com.bof.barn.core.utils.BoxUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

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
    private final @NotNull Core plugin;

    @EventHandler
    private void onRegionCreate(RegionCreatedEvent event) {
        this.handlePlotsCreation(event.getRegion());
    }

    /**
     * Create a new plot and call appropriate events
     *
     * @param region Region that owns the plot
     */
    private void handlePlotsCreation(BarnRegion region) {
        Map<PlotType, Set<Plot>> plots = new HashMap<>();
        Arrays.stream(PlotType.values()).forEach(plotType -> plots.put(plotType, BoxUtils.identifyPlots(plotType, region.getBox()).entrySet().stream()
                .map(entry -> Plot.newPlot(plugin, plotType, region, entry.getValue(), Integer.parseInt(entry.getKey())))
                .peek(plot -> Bukkit.getPluginManager().callEvent(new PlotCreatedEvent(plot)))
                .collect(Collectors.toSet())
        ));
        region.setPlots(plots);
    }
}
