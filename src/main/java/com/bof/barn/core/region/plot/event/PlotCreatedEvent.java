package com.bof.barn.core.region.plot.event;

import com.bof.barn.core.region.plot.AbstractPlot;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a new {@link AbstractPlot} is created
 */
@Getter
@RequiredArgsConstructor
public class PlotCreatedEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final AbstractPlot plot;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
