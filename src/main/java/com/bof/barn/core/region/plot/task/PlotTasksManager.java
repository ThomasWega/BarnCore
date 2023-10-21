package com.bof.barn.core.region.plot.task;

import com.bof.barn.core.Core;
import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.event.setting.PlotSettingLevelIncreaseEvent;
import com.bof.barn.core.region.plot.event.setting.PlotSettingToggleEvent;
import com.bof.barn.core.region.plot.harvestable.AbstractHarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.setting.AutoHarvestSetting;
import com.bof.barn.core.region.plot.harvestable.task.AutoHarvestTask;
import com.bof.barn.core.region.plot.setting.PlotSetting;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles scheduling and cancelling of {@link PlotTask}s
 */
@RequiredArgsConstructor
public class PlotTasksManager implements Listener {
    private final @NotNull Core plugin;
    @Getter
    private final Map<PlotType, Map<Integer, Map<Class<? extends PlotTask>, BukkitTask>>> runningTasks = new HashMap<>();

    @EventHandler
    private void onPlotSetting(PlotSettingToggleEvent event) {
        PlotSetting setting = event.getPlotSetting();
        AbstractPlot plot = event.getPlot();
        if (setting instanceof AutoHarvestSetting) {
            this.handleAutoHarvestTask(((AbstractHarvestablePlot<?>) plot), ((AutoHarvestSetting) setting));
        }
    }

    /**
     * When the level increases, we need to rerun the task to reset the timer period
     */
    @EventHandler
    private void onLevelIncrease(PlotSettingLevelIncreaseEvent event) {
        if (event.getPlotSetting() instanceof AutoHarvestSetting autoHarvestSetting) {
            AbstractPlot plot = event.getPlot();
            this.cancelTask(AutoHarvestTask.class, plot);
            this.handleAutoHarvestTask((AbstractHarvestablePlot<?>) plot, autoHarvestSetting);
        }
    }

    /**
     * If the setting gets toggled, start the task and save it to the map.
     * If the settings gets untoggled, cancel the task
     *
     * @param plot    Plot to start or cancel the task for
     * @param setting Setting instance
     * @see #cancelTask(Class, AbstractPlot)
     */
    private void handleAutoHarvestTask(AbstractHarvestablePlot<?> plot, AutoHarvestSetting setting) {
        if (setting.isToggled()) {
            Map<Integer, Map<Class<? extends PlotTask>, BukkitTask>> typeTasks = this.runningTasks.computeIfAbsent(plot.getType(), k -> new HashMap<>());
            Map<Class<? extends PlotTask>, BukkitTask> plotTasks = typeTasks.computeIfAbsent(plot.getId(), k -> new HashMap<>());
            BukkitTask task = Bukkit.getScheduler().runTaskTimer(this.plugin, new AutoHarvestTask<>((AbstractHarvestablePlot<?>) plot), 1L, setting.getTickSpeed());
            plotTasks.put(AutoHarvestTask.class, task);
        } else {
            this.cancelTask(AutoHarvestTask.class, plot);
        }
    }

    /**
     * Cancel the task and remove it from the map
     *
     * @param taskClazz Class of the task
     * @param plot      Plot the task belongs to
     */
    private void cancelTask(Class<? extends PlotTask> taskClazz, AbstractPlot plot) {
        Map<Integer, Map<Class<? extends PlotTask>, BukkitTask>> typeTasks = this.runningTasks.get(plot.getType());
        if (typeTasks == null) return;

        Map<Class<? extends PlotTask>, BukkitTask> plotTasks = typeTasks.get(plot.getId());
        if (plotTasks == null) return;

        BukkitTask task = plotTasks.remove(taskClazz);
        if (task == null) return;

        task.cancel();
    }
}
