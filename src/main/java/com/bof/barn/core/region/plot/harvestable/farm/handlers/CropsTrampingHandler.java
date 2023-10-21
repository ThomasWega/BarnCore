package com.bof.barn.core.region.plot.harvestable.farm.handlers;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Cancels tramping of the crops on {@link com.bof.barn.core.region.plot.harvestable.farm.FarmPlot}
 */
public class CropsTrampingHandler implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void onCropsDestroy(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.FARMLAND) {
            event.setCancelled(true);
        }
    }

}
