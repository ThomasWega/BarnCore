package com.bof.barn.core.region.plot.harvestable.setting;

import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.event.setting.PlotSettingLevelIncreaseEvent;
import com.bof.barn.core.region.setting.LeveledSetting;
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
public class AutoHarvestSetting extends HarvestablePlotSetting implements LeveledSetting {
    private final int maxLevel = 5;
    private final float basePrice = this.getPrice();
    private long tickSpeed = 200;
    private int currentLevel = 1;

    public AutoHarvestSetting() {
        super("Auto Harvest", new ItemBuilder(Material.SHEARS)
                .displayName(Component.text("Auto Harvest"))
                .lore(List.of(
                        Component.text("Automatically harvests crops/animals", NamedTextColor.GRAY)
                ))
                .build(), 500, false);
    }

    @Override
    public boolean upgradeLevel(AbstractPlot plot) {
        if (this.isAtMaxLevel()) return false;

        currentLevel++;
        tickSpeed -= 20;
        Bukkit.getPluginManager().callEvent(new PlotSettingLevelIncreaseEvent(plot, this));
        return true;
    }
}
