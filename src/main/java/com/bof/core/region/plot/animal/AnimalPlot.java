package com.bof.core.region.plot.animal;

import com.bof.core.region.BarnRegion;
import com.bof.core.region.plot.HarvestablePlot;
import com.bof.core.region.plot.PlotType;
import com.bof.core.region.plot.animal.menu.AnimalPlotMainMenu;
import com.bof.core.region.plot.barn.BarnPlot;
import com.bof.core.utils.BoxUtils;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import com.github.unldenis.hologram.line.BlockLine;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Data
public class AnimalPlot implements HarvestablePlot<AnimalType> {
    private final BarnRegion owningRegion;
    private final PlotType type = PlotType.ANIMAL;
    private final BoundingBox box;
    private final int id;
    private final Set<Block> boxBlocks;
    private final Set<UUID> animals = new HashSet<>();
    private final int animalCount = 6;
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


    public void changeAnimals(@NotNull AnimalType type) {
        // remove previous mobs
        this.animals.forEach(uuid -> {
            LivingEntity entity = ((LivingEntity) Bukkit.getWorld("world").getEntity(uuid));
            if (entity != null) {
                entity.setHealth(0);
            }
        });
        this.animals.clear();

        // add new mobs
        if (type != AnimalType.NONE) {
            for (int i = 0; i < animalCount; i++) {
                Location location = BoxUtils.getRandomLocation(this.getBox());
                this.animals.add(Bukkit.getWorld("world").spawnEntity(location, type.getEntityType(), CreatureSpawnEvent.SpawnReason.CUSTOM).getUniqueId());
            }
        }
        this.setCurrentlyHarvesting(type);
    }

    private Set<LivingEntity> getEntities() {
        return this.animals.stream()
                .map(uuid -> ((LivingEntity) Bukkit.getWorld("world").getEntity(uuid)))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }


    public int harvestAnimals(@NotNull Player player) {
        Set<LivingEntity> entities = this.getEntities();
        return this.handleAnimalKill(player, entities.toArray(LivingEntity[]::new));
    }

    public int handleAnimalKill(@NotNull Player player, @NotNull LivingEntity... entities) {
        int count = 0;

        // there is nothing to harvest
        if (currentlyHarvesting == AnimalType.NONE || !isAnimalPresent()) {
            return count;
        }

        for (LivingEntity entity : entities) {
            Optional<AnimalType> optAnimalType = AnimalType.getByEntityType(entity.getType())
                    .filter(type -> type != AnimalType.NONE);

            if (optAnimalType.isPresent()) {
                AnimalType animalType = optAnimalType.get();
                ItemStack item = new ItemStack(animalType.getItem());
                if (this.isAutoStore()) {
                    // first tries putting into barn, then tries inventory, if both fails, returns list of items which failed
                    if (!this.addAnimalsToBarn(item).isEmpty()) {
                        player.sendMessage("TO ADD - The barn is full. Putting the items to inventory");
                        if (!this.addAnimalsToInventory(item).isEmpty()) {
                            player.sendMessage("TO ADD - Animals inventory is full 1");
                        }
                        break;
                    }
                } else {
                    if (!this.addAnimalsToInventory(item).isEmpty()) {
                        player.sendMessage("TO ADD - Animals inventory is full 2");
                        break;
                    }
                }

                entity.setKiller(player);
                entity.setHealth(0);
                count++;

                // needs to be after killing the mob
                this.animals.remove(entity.getUniqueId());
            }
        }

        // everything was harvested
        if (this.getRemainingAnimals() == 0) {
            this.setCurrentlyHarvesting(AnimalType.NONE);
        }

        return count;
    }


    public @NotNull List<ItemStack> addAnimalsToInventory(@NotNull ItemStack... items) {
        return this.addAnimalsToInventory(Arrays.asList(items));
    }


    public @NotNull List<ItemStack> addAnimalsToInventory(@NotNull Collection<ItemStack> animals) {
        List<ItemStack> unAdded = new ArrayList<>();

        for (ItemStack item : animals) {
            if (!this.getOwningRegion().addAnimalsToInventory(item).isEmpty()) {
                unAdded.add(item);
            }
        }

        return unAdded;
    }


    public @NotNull List<ItemStack> addAnimalsToBarn(@NotNull ItemStack... animals) {
        return this.addAnimalsToBarn(Arrays.asList(animals));
    }

    public @NotNull List<ItemStack> addAnimalsToBarn(@NotNull Collection<ItemStack> items) {
        List<ItemStack> unAdded = new ArrayList<>();
        Optional<BarnPlot> optBarn = this.getOwningRegion().getFreeBarn();

        for (ItemStack item : items) {
            // no free barn is available, so put all remaining items as unAdded
            if (optBarn.isEmpty()) {
                unAdded.add(item);
                continue;
            }

            BarnPlot barn = optBarn.get();
            // barn is full, so try getting a new free barn
            if (!barn.addAnimalsToBarn(item).isEmpty()) {
                optBarn = this.getOwningRegion().getFreeBarn();
            }
        }

        return unAdded;
    }

    public int getRemainingAnimals() {
        return this.getEntities().size();
    }

    private boolean isAnimalPresent() {
        return this.getRemainingAnimals() > 0;
    }


    public void updateHologram() {
        this.hologram.getLines().stream()
                .filter(iLine -> iLine instanceof BlockLine)
                .map(iLine -> ((BlockLine) iLine))
                .forEach(blockLine -> blockLine.setObj(new ItemStack(this.currentlyHarvesting.getItem())));

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
