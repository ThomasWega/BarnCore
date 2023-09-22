package com.bof.core.region.plots.handler;

import com.bof.core.player.GamePlayer;
import com.bof.core.region.plots.PlotType;
import com.bof.core.region.plots.farm.CropType;
import com.bof.core.region.plots.farm.FarmPlot;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerFarmPlotHandler implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void onPlayerHarvest(BlockBreakEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = GamePlayer.get(player);
        Block block = event.getBlock();
        Material blockType = block.getType();

        event.setCancelled(true);

        gamePlayer.getBarnRegion().ifPresent(region -> region.getPlots().get(PlotType.FARM).stream()
                    .map(plot -> ((FarmPlot) plot))
                    .forEach(plot -> {
                        if (!plot.getBoxBlocks().contains(block)) return;
                        event.setDropItems(false);

                        // the crops inv is full
                        if (!region.addCrops(new ItemStack(blockType))) {
                            player.sendMessage("TO ADD - Crops inventory is full. Put the items in the silo first");
                            return;
                        }

                        if (plot.getRemainingCrops() <= 1) {
                            plot.setCurrentCrop(CropType.NONE);
                        }

                        event.setCancelled(false);
                    })
        );
    }
}
