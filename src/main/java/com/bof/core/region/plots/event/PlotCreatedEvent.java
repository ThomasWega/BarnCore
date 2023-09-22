package com.bof.core.region.plots.event;

import com.bof.core.region.plots.Plot;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class PlotCreatedEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Plot plot;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
