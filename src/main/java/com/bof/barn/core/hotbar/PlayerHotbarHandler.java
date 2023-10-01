package com.bof.barn.core.hotbar;

import com.bof.barn.core.Core;
import com.bof.barn.core.hotbar.impl.MainHotbarItem;
import com.bof.barn.core.region.event.RegionAssignedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * Handles adding the hotbar items to the player
 */
public class PlayerHotbarHandler implements Listener {
    private final Core core;

    public PlayerHotbarHandler(@NotNull Core core) {
        this.core = core;
    }

    @EventHandler
    private void onRegionAssign(RegionAssignedEvent event) {
        Player player = event.getPlayer();
        new MainHotbarItem(core).setToPlayer(player, 8);
    }
}
