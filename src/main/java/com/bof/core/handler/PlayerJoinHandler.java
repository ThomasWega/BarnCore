package com.bof.core.handler;

import com.bof.core.plots.PlotType;
import com.bof.core.plots.crops.CropsType;
import com.bof.core.plots.crops.FarmPlot;
import com.bof.core.region.RegionManager;
import com.bof.core.utils.BoxUtils;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
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
        BoxUtils.identifyPlots(PlotType.FARM, regionManager.getRegionOf(event.getPlayer()).get().getBox()).forEach(box -> {
            new FarmPlot(box).changeCrops(CropsType.CARROT);
        });
    }
}
