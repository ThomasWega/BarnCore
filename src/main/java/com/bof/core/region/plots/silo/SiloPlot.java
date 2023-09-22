package com.bof.core.region.plots.silo;

import com.bof.core.region.BarnRegion;
import com.bof.core.region.plots.Plot;
import com.bof.core.region.plots.PlotType;
import com.bof.core.region.plots.farm.CropType;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.HologramPool;
import com.github.unldenis.hologram.InteractiveHologramPool;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;

import java.util.Set;

@Data
public class SiloPlot extends Plot {
    private final BarnRegion owningRegion;
    private final PlotType type = PlotType.SILO;
    private final BoundingBox box;
    private final int id;
    private final Set<Block> boxBlocks;
    private Hologram hologram;
}
