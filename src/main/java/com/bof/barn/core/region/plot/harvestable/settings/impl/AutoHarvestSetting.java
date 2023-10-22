package com.bof.barn.core.region.plot.harvestable.settings.impl;

import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.harvestable.settings.HarvestablePlotSetting;
import com.bof.barn.core.region.setting.SettingState;
import com.bof.barn.core.region.setting.TimerSetting;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Whether harvestables should be auto harvested every given period
 */
@Getter
public class AutoHarvestSetting extends HarvestablePlotSetting implements TimerSetting {
    private long currentTickSpeed = 200;

    public AutoHarvestSetting() {
        super("Auto Harvest", new ItemBuilder(Material.SHEARS)
                .displayName(Component.text("Auto Harvest"))
                .lore(List.of(
                        Component.text("Automatically harvests crops/animals", NamedTextColor.GRAY)
                ))
                .build(), 500, SettingState.LOCKED, 5);
    }

    @Override
    public void upgradeAction(@NotNull AbstractPlot plot) {
        this.currentTickSpeed -= 20;
    }
}
