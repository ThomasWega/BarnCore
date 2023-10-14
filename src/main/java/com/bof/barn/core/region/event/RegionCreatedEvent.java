package com.bof.barn.core.region.event;

import com.bof.barn.core.region.BarnRegion;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when region is created
 */
@Getter
@RequiredArgsConstructor
public class RegionCreatedEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final BarnRegion region;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
