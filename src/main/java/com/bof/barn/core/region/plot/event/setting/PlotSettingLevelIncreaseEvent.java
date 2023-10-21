package com.bof.barn.core.region.plot.event.setting;

import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.setting.PlotSetting;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when level of {@link com.bof.barn.core.region.setting.LeveledSetting} increases
 */
@Getter
@RequiredArgsConstructor
public class PlotSettingLevelIncreaseEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final @NotNull AbstractPlot plot;
    private final @NotNull PlotSetting plotSetting;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
