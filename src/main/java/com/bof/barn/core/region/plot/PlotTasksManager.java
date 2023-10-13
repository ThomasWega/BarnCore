package com.bof.barn.core.region.plot;

import com.bof.barn.core.Core;
import com.bof.barn.core.region.plot.event.PlotSettingEvent;
import com.bof.barn.core.region.plot.harvestable.HarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.setting.AutoHarvestSetting;
import com.bof.barn.core.region.plot.harvestable.task.AutoHarvestTask;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles starting and stopping of the {@link PlotTask}
 */
@RequiredArgsConstructor
public class PlotTasksManager implements Listener {
    private final @NotNull Core plugin;
    private final Map<PlotType, Map<Integer, Map<Class<? extends PlotTask>, BukkitTask>>> runningTasks = new HashMap<>();

    @EventHandler
    private void onPlotSetting(PlotSettingEvent event) {
        PlotSetting setting = event.getPlotSetting();
        Plot plot = event.getPlot();
        if (setting instanceof AutoHarvestSetting) {
            this.handleAutoHarvestTask(((HarvestablePlot<?>) plot), ((AutoHarvestSetting) setting));
        }
    }

    private void handleAutoHarvestTask(HarvestablePlot<?> plot, AutoHarvestSetting setting) {
        if (setting.isToggled()) {
            Map<Integer, Map<Class<? extends PlotTask>, BukkitTask>> typeTasks = this.runningTasks.computeIfAbsent(plot.getType(), k -> new HashMap<>());
            Map<Class<? extends PlotTask>, BukkitTask> plotTasks = typeTasks.computeIfAbsent(plot.getId(), k -> new HashMap<>());
            BukkitTask task = Bukkit.getScheduler().runTaskTimer(this.plugin, new AutoHarvestTask<>((HarvestablePlot<?>) plot), 1L, setting.getTickSpeed());
            plotTasks.put(AutoHarvestTask.class, task);
        } else {
            cancelTask(AutoHarvestTask.class, plot);
        }
    }

    private void cancelTask(Class<? extends PlotTask> taskClazz, Plot plot) {
        Map<Integer, Map<Class<? extends PlotTask>, BukkitTask>> typeTasks = this.runningTasks.get(plot.getType());
        if (typeTasks == null) return;

        Map<Class<? extends PlotTask>, BukkitTask> plotTasks = typeTasks.get(plot.getId());
        if (plotTasks == null) return;

        BukkitTask task = plotTasks.remove(taskClazz);
        if (task == null) return;

        task.cancel();
    }
}
