package com.bof.core.hotbar;

import com.bof.core.Core;
import com.bof.core.hotbar.impl.MainHotbarItem;
import com.bof.core.region.event.RegionAssignedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

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
