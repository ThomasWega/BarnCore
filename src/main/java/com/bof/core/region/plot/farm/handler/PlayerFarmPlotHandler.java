package com.bof.core.region.plot.farm.handler;

import com.bof.core.player.GamePlayer;
import com.bof.core.region.plot.PlotType;
import com.bof.core.region.plot.farm.FarmPlot;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerFarmPlotHandler implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void onPlayerHarvestCrops(BlockBreakEvent event) {
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

                        if (plot.handleCropBreak(player, block) == 0) {
                            player.sendMessage("TO ADD - Crops inventory is full 3");
                            return;
                        }

                        event.setCancelled(false);
                    })
        );
    }
}
