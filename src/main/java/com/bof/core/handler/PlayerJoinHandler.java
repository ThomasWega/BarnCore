package com.bof.core.handler;

import com.bof.core.region.RegionManager;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

@RequiredArgsConstructor
public class PlayerJoinHandler implements Listener {
    private final RegionManager regionManager;

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onPrePlayerJoin(AsyncPlayerPreLoginEvent event) {
        if (!regionManager.isFreeRegionAvailable()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, Component.text("TO ADD - All barns are occupied"));
        }
    }
}
