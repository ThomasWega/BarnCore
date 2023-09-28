package com.bof.core.region.plot;

import com.bof.core.region.BarnRegion;
import com.bof.core.region.plot.harvestable.animal.AnimalPlot;
import com.bof.core.region.plot.selling.barn.BarnPlot;
import com.bof.core.region.plot.harvestable.farm.FarmPlot;
import com.bof.core.region.plot.selling.silo.SiloPlot;
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
    static Plot newPlot(@NotNull PlotType type, @NotNull BarnRegion owningRegion, @NotNull BoundingBox box, int id) {
        switch (type) {
            case FARM -> {
                return new FarmPlot(owningRegion, box, id);
            }
            case SILO -> {
                return new SiloPlot(owningRegion, box, id);
            }
            case ANIMAL -> {
                return new AnimalPlot(owningRegion, box, id);
            }
            case BARN -> {
                return new BarnPlot(owningRegion, box, id);
            }
        }
        return null;
    }

    PlotType getType();

    Hologram getHologram();

    void setHologram(@NotNull Hologram holo);

    int getId();

    Consumer<PlayerHologramInteractEvent> getHologramAction();

    Component getDisplayName();

    List<Component> getLore();
    BarnRegion getOwningRegion();

    BoundingBox getBox();

    Set<Block> getBoxBlocks();

    void updateHologram();
}
