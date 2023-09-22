package com.bof.core.region.plots;

import com.bof.core.region.plots.farm.FarmPlot;
import com.bof.core.region.BarnRegion;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public interface HarvestablePlot extends Plot {
    static HarvestablePlot newPlot(@NotNull BarnRegion owningRegion, @NotNull PlotType type, @NotNull BoundingBox box, int id) {
        if (type == PlotType.FARM) {
            return new FarmPlot(owningRegion, box, id);
        }
        return null;
    }

    boolean isAutoHarvest();
}
