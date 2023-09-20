package com.bof.core.plots.crops;

import com.bof.core.plots.Plot;
import com.bof.core.plots.PlotType;
import com.bof.core.utils.BoxUtils;
import com.github.unldenis.hologram.Hologram;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Data
public class FarmPlot implements Plot {
    private final PlotType type = PlotType.FARM;
    private final BoundingBox box;
    private final Set<Block> boxBlocks;
    private CropsType currentCrop = CropsType.NONE;
    private Hologram hologram;

    public FarmPlot(@NotNull BoundingBox box) {
        this.box = box;
        this.boxBlocks = BoxUtils.getBlocksInBox(box, true);
    }

    public void changeCrops(CropsType type) {
        boxBlocks.forEach(block -> {
                    if (block.getRelative(BlockFace.DOWN).getType() == Material.FARMLAND) {
                        block.setType(type.getMaterial());
                        if (block.getBlockData() instanceof Ageable ageable) {
                            ageable.setAge(ageable.getMaximumAge());
                            block.setBlockData(ageable);
                        }
                    }
                });
        this.setCurrentCrop(type);
    }
}
