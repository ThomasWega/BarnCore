package com.bof.barn.core.region.plot.harvestable.farm;

import com.bof.barn.core.Core;
import com.bof.barn.core.HarvestableManager;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.PlotSetting;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.AdditionResult;
import com.bof.barn.core.region.plot.harvestable.HarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.farm.menu.FarmPlotMainMenu;
import com.bof.barn.core.region.plot.harvestable.setting.AutoStoreSetting;
import com.bof.barn.core.region.plot.selling.silo.SiloPlot;
import com.bof.barn.core.utils.BoxUtils;
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

import static com.bof.barn.core.Core.WORLD;

@Data
public class FarmPlot implements HarvestablePlot<CropType> {
    private final Core plugin;
    private final Map<Class<? extends PlotSetting>, PlotSetting> settings = new HashMap<>();
    private final BarnRegion owningRegion;
    private final PlotType type = PlotType.FARM;
    private final BoundingBox box;
    private final int id;
    private final Set<Block> boxBlocks;
    private CropType currentlyHarvesting = CropType.NONE;
    private Hologram hologram;

    public FarmPlot(Core plugin, BarnRegion owningRegion, BoundingBox box, int id) {
        this.plugin = plugin;
        this.owningRegion = owningRegion;
        this.box = box;
        this.boxBlocks = BoxUtils.getBlocksInBox(box, true);
        this.id = id;
    }

    public void setCurrentlyHarvesting(@NotNull CropType cropType) {
        this.currentlyHarvesting = cropType;
        this.updateHologram();
    }

    @Override
    public void changeType(@NotNull CropType type) {
        this.boxBlocks.forEach(block -> {
            if (block.getRelative(BlockFace.DOWN).getType() == Material.FARMLAND) {
                if (type == CropType.NONE) {
                    WORLD.playSound(block.getLocation(), block.getBlockSoundGroup().getBreakSound(), 1f, 1f);
                }
                block.setType(type.getMaterial());

                // it's done this stupidly and not in one if else, because I need to change the type between them
                if (type != CropType.NONE) {
                    WORLD.playSound(block.getLocation(), block.getBlockSoundGroup().getPlaceSound(), 1f, 1f);
                }

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
        this.handleAutoReplant();
        return this.handleCropBreak(player, false, this.boxBlocks);
    }

    /**
     * Handles the breakage of block.
     * Changes the {@link #currentlyHarvesting} type, puts the items into {@link SiloPlot} on {@link AutoStoreSetting},
     * if the silo is full, tries putting it into {@link BarnRegion#getCropsInventory()}.
     * If that is full as well, sends a message to the player
     *
     * @param player Player that broke the crop
     * @param blocks Blocks that were broken
     * @param byHand   Whether the crop was broken by hand
     * @return amount of crops that were successfully broken
     */
    public int handleCropBreak(@NotNull Player player, boolean byHand, @NotNull Block... blocks) {
        return this.handleCropBreak(player, byHand, Arrays.asList(blocks));
    }

    /**
     * Handles the breakage of block.
     * Changes the {@link #currentlyHarvesting} type, puts the items into {@link SiloPlot} on {@link AutoStoreSetting},
     * if the silo is full, tries putting it into {@link BarnRegion#getCropsInventory()}.
     * If that is full as well, sends a message to the player
     *
     * @param player Player that broke the crop
     * @param blocks Blocks that were broken
     * @param byHand   Whether the crop was broken by hand
     * @return amount of crops that were successfully broken
     */
    public int handleCropBreak(@NotNull Player player, boolean byHand, @NotNull Collection<Block> blocks) {
        final int[] count = {0};

        // there is nothing to harvest
        if (this.currentlyHarvesting == CropType.NONE || !isHarvestablePresent()) {
            return count[0];
        }

        for (Block block : blocks) {
            CropType.getByMaterial(block.getType())
                    .filter(cropType -> cropType != CropType.NONE)
                    .ifPresent(cropType -> {
                        ItemStack item = new ItemStack(cropType.getItem());
                        if (byHand) {
                            item = HarvestableManager.getDrop(cropType);
                        } else {
                            WORLD.playSound(FarmPlotSound.CROP.getSound(), block.getLocation().getX(), block.getLocation().getY(), block.getLocation().getZ());
                        }
                        AdditionResult result = this.handleAddition(item);
                        switch (result) {
                            case CONTAINER_FULL -> player.sendMessage("TO ADD - All silos are full. Putting the items to inventory");
                            case INV_FULL -> player.sendMessage("TO ADD - Crops inventory is full 1");
                            case SUCCESS -> {
                                block.setType(Material.AIR);
                                count[0]++;
                            }
                        }
                    });
        }

        int bonusCount = HarvestableManager.handleBonusDrops(this, blocks);
        if (bonusCount > 0) {
            player.sendMessage("TO ADD - bonus drops " + bonusCount);
        }

        // everything was harvested
        if (this.getRemainingHarvestablesCount() == 0) {
            this.setCurrentlyHarvesting(CropType.NONE);
        }

        return count[0];
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
            if (!silo.addHarvestablesToContainer(item).isEmpty()) {
                optSilo = this.getOwningRegion().getFreeSilo();
            }
        }

        return unAdded;
    }

    @Override
    public int getRemainingHarvestablesCount() {
        return this.getRemainingHarvestables().size();
    }

    public @NotNull Map<Block, CropType> getRemainingHarvestables() {
        return this.boxBlocks.stream()
                .filter(block -> block.getType() != Material.AIR)
                .map(block -> Map.entry(block, CropType.getByMaterial(block.getType())))
                .filter(entry -> entry.getValue().isPresent())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().get()
                ));
    }

    @Override
    public void updateHologram() {
        this.hologram.getLines().stream()
                .filter(iLine -> iLine instanceof BlockLine)
                .map(iLine -> ((BlockLine) iLine))
                .forEach(blockLine -> blockLine.setObj(this.getCurrentlyHarvesting().getItem()));

        this.hologram.getLines().forEach(iLine -> iLine.update(this.owningRegion.getAllOnlinePlayers()));
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
                "<color:#FCDB03>Production: <red>N/s</red></color>",
                "<color:#D4F542>Multipliers: <red>0x</red></color>"
        ));
    }
}
