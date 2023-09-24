package com.bof.core.region.plot.animal;

import com.bof.core.region.BarnRegion;
import com.bof.core.region.plot.HarvestablePlot;
import com.bof.core.region.plot.PlotType;
import com.bof.core.region.plot.animal.menu.AnimalPlotMainMenu;
import com.bof.core.region.plot.animal.nms.NMSAnimal;
import com.bof.core.utils.BoxUtils;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import com.github.unldenis.hologram.line.BlockLine;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Data
public class AnimalPlot implements HarvestablePlot<AnimalType> {
    private final BarnRegion owningRegion;
    private final PlotType type = PlotType.ANIMAL;
    private final BoundingBox box;
    private final int id;
    private final Set<Block> boxBlocks;
    private AnimalType currentlyHarvesting = AnimalType.NONE;
    private Hologram hologram;
    private boolean autoStore = false;

    public AnimalPlot(@NotNull BarnRegion owningRegion, @NotNull BoundingBox box, int id) {
        this.owningRegion = owningRegion;
        this.box = box;
        this.boxBlocks = BoxUtils.getBlocksInBox(box, true);
        this.id = id;
    }

    public void setCurrentlyHarvesting(@NotNull AnimalType type) {
        this.currentlyHarvesting = type;
        this.updateHologram();
    }


    public void changeAnimals(AnimalType type) {
        Location center = BoxUtils.getCenterLocation(this.box);
        new NMSAnimal(center, type);
        this.setCurrentlyHarvesting(type);
    }

    public int harvestAnimals(@NotNull Player player) {
        player.sendMessage("TO FIX - finish adding!");
        return 0;
    }
/*
    public int harvestAnimals(@NotNull Player player) {
        return this.handleAnimalBreak(player, this.boxBlocks);
    }

    public int handleAnimalBreak(@NotNull Player player, @NotNull Block... blocks) {
        return this.handleAnimalBreak(player, Arrays.asList(blocks));
    }
    
    public int handleAnimalBreak(@NotNull Player player, @NotNull Collection<Block> blocks) {
        int count = 0;

        // there is nothing to harvest
        if (currentlyHarvesting == AnimalType.NONE || !isAnimalPresent()) {
            return count;
        }

        for (Block block : blocks) {
            if (AnimalType.getByMaterial(block.getType())
                    .filter(cropType -> cropType != AnimalType.NONE)
                    .isPresent()) {

                ItemStack item = new ItemStack(block.getType());
                if (this.isAutoStore()) {
                    // first tries putting into silo, then tries inventory, if both fails, returns list of items which failed
                    if (!this.addAnimalsToSilo(player, item).isEmpty()) {
                        player.sendMessage("TO ADD - All silos are full. Putting the items to inventory");
                        if (!this.addAnimalsToInventory(player, item).isEmpty()) {
                            player.sendMessage("TO ADD - Animals inventory is full 1");
                        }
                        break;
                    }
                } else {
                    if (!this.addAnimalsToInventory(player, item).isEmpty()) {
                        player.sendMessage("TO ADD - Animals inventory is full 2");
                        break;
                    }
                }

                block.setType(Material.AIR);
                count++;
            }
        }

        // everything was harvested
        if (this.getRemainingAnimals() == 0) {
            this.setCurrentlyHarvesting(AnimalType.NONE);
        }

        return count;
    }


    public @NotNull List<ItemStack> addAnimalsToInventory(@NotNull Player player, @NotNull ItemStack... items) {
        return this.addAnimalsToInventory(player, Arrays.asList(items));
    }


    public @NotNull List<ItemStack> addAnimalsToInventory(@NotNull Player player, @NotNull Collection<ItemStack> crops) {
        List<ItemStack> unAdded = new ArrayList<>();

        for (ItemStack item : crops) {
            if (!this.getOwningRegion().addAnimalsToInventory(item).isEmpty()) {
                unAdded.add(item);
            }
        }

        return unAdded;
    }


    public @NotNull List<ItemStack> addAnimalsToSilo(@NotNull Player player, @NotNull ItemStack... crops) {
        return this.addAnimalsToSilo(player, Arrays.asList(crops));
    }

    public @NotNull List<ItemStack> addAnimalsToSilo(@NotNull Player player, @NotNull Collection<ItemStack> items) {
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
            if (!silo.addAnimalsToSilo(item).isEmpty()) {
                optSilo = this.getOwningRegion().getFreeSilo();
            }
        }

        return unAdded;
    }

    public int getRemainingAnimals() {
        return (int) boxBlocks.stream()
                .filter(block -> block.getType() != Material.AIR)
                .filter(block -> AnimalType.getByMaterial(block.getType()).isPresent())
                .count();
    }

    private boolean isAnimalPresent() {
        Set<Material> boxBlocksMat = boxBlocks.stream()
                .map(Block::getType)
                .collect(Collectors.toSet());

        return AnimalType.getMaterials().stream()
                .anyMatch(boxBlocksMat::contains);
    }

 */

    public void updateHologram() {
        this.hologram.getLines().stream()
                .filter(iLine -> iLine instanceof BlockLine)
                .map(iLine -> ((BlockLine) iLine))
                .forEach(blockLine -> blockLine.setObj(new ItemStack(currentlyHarvesting.getItem())));

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
                new AnimalPlotMainMenu(this, true).show(event.getPlayer());
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return MiniMessage.miniMessage().deserialize("<b><color:#3bff5b>Animal Plot " + id + "</color></b>");
    }

    @Override
    public List<Component> getLore() {
        return List.of(
                MiniMessage.miniMessage().deserialize("<color:#FCDB03>Upgrades: <red>OFF</red></color>"),
                MiniMessage.miniMessage().deserialize("<color:#D4F542>Enchanted Rain: <red>OFF</red></color>"),
                MiniMessage.miniMessage().deserialize("<color:#2B84FF>Auto Store: %barn_plot_animal_colored_status_autostore_" + this.id + "%</color>")
        );
    }
}
