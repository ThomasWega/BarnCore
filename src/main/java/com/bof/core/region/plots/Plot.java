package com.bof.core.region.plots;

import com.bof.core.region.plots.farm.FarmPlot;
import com.bof.core.region.BarnRegion;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public interface Plot {
    static Plot newPlot(@NotNull BarnRegion owningRegion, @NotNull PlotType type, @NotNull BoundingBox box, int id) {
        if (type == PlotType.FARM) {
            return new FarmPlot(owningRegion, box, id);
        }
        return null;
    }

    BoundingBox getBox();

    Set<Block> getBoxBlocks();

    PlotType getType();

    Hologram getHologram();

    void setHologram(@NotNull Hologram holo);

    int getId();

    Consumer<PlayerHologramInteractEvent> getHologramAction();

    Component getDisplayName();

    List<Component> getLore();

    boolean isAutoHarvest();
    BarnRegion getOwningRegion();
}
