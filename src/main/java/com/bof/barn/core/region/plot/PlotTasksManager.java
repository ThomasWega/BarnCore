package com.bof.barn.core.region.plot;

import com.bof.barn.core.Core;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.event.RegionAssignedEvent;
import com.bof.barn.core.region.plot.harvestable.AutoHarvestTask;
import com.bof.barn.core.region.plot.harvestable.HarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.settings.AutoHarvestSetting;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles creation of the {@link Plot} instance
 */
@RequiredArgsConstructor
public class PlotTasksManager implements Listener {
    private final @NotNull Core plugin;
    private final @NotNull Set<PlotTask> tasks = new HashSet<>();

    @EventHandler
    private void onRegionAssign(RegionAssignedEvent event) {
        this.handleTasksStart(event.getRegion());
    }

    /**
     * Start all tasks including plot tasks
     *
     * @param region Region to start tasks for
     */
    private void handleTasksStart(BarnRegion region) {
        region.getPlots().forEach((plotType, plots) -> {
            switch (plotType) {
                case FARM, ANIMAL:
                    plots.forEach(plot -> {
                        Bukkit.getScheduler().runTaskTimer(plugin, new AutoHarvestTask<>((HarvestablePlot<?>) plot), 1L, plot.getSetting(AutoHarvestSetting.class).getTickSpeed());
                    });
                case BARN, SILO:
            }
        });
    }
}
