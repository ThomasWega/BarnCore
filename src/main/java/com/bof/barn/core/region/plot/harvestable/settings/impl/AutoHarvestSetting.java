package com.bof.barn.core.region.plot.harvestable.settings.impl;

import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.event.setting.PlotSettingLevelIncreaseEvent;
import com.bof.barn.core.region.plot.harvestable.settings.HarvestablePlotSetting;
import com.bof.barn.core.region.setting.LeveledSetting;
import com.bof.barn.core.region.setting.SettingState;
import com.bof.barn.core.region.setting.TimerSetting;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.List;

/**
 * Whether harvestables should be auto harvested every given period
 */
@Getter
public class AutoHarvestSetting extends HarvestablePlotSetting implements LeveledSetting, TimerSetting {
    private final int maxLevel = 5;
    private final float basePrice = this.getPrice();
    private long currentTickSpeed = 200;
    private int currentLevel = 1;

    public AutoHarvestSetting() {
        super("Auto Harvest", new ItemBuilder(Material.SHEARS)
                .displayName(Component.text("Auto Harvest"))
                .lore(List.of(
                        Component.text("Automatically harvests crops/animals", NamedTextColor.GRAY)
                ))
                .build(), 500, SettingState.LOCKED);
    }

    @Override
    public boolean upgradeLevel(AbstractPlot plot) {
        if (this.isAtMaxLevel()) return false;

        currentLevel++;
        currentTickSpeed -= 20;
        Bukkit.getPluginManager().callEvent(new PlotSettingLevelIncreaseEvent(plot, this));
        return true;
    }
}
