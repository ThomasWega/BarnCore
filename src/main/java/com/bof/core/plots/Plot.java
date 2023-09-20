package com.bof.core.plots;

import com.bof.core.plots.crops.FarmPlot;
import com.github.unldenis.hologram.Hologram;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public interface Plot {
    BoundingBox getBox();
    PlotType getType();
    Hologram getHologram();
    void setHologram(@NotNull Hologram holo);

    static Plot newPlot(@NotNull PlotType type, @NotNull BoundingBox box) {
        if (type == PlotType.FARM) {
            return new FarmPlot(box);
        }
        return null;
    }
}
