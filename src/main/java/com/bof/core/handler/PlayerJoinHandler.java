package com.bof.core.handler;

import com.bof.core.plots.PlotType;
import com.bof.core.plots.crops.FarmPlot;
import com.bof.core.plots.crops.menu.CropsMainMenu;
import com.bof.core.plots.crops.menu.FarmChangeCropsMenu;
import com.bof.core.region.BarnRegion;
import com.bof.core.region.RegionManager;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class PlayerJoinHandler implements Listener {
    private final RegionManager regionManager;

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onPrePlayerJoin(AsyncPlayerPreLoginEvent event) {
        if (!regionManager.isFreeRegionAvailable()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, Component.text("TO ADD - All barns are occupied"));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onPlayerJoin(PlayerJoinEvent event) {
        regionManager.assignRegion(event.getPlayer());
    }

    @EventHandler
    private void test(PlayerChatEvent event) {
        Player player = event.getPlayer();
        BarnRegion region = regionManager.getRegionOf(player).get();
        // new FarmChangeCropsMenu(((FarmPlot) region.getPlots().get(PlotType.FARM).toArray()[0])).show(player);
        new CropsMainMenu(region).show(player);
    }
}
