package com.bof.core.region.plots.farm;

import com.bof.core.region.BarnRegion;
import com.bof.core.region.plots.HarvestablePlot;
import com.bof.core.region.plots.PlotType;
import com.bof.core.region.plots.farm.menu.FarmPlotMainMenu;
import com.bof.core.region.plots.silo.SiloPlot;
import com.bof.core.utils.BoxUtils;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import com.github.unldenis.hologram.line.BlockLine;
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

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Data
public class FarmPlot implements HarvestablePlot {
    private final BarnRegion owningRegion;
    private final PlotType type = PlotType.FARM;
    private final BoundingBox box;
    private final int id;
    private final Set<Block> boxBlocks;
    private CropType currentCrop = CropType.NONE;
    private Hologram hologram;
    private boolean autoStore = false;

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

    public int harvestCrops(@NotNull Player player) {
        return this.handleCropBreak(player, this.boxBlocks);
    }

    public int handleCropBreak(@NotNull Player player, @NotNull Block... blocks) {
        return this.handleCropBreak(player, Arrays.asList(blocks));
    }
    
    public int handleCropBreak(@NotNull Player player, @NotNull Collection<Block> blocks) {
        int count = 0;

        // there is nothing to harvest
        if (currentCrop == CropType.NONE || !isCropPresent()) {
            return count;
        }

        for (Block block : blocks) {
            if (CropType.getByMaterial(block.getType())
                    .filter(cropType -> cropType != CropType.NONE)
                    .isPresent()) {

                ItemStack item = new ItemStack(block.getType());
                if (this.isAutoStore()) {
                    // first tries putting into silo, then tries inventory, if both fails, returns list of items which failed
                    if (!this.addCropsToSilo(player, item).isEmpty()) {
                        player.sendMessage("TO ADD - All silos are full. Putting the items to inventory");
                        if (!this.addCropsToInventory(player, item).isEmpty()) {
                            player.sendMessage("TO ADD - Crops inventory is full 1");
                        }
                        break;
                    }
                } else {
                    if (!this.addCropsToInventory(player, item).isEmpty()) {
                        player.sendMessage("TO ADD - Crops inventory is full 2");
                        break;
                    }
                }

                block.setType(Material.AIR);
                count++;
            }
        }

        // everything was harvested
        if (this.getRemainingCrops() == 0) {
            this.setCurrentCrop(CropType.NONE);
        }

        return count;
    }

    public @NotNull List<ItemStack> addCropsToInventory(@NotNull Player player, @NotNull ItemStack... items) {
        return this.addCropsToInventory(player, Arrays.asList(items));
    }

    public @NotNull List<ItemStack> addCropsToInventory(@NotNull Player player, @NotNull Collection<ItemStack> crops) {
        List<ItemStack> unAdded = new ArrayList<>();

        for (ItemStack item : crops) {
            if (!this.getOwningRegion().addCropsToInventory(item).isEmpty()) {
                unAdded.add(item);
            }
        }

        return unAdded;
    }

    public @NotNull List<ItemStack> addCropsToSilo(@NotNull Player player, @NotNull ItemStack... crops) {
        return this.addCropsToSilo(player, Arrays.asList(crops));
    }

    public @NotNull List<ItemStack> addCropsToSilo(@NotNull Player player, @NotNull Collection<ItemStack> items) {
        List<ItemStack> unAdded = new ArrayList<>();
        Optional<SiloPlot> optSilo = this.getOwningRegion().getFreeSilo();

        for (ItemStack item : items) {
            // no free silo is available, so put all remaining items as unAdded
            if (optSilo.isEmpty()) {
                unAdded.add(item);
                continue;
            }

            SiloPlot silo = optSilo.get();
            // silo is full, so try getting a new free silo
            if (!silo.addCropsToSilo(item).isEmpty()) {
                optSilo = this.getOwningRegion().getFreeSilo();
            }
        }

        return unAdded;
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
        this.hologram.getLines().stream()
                .filter(iLine -> iLine instanceof BlockLine)
                .map(iLine -> ((BlockLine) iLine))
                .forEach(blockLine -> blockLine.setObj(new ItemStack(currentCrop.getItemMaterial())));

        this.hologram.getLines().forEach(iLine -> iLine.update(this.owningRegion.getAllPlayers()));
    }

    public void setAutoStore(@NotNull Player player, boolean autoStore) {
        if (!this.getOwningRegion().hasFreeAutoStoreSlots() && autoStore) {
            player.sendMessage("TO ADD - No free AutoStore slots");
            return;
        }

        this.autoStore = autoStore;
        this.updateHologram();
    }

    @Override
    public Consumer<PlayerHologramInteractEvent> getHologramAction() {
        return event -> {
            if (event.getHologram().equals(this.hologram)) {
                new FarmPlotMainMenu(this).show(event.getPlayer());
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return MiniMessage.miniMessage().deserialize("<b><color:#FC7B03>Farm Plot " + id + "</color></b>");
    }

    @Override
    public List<Component> getLore() {
        return List.of(
                MiniMessage.miniMessage().deserialize("<color:#FCDB03>Upgrades: <red>OFF</red></color>"),
                MiniMessage.miniMessage().deserialize("<color:#D4F542>Enchanted Rain: <red>OFF</red></color>"),
                MiniMessage.miniMessage().deserialize("<color:#2B84FF>Auto Store: %barn_plot_farm_colored_status_autostore_" + this.id + "%</color>")
        );
    }
}
