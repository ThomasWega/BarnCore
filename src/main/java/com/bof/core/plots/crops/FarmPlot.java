package com.bof.core.plots.crops;

import com.bof.core.plots.Plot;
import com.bof.core.plots.PlotType;
import com.bof.core.plots.crops.menu.FarmPlotMainMenu;
import com.bof.core.region.BarnRegion;
import com.bof.core.utils.BoxUtils;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import com.github.unldenis.hologram.line.BlockLine;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLOutput;
import java.util.Set;

@Data
public class FarmPlot implements Plot {
    private final BarnRegion owningRegion;
    private final PlotType type = PlotType.FARM;
    private final BoundingBox box;
    private final int id;
    private final Set<Block> boxBlocks;
    @Setter(AccessLevel.PRIVATE)
    private CropsType currentCrop = CropsType.NONE;
    private Hologram hologram;

    public FarmPlot(@NotNull BarnRegion owningRegion, @NotNull BoundingBox box, int id) {
        this.owningRegion = owningRegion;
        this.box = box;
        this.boxBlocks = BoxUtils.getBlocksInBox(box, true);
        this.id = id;
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
        this.updateHologram();
    }

    public void updateHologram() {
        hologram.getLines().stream()
                .filter(iLine -> iLine instanceof BlockLine)
                .map(iLine -> ((BlockLine) iLine))
                .forEach(blockLine -> {
                    blockLine.setObj(new ItemStack(currentCrop.getDisplayMaterial()));
                    blockLine.update(owningRegion.getAllPlayers());
                });
    }

    @Override
    public Consumer<PlayerHologramInteractEvent> getHologramAction() {
        return event -> {
            if (event.getHologram().equals(this.hologram)) {
                Player player = event.getPlayer();
                new FarmPlotMainMenu(this).show(player);
            }
        };
    }
}
