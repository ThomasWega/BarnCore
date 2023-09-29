package com.bof.core.region.plot;

import com.bof.core.region.BarnRegion;
import com.bof.core.region.plot.harvestable.animal.AnimalPlot;
import com.bof.core.region.plot.harvestable.farm.FarmPlot;
import com.bof.core.region.plot.selling.barn.BarnPlot;
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

    /**
     * @param type         Type of the plot
     * @param owningRegion Region that owns the plot
     * @param box          Box the plot is in
     * @param id           ID of the plot
     * @return new Plot instance depending on the type
     */
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

    /**
     * Determines what should happen when the plot's hologram is clicked
     */
    Consumer<PlayerHologramInteractEvent> getHologramAction();


    /**
     * @return Display name that should be used in items and holograms
     * @see #getLore()
     */
    Component getDisplayName();

    /**
     * @return Lore that should be used in items and holograms
     * @see #getDisplayName()
     */
    List<Component> getLore();

    /**
     * @return Region that owns this plot
     */
    BarnRegion getOwningRegion();

    BoundingBox getBox();

    /**
     * @return All blocks the {@link BoundingBox} contains
     */
    Set<Block> getBoxBlocks();

    void updateHologram();
}
