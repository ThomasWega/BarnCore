package com.bof.barn.core.region.handlers;

import com.bof.barn.core.Core;
import com.bof.barn.core.player.GamePlayer;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.RegionManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Calls the assignation and de-assignation of {@link BarnRegion} for the player
 */
public class PlayerRegionAssignHandler implements Listener {
    private final Core plugin;
    private final RegionManager regionManager;

    public PlayerRegionAssignHandler(Core plugin) {
        this.plugin = plugin;
        this.regionManager = plugin.getRegionManager();
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onPrePlayerJoin(AsyncPlayerPreLoginEvent event) {
        if (!regionManager.isFreeRegionAvailable()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, Component.text("TO ADD - All barns are occupied"));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        GamePlayer.cache(plugin, player);
        regionManager.assignRegion(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        regionManager.deAssignRegion(player);
        GamePlayer.unCache(player);
    }

    @EventHandler
    private void test(PlayerChatEvent event) {
        Player player = event.getPlayer();
        BarnRegion region = regionManager.getRegionOf(player).get();
    }
}
