package com.bof.barn.core.region.plot.harvestable.animal;

import com.bof.barn.core.region.plot.harvestable.HarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.animal.menu.AnimalPlotMainMenu;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.selling.barn.BarnPlot;
import com.bof.barn.core.utils.BoxUtils;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import com.github.unldenis.hologram.line.BlockLine;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
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

import static com.bof.barn.core.Core.WORLD;

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
    @Setter(AccessLevel.NONE)
    private boolean autoStore = false;

    public AnimalPlot(@NotNull BarnRegion owningRegion, @NotNull BoundingBox box, int id) {
        this.owningRegion = owningRegion;
        this.box = box;
        this.boxBlocks = BoxUtils.getBlocksInBox(box, true);
        this.id = id;
    }

    @Override
    public void changeType(@NotNull AnimalType type) {
        // remove previous mobs
        this.animals.forEach(uuid -> {
            LivingEntity entity = ((LivingEntity) WORLD.getEntity(uuid));
            if (entity != null) {
                entity.remove();
            }
        });
        this.animals.clear();

        // add new mobs
        if (type != AnimalType.NONE) {
            for (int i = 0; i < animalCount; i++) {
                this.addAnimal(type);
            }
        }

        this.setCurrentlyHarvesting(type);
        this.updateHologram();
    }

    public void addAnimal(AnimalType type) {
        Location location = BoxUtils.getRandomLocation(this.getBox());
        this.animals.add(WORLD.spawnEntity(location, type.getEntityType(), CreatureSpawnEvent.SpawnReason.CUSTOM).getUniqueId());
    }

    private Set<LivingEntity> getEntities() {
        return this.animals.stream()
                .map(uuid -> ((LivingEntity) WORLD.getEntity(uuid)))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }


    @Override
    public int harvest(@NotNull Player player) {
        Set<LivingEntity> entities = this.getEntities();
        return this.handleAnimalKill(player, entities.toArray(LivingEntity[]::new));
    }

    /**
     * Handles the killing of animals.
     * Changes the {@link #currentlyHarvesting} type, puts the items into {@link BarnPlot} on {@link #autoStore},
     * if the barn is full, tries putting it into {@link BarnRegion#getAnimalInventory()}.
     * If that is full as well, sends a message to the player
     *
     * @param player   Player that killed the animals
     * @param entities Animals that were killed
     * @return amount of animals that were successfully killed
     */
    public int handleAnimalKill(@NotNull Player player, @NotNull LivingEntity... entities) {
        return this.handleAnimalKill(player, Arrays.asList(entities));
    }

    /**
     * Handles the killing of animals.
     * Changes the {@link #currentlyHarvesting} type, puts the items into {@link BarnPlot} on {@link #autoStore},
     * if the barn is full, tries putting it into {@link BarnRegion#getAnimalInventory()}.
     * If that is full as well, sends a message to the player
     *
     * @param player   Player that killed the animals
     * @param entities Animals that were killed
     * @return amount of animals that were successfully killed
     */
    public int handleAnimalKill(@NotNull Player player, @NotNull Collection<LivingEntity> entities) {
        int count = 0;

        // there is nothing to harvest
        if (currentlyHarvesting == AnimalType.NONE || !isHarvestablePresent()) {
            return count;
        }

        List<LivingEntity> entitiesCopy = new ArrayList<>(entities);
        for (LivingEntity entity : entitiesCopy) {
            Optional<AnimalType> optAnimalType = AnimalType.getByEntityType(entity.getType())
                    .filter(type -> type != AnimalType.NONE);

            if (optAnimalType.isPresent()) {
                AnimalType animalType = optAnimalType.get();
                ItemStack item = new ItemStack(animalType.getItem());
                if (this.isAutoStore()) {
                    // first tries putting into barn, then tries inventory, if both fails, returns list of items which failed
                    if (!this.addToContainer(item).isEmpty()) {
                        player.sendMessage("TO ADD - The barn is full. Putting the items to inventory");
                        if (!this.addToInventory(item).isEmpty()) {
                            player.sendMessage("TO ADD - Animals inventory is full 1");
                        }
                        break;
                    }
                } else {
                    if (!this.addToInventory(item).isEmpty()) {
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
        if (this.getRemainingHarvestables() == 0) {
            this.setCurrentlyHarvesting(AnimalType.NONE);
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
            if (!this.getOwningRegion().addAnimalsToInventory(item).isEmpty()) {
                unAdded.add(item);
            }
        }

        return unAdded;
    }


    @Override
    public @NotNull List<ItemStack> addToContainer(@NotNull ItemStack... items) {
        return this.addToContainer(Arrays.asList(items));
    }

    @Override
    public @NotNull List<ItemStack> addToContainer(@NotNull Collection<ItemStack> items) {
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

    @Override
    public int getRemainingHarvestables() {
        return this.getEntities().size();
    }

    @Override
    public boolean isHarvestablePresent() {
        return this.getRemainingHarvestables() > 0;
    }

    @Override
    public void updateHologram() {
        this.hologram.getLines().stream()
                .filter(iLine -> iLine instanceof BlockLine)
                .map(iLine -> ((BlockLine) iLine))
                .forEach(blockLine -> blockLine.setObj(new ItemStack(this.currentlyHarvesting.getItem())));

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
        return this.parsePlaceholdersAndConvertToComponent(List.of(
                "<color:#FCDB03>Upgrades: <red>OFF</red></color>",
                "<color:#D4F542>Enchanted Rain: <red>OFF</red></color>",
                "<color:#2B84FF>Auto Store: %barn_plot_animal_colored_status_autostore_" + this.id + "%</color>"
        ));
    }
}
