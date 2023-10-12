package com.bof.barn.core.region.plot.harvestable;


import com.bof.barn.core.region.plot.PlotTask;
import com.bof.barn.core.region.plot.harvestable.farm.CropType;
import com.bof.barn.core.region.plot.harvestable.farm.FarmPlot;
import com.bof.barn.core.region.plot.harvestable.settings.AutoHarvestSetting;
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
        if (plot.isSetting(AutoHarvestSetting.class, false)) return;
        if (!plot.isHarvestablePresent()) return;

        FarmPlot plot = (FarmPlot) this.plot;
        this.getRandomBlockAndCropType(plot.getRemainingHarvestables()).ifPresent(entry -> {
            // technically the player doesn't matter, as no message will be sent, do take a caution with this though!
            plot.handleCropBreak(plot.getOwningRegion().getOwner(), false, entry.getKey());
        });
    }

    private @NotNull Optional<Map.Entry<Block, CropType>> getRandomBlockAndCropType(Map<Block, CropType> harvestables) {
        return harvestables.entrySet()
                .stream()
                .findAny();
    }
}
