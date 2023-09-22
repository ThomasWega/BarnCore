package com.bof.core.region.plots.silo;

import com.bof.core.region.BarnRegion;
import com.bof.core.region.plots.Plot;
import com.bof.core.region.plots.PlotType;
import com.bof.core.utils.BoxUtils;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import lombok.Data;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Data
public class SiloPlot implements Plot {
    private final BarnRegion owningRegion;
    private final PlotType type = PlotType.SILO;
    private final BoundingBox box;
    private final int id;
    private final Set<Block> boxBlocks;
    private Hologram hologram;

    public SiloPlot(BarnRegion owningRegion, BoundingBox box, int id) {
        this.owningRegion = owningRegion;
        this.box = box;
        this.id = id;
        this.boxBlocks = BoxUtils.getBlocksInBox(box, true);
    }

    @Override
    public Consumer<PlayerHologramInteractEvent> getHologramAction() {
        return null;
    }

    @Override
    public Component getDisplayName() {
        return null;
    }

    @Override
    public List<Component> getLore() {
        return null;
    }
}
