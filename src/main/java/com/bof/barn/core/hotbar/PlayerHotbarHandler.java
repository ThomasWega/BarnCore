package com.bof.barn.core.hotbar;

import com.bof.barn.core.Core;
import com.bof.barn.core.hotbar.impl.MainHotbarItem;
import com.bof.barn.core.region.events.RegionAssignedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * Handles adding the hotbar items to the player
 */
public class PlayerHotbarHandler implements Listener {
    private final Core plugin;

    public PlayerHotbarHandler(@NotNull Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onRegionAssign(RegionAssignedEvent event) {
        Player player = event.getPlayer();
        new MainHotbarItem(plugin).setToPlayer(player, 8);
    }
}
