package com.bof.core.region.plot.harvestable.farm;

import com.bof.core.region.BarnRegion;
import com.bof.core.region.plot.PlotType;
import com.bof.core.region.plot.harvestable.HarvestablePlot;
import com.bof.core.region.plot.harvestable.farm.menu.FarmPlotMainMenu;
import com.bof.core.region.plot.selling.silo.SiloPlot;
import com.bof.core.utils.BoxUtils;
import com.bof.toolkit.utils.ColorUtils;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import com.github.unldenis.hologram.line.BlockLine;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
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
public class FarmPlot implements HarvestablePlot<CropType> {
    private final BarnRegion owningRegion;
    private final PlotType type = PlotType.FARM;
    private final BoundingBox box;
    private final int id;
    private final Set<Block> boxBlocks;
    private CropType currentlyHarvesting = CropType.NONE;
    private Hologram hologram;
    @Setter(AccessLevel.NONE)
    private boolean autoStore = false;

    public FarmPlot(@NotNull BarnRegion owningRegion, @NotNull BoundingBox box, int id) {
        this.owningRegion = owningRegion;
        this.box = box;
        this.boxBlocks = BoxUtils.getBlocksInBox(box, true);
        this.id = id;
    }

    @Override
    public void changeType(@NotNull CropType type) {
        this.boxBlocks.forEach(block -> {
            if (block.getRelative(BlockFace.DOWN).getType() == Material.FARMLAND) {
                block.setType(type.getMaterial());
                if (block.getBlockData() instanceof Ageable ageable) {
                    ageable.setAge(ageable.getMaximumAge());
                    block.setBlockData(ageable);
                }
            }
        });
        this.setCurrentlyHarvesting(type);
        this.updateHologram();
    }

    @Override
    public int harvest(@NotNull Player player) {
        return this.handleCropBreak(player, this.boxBlocks);
    }

    /**
     * Handles the breakage of block.
     * Changes the {@link #currentlyHarvesting} type, puts the items into {@link SiloPlot} on {@link #autoStore},
     * if the silo is full, tries putting it into {@link BarnRegion#getCropsInventory()}.
     * If that is full as well, sends a message to the player
     *
     * @param player Player that broke the crop
     * @param blocks Blocks that were broken
     * @return amount of crops that were successfully broken
     */
    public int handleCropBreak(@NotNull Player player, @NotNull Block... blocks) {
        return this.handleCropBreak(player, Arrays.asList(blocks));
    }

    /**
     * Handles the breakage of block.
     * Changes the {@link #currentlyHarvesting} type, puts the items into {@link SiloPlot} on {@link #autoStore},
     * if the silo is full, tries putting it into {@link BarnRegion#getCropsInventory()}.
     * If that is full as well, sends a message to the player
     *
     * @param player Player that broke the crop
     * @param blocks Blocks that were broken
     * @return amount of crops that were successfully broken
     */
    public int handleCropBreak(@NotNull Player player, @NotNull Collection<Block> blocks) {
        int count = 0;

        // there is nothing to harvest
        if (this.currentlyHarvesting == CropType.NONE || !isHarvestablePresent()) {
            return count;
        }

        for (Block block : blocks) {
            if (CropType.getByMaterial(block.getType())
                    .filter(cropType -> cropType != CropType.NONE)
                    .isPresent()) {

                ItemStack item = new ItemStack(block.getType());
                if (this.isAutoStore()) {
                    // first tries putting into silo, then tries inventory, if both fails, returns list of items which failed
                    if (!this.addToContainer(item).isEmpty()) {
                        player.sendMessage("TO ADD - All silos are full. Putting the items to inventory");
                        if (!this.addToInventory(item).isEmpty()) {
                            player.sendMessage("TO ADD - Crops inventory is full 1");
                        }
                        break;
                    }
                } else {
                    if (!this.addToInventory(item).isEmpty()) {
                        player.sendMessage("TO ADD - Crops inventory is full 2");
                        break;
                    }
                }

                block.setType(Material.AIR);
                count++;
            }
        }

        // everything was harvested
        if (this.getRemainingHarvestables() == 0) {
            this.setCurrentlyHarvesting(CropType.NONE);
        }

        return count;
    }

    @Override
    public @NotNull List<ItemStack> addToInventory(@NotNull ItemStack... items) {
        return this.addToInventory(Arrays.asList(items));
    }

    @Override
    public @NotNull List<ItemStack> addToInventory(@NotNull Collection<ItemStack> items) {
        List<ItemStack> unAdded = new ArrayList<>();

        for (ItemStack item : items) {
            if (!this.getOwningRegion().addCropsToInventory(item).isEmpty()) {
                unAdded.add(item);
            }
        }

        return unAdded;
    }

    @Override
    public @NotNull List<ItemStack> addToContainer(@NotNull ItemStack... crops) {
        return this.addToContainer(Arrays.asList(crops));
    }

    @Override
    public @NotNull List<ItemStack> addToContainer(@NotNull Collection<ItemStack> items) {
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

    @Override
    public int getRemainingHarvestables() {
        return (int) this.boxBlocks.stream()
                .filter(block -> block.getType() != Material.AIR)
                .filter(block -> CropType.getByMaterial(block.getType()).isPresent())
                .count();
    }

    @Override
    public boolean isHarvestablePresent() {
        Set<Material> boxBlocksMat = this.boxBlocks.stream()
                .map(Block::getType)
                .collect(Collectors.toSet());

        return CropType.getMaterials().stream()
                .anyMatch(boxBlocksMat::contains);
    }

    @Override
    public void updateHologram() {
        this.hologram.getLines().stream()
                .filter(iLine -> iLine instanceof BlockLine)
                .map(iLine -> ((BlockLine) iLine))
                .forEach(blockLine -> blockLine.setObj(new ItemStack(this.currentlyHarvesting.getItemMaterial())));

        this.hologram.getLines().forEach(iLine -> iLine.update(this.owningRegion.getAllOnlinePlayers()));
    }

    @Override
    public boolean setAutoStore(boolean autoStore) {
        if (!this.getOwningRegion().hasFreeAutoStoreSlots() && autoStore) {
            return false;
        }

        this.autoStore = autoStore;
        this.updateHologram();
        return true;
    }

    @Override
    public Consumer<PlayerHologramInteractEvent> getHologramAction() {
        return event -> {
            if (event.getHologram().equals(this.hologram)) {
                new FarmPlotMainMenu(this, true).show(event.getPlayer());
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return MiniMessage.miniMessage().deserialize("<b><color:#FC7B03>Farm Plot " + id + "</color></b>");
    }

    @Override
    public List<Component> getLore() {
        return this.parsePlaceholdersAndConvertToComponent(List.of(
                "<color:#FCDB03>Upgrades: <red>OFF</red></color>",
                "<color:#D4F542>Enchanted Rain: <red>OFF</red></color>",
                "<color:#2B84FF>Auto Store: %barn_plot_farm_colored_status_autostore_" + this.id + "%</color>"
        ));
    }
}
