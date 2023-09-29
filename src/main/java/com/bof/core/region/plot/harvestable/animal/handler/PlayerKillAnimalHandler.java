package com.bof.core.region.plot.harvestable.animal.handler;

import com.bof.core.player.GamePlayer;
import com.bof.core.region.plot.PlotType;
import com.bof.core.region.plot.harvestable.animal.AnimalPlot;
import com.bof.core.region.plot.harvestable.animal.AnimalType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * Handles when an animal is hit or killed
 */
public class PlayerKillAnimalHandler implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void onPlayerKillAnimal(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        Player player = entity.getKiller();
        if (player == null) return;

        GamePlayer gamePlayer = GamePlayer.get(player);

        gamePlayer.getBarnRegion().ifPresent(region -> region.getPlots().get(PlotType.ANIMAL).stream()
                .map(plot -> ((AnimalPlot) plot))
                .forEach(plot -> {
                    if (!plot.getAnimals().contains(entity.getUniqueId())) {
                        return;
                    }
                    event.setDroppedExp(0);
                    event.getDrops().clear();
                })
        );
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerDamageAnimal(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (AnimalType.getByEntityType(event.getEntityType()).isEmpty()) return;
        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        GamePlayer gamePlayer = GamePlayer.get(player);

        gamePlayer.getBarnRegion().ifPresent(region -> region.getPlots().get(PlotType.ANIMAL).stream()
                .map(plot -> ((AnimalPlot) plot))
                .forEach(plot -> {
                    if (!plot.getAnimals().contains(entity.getUniqueId())) return;

                    if (plot.handleAnimalKill(player, entity) == 0) {
                        player.sendMessage("TO ADD - Animal inventory is full 3");
                        event.setCancelled(true);
                    }
                })
        );
    }
}
