package com.bof.core.plots.crops;

import com.bof.core.plots.Plot;
import com.bof.core.utils.BoxUtils;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.util.BoundingBox;

@Data
public class FarmPlot implements Plot {
    private final BoundingBox box;

    @Override
    public BoundingBox getBox() {
        return box;
    }

    public void changeCrops(CropsType type) {
        BoxUtils.getBlocksInBox(box).stream()
                .filter(block -> block.getType() == Material.FARMLAND)
                .forEach(block -> {
                    Block cropBlock = block.getRelative(BlockFace.UP);
                    cropBlock.setType(type.getMaterial());
                    Ageable ageable = ((Ageable) cropBlock.getBlockData());
                    ageable.setAge(ageable.getMaximumAge());
                    cropBlock.setBlockData(ageable);
                });
    }
}
