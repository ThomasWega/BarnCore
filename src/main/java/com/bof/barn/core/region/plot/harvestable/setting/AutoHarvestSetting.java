package com.bof.barn.core.region.plot.harvestable.setting;

import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.region.setting.LeveledSetting;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

import java.util.List;

/**
 * Whether harvestables should be auto harvested every given period
 */
@Getter
public class AutoHarvestSetting extends HarvestablePlotSetting implements LeveledSetting {
    private long tickSpeed = 200;
    private int currentLevel = 1;
    private final int maxLevel = 5;
    private final float basePrice = this.getPrice();

    public AutoHarvestSetting() {
        super("Auto Harvest", new ItemBuilder(Material.SHEARS)
                .displayName(Component.text("Auto Harvest"))
                .lore(List.of(
                        Component.text("Automatically harvests crops/animals", NamedTextColor.GRAY)
                ))
                .build(), 500, false);
    }

    @Override
    public boolean upgradeLevel() {
        if (this.isAtMaxLevel()) return false;

        currentLevel++;
        tickSpeed -= 20;
        return true;
    }
}
