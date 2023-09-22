package com.bof.core.region.plots.farm;

import com.bof.core.region.plots.Plot;
import com.bof.core.region.plots.PlotType;
import com.bof.core.region.plots.farm.menu.FarmPlotMainMenu;
import com.bof.core.region.BarnRegion;
import com.bof.core.utils.BoxUtils;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import com.github.unldenis.hologram.line.BlockLine;
import com.github.unldenis.hologram.line.TextLine;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Data
public class FarmPlot implements Plot {
    private final BarnRegion owningRegion;
    private final PlotType type = PlotType.FARM;
    private final BoundingBox box;
    private final int id;
    private final Set<Block> boxBlocks;
    private CropType currentCrop = CropType.NONE;
    private Hologram hologram;
    private boolean autoHarvest = false;

    public FarmPlot(@NotNull BarnRegion owningRegion, @NotNull BoundingBox box, int id) {
        this.owningRegion = owningRegion;
        this.box = box;
        this.boxBlocks = BoxUtils.getBlocksInBox(box, true);
        this.id = id;
    }

    public void setCurrentCrop(@NotNull CropType type) {
        this.currentCrop = type;
        this.updateHologram();
    }

    public void changeCrops(CropType type) {
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

    public boolean harvestCrops(@NotNull Player player) {
        // there is nothing to harvest
        if (currentCrop == CropType.NONE || !isCropPresent()) {
            player.sendMessage("TO ADD - No crop is planted");
            return false;
        }

        int count = 0;
        for (Block block : boxBlocks) {
            if (this.getOwningRegion().isCropsInvFull()) {
                player.sendMessage("TO ADD - Crops inventory is full");
                break;
            }
            if (CropType.getByMaterial(block.getType())
                    .filter(cropType -> cropType != CropType.NONE)
                    .isPresent()) {
                this.getOwningRegion().addCrops(new ItemStack(block.getType()));
                block.setType(Material.AIR);
                count++;
            }
        }

        if (count > 0) {
            player.sendMessage("TO ADD - Harvested " + count + " crops");
        }

        return true;
    }

    public int getRemainingCrops() {
        return (int) boxBlocks.stream()
                .filter(block -> block.getType() != Material.AIR)
                .filter(block -> CropType.getByMaterial(block.getType()).isPresent())
                .count();
    }

    private boolean isCropPresent() {
        Set<Material> boxBlocksMat = boxBlocks.stream()
                .map(Block::getType)
                .collect(Collectors.toSet());

        return CropType.getMaterials().stream()
                .anyMatch(boxBlocksMat::contains);
    }

    public void updateHologram() {
        hologram.getLines().stream()
                .filter(iLine -> iLine instanceof BlockLine)
                .map(iLine -> ((BlockLine) iLine))
                .forEach(blockLine -> {
                    blockLine.setObj(new ItemStack(currentCrop.getItemMaterial()));
                    blockLine.update(owningRegion.getAllPlayers());
                });

        hologram.getLines().stream()
                .filter(iLine -> iLine instanceof TextLine)
                .map(iLine -> ((TextLine) iLine))
                .forEach(textLine -> textLine.update(owningRegion.getAllPlayers()));
    }

    public void setAutoHarvest(@NotNull Player player, boolean autoHarvest) {
        if (!this.getOwningRegion().hasFreeAutoHarvestSlots() && autoHarvest) {
            player.sendMessage("TO ADD - No free AutoHarvest slots");
            return;
        }

        this.autoHarvest = autoHarvest;
        this.updateHologram();
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

    @Override
    public Component getDisplayName() {
        return MiniMessage.miniMessage().deserialize("<b><color:#FC7B03>Farm Plot " + id + "</color></b>");
    }

    @Override
    public List<Component> getLore() {
        String autoHarvestStatus = this.autoHarvest ? "<green>ON</green>" : "<red>OFF</red>";
        return List.of(
                MiniMessage.miniMessage().deserialize("<color:#FCDB03>Upgrades: <red>❌</red></color>"),
                MiniMessage.miniMessage().deserialize("<color:#D4F542>Enchanted Rain: <red>❌</red></color>"),
                MiniMessage.miniMessage().deserialize("<color:#2B84FF>Auto Harvest: %barn_plot_farm_colored_status_autoharvest_" + this.id + "%</color>")
        );
    }
}
