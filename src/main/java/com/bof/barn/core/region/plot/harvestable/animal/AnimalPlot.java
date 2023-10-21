package com.bof.barn.core.region.plot.harvestable.animal;

import com.bof.barn.core.Core;
import com.bof.barn.core.HarvestableManager;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.container.barn.BarnPlot;
import com.bof.barn.core.region.plot.harvestable.AbstractHarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.AdditionResult;
import com.bof.barn.core.region.plot.harvestable.animal.menus.AnimalPlotMainMenu;
import com.bof.barn.core.region.plot.harvestable.settings.impl.AutoStoreSetting;
import com.bof.barn.core.region.plot.harvestable.tasks.ReplantAllTask;
import com.bof.barn.core.utils.BoxUtils;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import com.github.unldenis.hologram.line.BlockLine;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
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

@Getter
@Setter
public class AnimalPlot extends AbstractHarvestablePlot<AnimalType> {
    private final Set<UUID> animals = new HashSet<>();
    private final int animalCount = 6;

    public AnimalPlot(@NotNull Core plugin, @NotNull BarnRegion owningRegion, @NotNull BoundingBox box, int id) {
        super(plugin, PlotType.ANIMAL, owningRegion, box, id, AnimalType.NONE);
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
        Entity entity = WORLD.spawnEntity(location, type.getEntityType(), CreatureSpawnEvent.SpawnReason.CUSTOM);
        entity.setSilent(true);
        WORLD.playSound(AnimalPlotSound.SUMMON.getSound(), entity.getX(), entity.getY(), entity.getZ());
        this.animals.add(entity.getUniqueId());
    }

    private Set<LivingEntity> getEntities() {
        return this.animals.stream()
                .map(uuid -> ((LivingEntity) WORLD.getEntity(uuid)))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }


    @Override
    public int harvest(@NotNull Player player) {
        if (this.getRemainingHarvestablesCount() == 0) return 0;

        new ReplantAllTask<>(this).run();

        int i = 0;
        animalLoop:
        for (LivingEntity entity : this.getEntities()) {
            switch (this.handleAnimalKill(player, false, entity)) {
                case SUCCESS -> i++;
                case INV_FULL -> {
                    player.sendMessage("TO ADD - Animal inventory is full 1");
                    break animalLoop;
                }
                case CONTAINER_FULL -> {
                    player.sendMessage("TO ADD - All barns are full. Putting the items to inventory");
                    break animalLoop;
                }
            }
        }

        int bonusCount = HarvestableManager.handleBonusDrops(this, this.animals);
        if (bonusCount > 0) {
            player.sendMessage("TO ADD - bonus drops " + bonusCount);
        }

        return i;
    }

    /**
     * Handles the killing of animals.
     * Changes the {@link #currentlyHarvesting} type, puts the items into {@link BarnPlot} on {@link AutoStoreSetting},
     * if the barn is full, tries putting it into {@link BarnRegion#getAnimalInventory()}.
     *
     * @param player Player that killed the animals
     * @param entity Animal that was killed
     * @param byHand Whether the animal was killed by hand
     * @return result when trying to save the animal drop
     */
    public @NotNull AdditionResult handleAnimalKill(@NotNull Player player, boolean byHand, @NotNull LivingEntity entity) {
        final AdditionResult[] result = new AdditionResult[1];
        AnimalType.getByEntityType(entity.getType())
                .filter(type -> type != AnimalType.NONE)
                .ifPresent(animalType -> {
                    ItemStack item = new ItemStack(animalType.getItem());
                    if (byHand) {
                        item = HarvestableManager.getDrop(animalType);
                    }
                    result[0] = this.handleAddition(item);
                    if (result[0] == AdditionResult.SUCCESS) {
                        entity.setKiller(player);
                        entity.setHealth(0);
                        // needs to be after killing the mob
                        this.animals.remove(entity.getUniqueId());
                        // the animal is silent, so play the sounds
                        WORLD.playSound(entity.getLocation(), entity.getDeathSound(), 1f, 1f);
                    }
                });

        // everything was harvested
        if (this.getRemainingHarvestablesCount() == 0) {
            this.setCurrentlyHarvesting(AnimalType.NONE);
        }

        return result[0];
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
            if (!barn.addHarvestablesToContainer(item).isEmpty()) {
                optBarn = this.getOwningRegion().getFreeBarn();
            }
        }

        return unAdded;
    }

    @Override
    public int getRemainingHarvestablesCount() {
        return this.getEntities().size();
    }

    public @NotNull Map<LivingEntity, AnimalType> getRemainingHarvestables() {
        return this.animals.stream()
                .map(uuid -> (LivingEntity) WORLD.getEntity(uuid))
                .map(entity -> Map.entry(entity, AnimalType.getByEntityType(entity.getType())))
                .filter(entry -> entry.getValue().isPresent())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().get()
                ));
    }

    @Override
    public void updateHologram() {
        this.getHologram().getLines().stream()
                .filter(iLine -> iLine instanceof BlockLine)
                .map(iLine -> ((BlockLine) iLine))
                .forEach(blockLine -> blockLine.setObj(new ItemStack(this.currentlyHarvesting.getItem())));

        this.getHologram().getLines().forEach(iLine -> iLine.update(this.getOwningRegion().getAllOnlinePlayers()));
    }

    @Override
    public Consumer<PlayerHologramInteractEvent> getHologramAction() {
        return event -> {
            if (event.getHologram().equals(this.getHologram())) {
                new AnimalPlotMainMenu(this, true).show(event.getPlayer());
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return MiniMessage.miniMessage().deserialize("<b><color:#3bff5b>Animal Plot " + getId() + "</color></b>");
    }

    @Override
    public List<Component> getLore() {
        return this.parsePlaceholdersAndConvertToComponent(List.of(
                "<color:#FCDB03>Production: <red>N/s</red></color>",
                "<color:#D4F542>Multipliers: <red>0x</red></color>"
        ));
    }
}
