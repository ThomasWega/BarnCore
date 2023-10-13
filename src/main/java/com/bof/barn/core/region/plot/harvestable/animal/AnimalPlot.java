package com.bof.barn.core.region.plot.harvestable.animal;

import com.bof.barn.core.Core;
import com.bof.barn.core.HarvestableManager;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.PlotSetting;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.AdditionResult;
import com.bof.barn.core.region.plot.harvestable.HarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.animal.menu.AnimalPlotMainMenu;
import com.bof.barn.core.region.plot.harvestable.setting.AutoStoreSetting;
import com.bof.barn.core.region.plot.selling.barn.BarnPlot;
import com.bof.barn.core.utils.BoxUtils;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import com.github.unldenis.hologram.line.BlockLine;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.block.Block;
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


@Data
public class AnimalPlot implements HarvestablePlot<AnimalType> {
    private final Core plugin;
    private final Map<Class<? extends PlotSetting>, PlotSetting> settings = new HashMap<>();
    private final BarnRegion owningRegion;
    private final PlotType type = PlotType.ANIMAL;
    private final BoundingBox box;
    private final int id;
    private final Set<Block> boxBlocks;
    private final Set<UUID> animals = new HashSet<>();
    private final int animalCount = 6;
    private AnimalType currentlyHarvesting = AnimalType.NONE;
    private Hologram hologram;

    public AnimalPlot(Core plugin, BarnRegion owningRegion, BoundingBox box, int id) {
        this.plugin = plugin;
        this.owningRegion = owningRegion;
        this.box = box;
        this.boxBlocks = BoxUtils.getBlocksInBox(box, true);
        this.id = id;
    }

    public void setCurrentlyHarvesting(@NotNull AnimalType animalType) {
        this.currentlyHarvesting = animalType;
        this.updateHologram();
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
        this.handleAutoReplant();
        return this.handleAnimalKill(player, false, this.getEntities());
    }

    /**
     * Handles the killing of animals.
     * Changes the {@link #currentlyHarvesting} type, puts the items into {@link BarnPlot} on {@link AutoStoreSetting},
     * if the barn is full, tries putting it into {@link BarnRegion#getAnimalInventory()}.
     * If that is full as well, sends a message to the player
     *
     * @param player   Player that killed the animals
     * @param entities Animals that were killed
     * @param byHand   Whether the animal was killed by hand
     * @return amount of animals that were successfully killed
     */
    public int handleAnimalKill(@NotNull Player player, boolean byHand, @NotNull Collection<LivingEntity> entities) {
        final int[] count = {0};

        // there is nothing to harvest
        if (currentlyHarvesting == AnimalType.NONE || !isHarvestablePresent()) {
            return count[0];
        }

        List<LivingEntity> entitiesCopy = new ArrayList<>(entities);
        for (LivingEntity entity : entitiesCopy) {
            AnimalType.getByEntityType(entity.getType())
                    .filter(type -> type != AnimalType.NONE)
                    .ifPresent(animalType -> {
                        ItemStack item = new ItemStack(animalType.getItem());
                        if (byHand) {
                            item = HarvestableManager.getDrop(animalType);
                        }
                        AdditionResult result = this.handleAddition(item);
                        switch (result) {
                            case CONTAINER_FULL ->
                                    player.sendMessage("TO ADD - All barns are full. Putting the items to inventory");
                            case INV_FULL -> player.sendMessage("TO ADD - Animal inventory is full 1");
                            case SUCCESS -> {
                                entity.setKiller(player);
                                entity.setHealth(0);
                                count[0]++;
                                // needs to be after killing the mob
                                this.animals.remove(entity.getUniqueId());
                                WORLD.playSound(entity.getLocation(), entity.getDeathSound(), 1f, 1f);
                            }
                        }
                    });
        }

        int bonusCount = HarvestableManager.handleBonusDrops(this, entitiesCopy);
        if (bonusCount > 0) {
            player.sendMessage("TO ADD - bonus drops " + bonusCount);
        }

        // everything was harvested
        if (this.getRemainingHarvestablesCount() == 0) {
            this.setCurrentlyHarvesting(AnimalType.NONE);
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

    @Override
    public void updateHologram() {
        this.hologram.getLines().stream()
                .filter(iLine -> iLine instanceof BlockLine)
                .map(iLine -> ((BlockLine) iLine))
                .forEach(blockLine -> blockLine.setObj(new ItemStack(this.currentlyHarvesting.getItem())));

        this.hologram.getLines().forEach(iLine -> iLine.update(this.owningRegion.getAllOnlinePlayers()));
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
                "<color:#FCDB03>Production: <red>N/s</red></color>",
                "<color:#D4F542>Multipliers: <red>0x</red></color>"
        ));
    }
}
