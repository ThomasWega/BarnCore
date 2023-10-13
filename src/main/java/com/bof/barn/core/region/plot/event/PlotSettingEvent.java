package com.bof.barn.core.region.plot.event;

import com.bof.barn.core.region.plot.Plot;
import com.bof.barn.core.region.plot.PlotSetting;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class PlotSettingEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Plot plot;
    private final PlotSetting plotSetting;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
