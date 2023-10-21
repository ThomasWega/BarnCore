package com.bof.barn.core.region.plot.harvestable.tasks;


import com.bof.barn.core.region.plot.harvestable.AbstractHarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalPlot;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalType;
import com.bof.barn.core.region.plot.harvestable.farm.CropType;
import com.bof.barn.core.region.plot.harvestable.farm.FarmPlot;
import com.bof.barn.core.region.plot.task.PlotTask;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

/**
 * Handles the automatic harvesting of the harvestables on the plot
 *
 * @param <T> Harvestable plot
 */
@RequiredArgsConstructor
public class AutoHarvestTask<T extends AbstractHarvestablePlot<?>> implements PlotTask {
    private final T plot;

    @Override
    public void run() {
        if (this.plot instanceof FarmPlot farmPlot) {
            this.getRandomCropWithType(farmPlot.getRemainingHarvestables()).ifPresent(entry -> {
                // technically the player doesn't matter for now, as no message will be sent, do take a caution with this though! (13.10.2023)
                farmPlot.handleCropBreak(farmPlot.getOwningRegion().getOwner(), false, entry.getKey());
            });
            return;
        }

        if (this.plot instanceof AnimalPlot animalPlot) {
            this.getRandomAnimalWithType(animalPlot.getRemainingHarvestables()).ifPresent(entry -> {
                // technically the player doesn't matter for now, as no message will be sent, do take a caution with this though! (13.10.2023)
                animalPlot.handleAnimalKill(animalPlot.getOwningRegion().getOwner(), false, entry.getKey());
            });
            return;
        }
    }

    private @NotNull Optional<Map.Entry<Block, CropType>> getRandomCropWithType(Map<Block, CropType> harvestables) {
        return harvestables.entrySet()
                .stream()
                .findAny();
    }

    private @NotNull Optional<Map.Entry<LivingEntity, AnimalType>> getRandomAnimalWithType(Map<LivingEntity, AnimalType> harvestables) {
        return harvestables.entrySet()
                .stream()
                .findAny();
    }
}
