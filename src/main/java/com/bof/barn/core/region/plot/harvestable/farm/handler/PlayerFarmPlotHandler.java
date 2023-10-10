package com.bof.barn.core.region.plot.harvestable.farm.handler;

import com.bof.barn.core.player.GamePlayer;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.farm.FarmPlot;
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

        event.setCancelled(true);

        gamePlayer.getBarnRegion().ifPresent(region -> region.getPlots().get(PlotType.FARM).stream()
                .map(plot -> ((FarmPlot) plot))
                .forEach(plot -> {
                    if (!plot.getBoxBlocks().contains(block)) return;
                    event.setDropItems(false);

                    if (plot.handleCropBreak(player, true, block) == 0) {
                        player.sendMessage("TO ADD - Crops inventory is full 3");
                        return;
                    }

                    event.setCancelled(false);
                })
        );
    }
}
