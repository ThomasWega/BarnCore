package com.bof.barn.core.region.events;

import com.bof.barn.core.region.BarnRegion;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when region is assigned to a player
 */
@Getter
@RequiredArgsConstructor
public class RegionAssignedEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final BarnRegion region;
    private final Player player;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
