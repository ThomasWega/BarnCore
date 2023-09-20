package com.bof.core.plots;

import com.bof.core.plots.crops.FarmPlot;
import com.bof.core.region.BarnRegion;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.NotNull;

public interface Plot {
    BoundingBox getBox();
    PlotType getType();
    Hologram getHologram();
    int getId();
    void setHologram(@NotNull Hologram holo);
    Consumer<PlayerHologramInteractEvent> getHologramAction();

    static Plot newPlot(@NotNull BarnRegion owningRegion, @NotNull PlotType type, @NotNull BoundingBox box, int id) {
        if (type == PlotType.FARM) {
            return new FarmPlot(owningRegion, box, id);
        }
        return null;
    }
}
