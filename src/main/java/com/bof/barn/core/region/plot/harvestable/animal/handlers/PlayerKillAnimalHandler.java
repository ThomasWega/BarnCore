package com.bof.barn.core.region.plot.harvestable.animal.handlers;

import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.AdditionResult;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalPlot;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalType;
import com.bof.barn.core.region.storage.RegionStorage;
import de.tr7zw.changeme.nbtapi.NBT;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Handles when an animal is hit or killed
 */
@RequiredArgsConstructor
public class PlayerKillAnimalHandler implements Listener {
    private final @NotNull RegionStorage regionStorage;

    @EventHandler(ignoreCancelled = true)
    private void onPlayerKillAnimal(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        UUID regionUUID = NBT.getPersistentData(entity, readableNBT -> readableNBT.getUUID("region-uuid"));
        if (regionUUID == null) return;
        int plotId = NBT.getPersistentData(entity, readableNBT -> readableNBT.getInteger("animal-plot-id"));

        regionStorage.getByUUID(regionUUID)
                .flatMap(region -> region.getPlot(PlotType.ANIMAL, plotId)
                        .map(plot -> (AnimalPlot) plot))
                .filter(plot -> plot.getAnimals().contains(entity.getUniqueId()))
                .ifPresent(plot -> {
                    event.setDroppedExp(0);
                    event.getDrops().clear();
                });
    }

    /**
     * Kill the animal on any player hit and handle the kill ourselves
     */
    @EventHandler(ignoreCancelled = true)
    private void onPlayerDamageAnimal(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (AnimalType.getByEntityType(event.getEntityType()).isEmpty()) return;
        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        UUID regionUUID = NBT.getPersistentData(entity, readableNBT -> readableNBT.getUUID("region-uuid"));
        if (regionUUID == null) return;
        int plotId = NBT.getPersistentData(entity, readableNBT -> readableNBT.getInteger("animal-plot-id"));

        regionStorage.getByUUID(regionUUID)
                .flatMap(region -> region.getPlot(PlotType.ANIMAL, plotId)
                        .map(plot -> (AnimalPlot) plot))
                .filter(plot -> plot.getAnimals().contains(entity.getUniqueId()))
                .ifPresent(plot -> {
                    AdditionResult result = plot.handleAnimalKill(true, entity);
                    switch (result) {
                        case INV_FULL -> {
                            player.sendMessage("TO ADD - Animal inventory is full 3");
                            event.setCancelled(true);
                        }
                        case CONTAINER_FULL -> {
                            player.sendMessage("TO ADD - Barn inventory is full IDK");
                            event.setCancelled(true);
                        }
                    }
                }
        );
    }
}
