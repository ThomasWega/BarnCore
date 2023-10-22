package com.bof.barn.core.region.plot.harvestable.settings.impl;

import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.harvestable.settings.HarvestablePlotSetting;
import com.bof.barn.core.region.setting.SettingState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Whether harvested harvestables should go straight to the container
 */
public class AutoStoreSetting extends HarvestablePlotSetting {
    public AutoStoreSetting() {
        super("Auto Store", new ItemBuilder(Material.BARREL)
                .displayName(Component.text("Auto Store", Style.style(NamedTextColor.YELLOW, TextDecoration.BOLD)))
                .lore(List.of(
                        Component.text("Automatically stores harvestables", NamedTextColor.GRAY)
                ))
                .build(), 500, SettingState.LOCKED);
    }

    @Override
    public void upgradeAction(@NotNull AbstractPlot plot) {
    }
}
