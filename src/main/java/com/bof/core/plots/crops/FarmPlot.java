package com.bof.core.plots.crops;

import com.bof.core.plots.Plot;
import com.bof.core.plots.PlotType;
import com.bof.core.utils.BoxUtils;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.util.BoundingBox;

@Data
public class FarmPlot implements Plot {
    private final BoundingBox box;

    @Override
    public BoundingBox getBox() {
        return box;
    }

    @Override
    public PlotType getType() {
        return PlotType.FARM;
    }

    public void changeCrops(CropsType type) {
        BoxUtils.getBlocksInBox(box, true)
                .forEach(block -> {
                    if (block.getState() instanceof Sign sign) {
                        sign.getBlock().getRelative(BlockFace.DOWN).setType(Material.FARMLAND);
                        Farmland farmland = ((Farmland) block.getRelative(BlockFace.DOWN).getBlockData());
                        farmland.setMoisture(farmland.getMaximumMoisture());
                    }

                    if (block.getRelative(BlockFace.DOWN).getType() == Material.FARMLAND) {
                        block.setType(type.getMaterial());
                        Ageable ageable = ((Ageable) block.getBlockData());
                        ageable.setAge(ageable.getMaximumAge());
                    }
                });
    }
}
