package com.bof.barn.core.region.plot.harvestable.settings.impl;

import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.harvestable.settings.HarvestablePlotSetting;
import com.bof.barn.core.region.setting.ChanceSetting;
import com.bof.barn.core.region.setting.SettingState;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Whether harvestables should be replanted after being harvested with a button
 */
@Getter
public class ReplantAllSetting extends HarvestablePlotSetting implements ChanceSetting {
    private float currentChance = 0.1f;

    public ReplantAllSetting() {
        super("Replant All", new ItemBuilder(Material.BONE_MEAL)
                .displayName(Component.text("Replant All", Style.style(NamedTextColor.YELLOW, TextDecoration.BOLD)))
                .lore(List.of(
                        Component.text("Automatically replants all harvestables", NamedTextColor.GRAY),
                        Component.text("when harvesting all at once", NamedTextColor.GRAY)
                ))
                .build(), 500, SettingState.LOCKED, 3);
    }


    @Override
    public void upgradeAction(@NotNull AbstractPlot plot) {
        this.currentChance += 0.1f;
    }
}
