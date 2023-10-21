package com.bof.barn.core.region.plot.harvestable.tasks;


import com.bof.barn.core.region.plot.harvestable.AbstractHarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalPlot;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalType;
import com.bof.barn.core.region.plot.harvestable.farm.CropType;
import com.bof.barn.core.region.plot.harvestable.farm.FarmPlot;
import com.bof.barn.core.region.plot.harvestable.settings.impl.ReplantAllSetting;
import com.bof.barn.core.region.plot.task.PlotTask;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

/**
 * Handles automatic replanting of all the harvestables if they are harvested all at once
 *
 * @param <P> Harvestable plot
 */
@RequiredArgsConstructor
public class ReplantAllTask<P extends AbstractHarvestablePlot<?>> implements PlotTask {
    private final P plot;

    @Override
    public void run() {
        ReplantAllSetting setting = plot.getSetting(ReplantAllSetting.class);
        if (setting.isToggled() && setting.shouldRun()) {
            if (plot instanceof AnimalPlot animalPlot) {
                // save the current harvesting instance, as after delay it might be harvested already
                AnimalType currentlyHarvesting = animalPlot.getCurrentlyHarvesting();
                Bukkit.getScheduler().runTaskLater(animalPlot.getPlugin(), () -> animalPlot.changeType(currentlyHarvesting), 20L);
            } else if (plot instanceof FarmPlot farmPlot) {
                CropType currentlyHarvesting = farmPlot.getCurrentlyHarvesting();
                Bukkit.getScheduler().runTaskLater(farmPlot.getPlugin(), () -> farmPlot.changeType(currentlyHarvesting), 20L);
            }
        }
    }
}
