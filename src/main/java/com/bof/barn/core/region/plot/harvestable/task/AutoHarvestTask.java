package com.bof.barn.core.region.plot.harvestable.task;


import com.bof.barn.core.region.plot.task.PlotTask;
import com.bof.barn.core.region.plot.harvestable.HarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.farm.CropType;
import com.bof.barn.core.region.plot.harvestable.farm.FarmPlot;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class AutoHarvestTask<T extends HarvestablePlot<?>> implements PlotTask {
    private final T plot;

    @Override
    public void run() {
        FarmPlot plot = (FarmPlot) this.plot;
        this.getRandomBlockAndCropType(plot.getRemainingHarvestables()).ifPresent(entry -> {
            // technically the player doesn't matter for now, as no message will be sent, do take a caution with this though! (13.10.2023)
            plot.handleCropBreak(plot.getOwningRegion().getOwner(), false, entry.getKey());
        });
    }

    private @NotNull Optional<Map.Entry<Block, CropType>> getRandomBlockAndCropType(Map<Block, CropType> harvestables) {
        return harvestables.entrySet()
                .stream()
                .findAny();
    }
}
