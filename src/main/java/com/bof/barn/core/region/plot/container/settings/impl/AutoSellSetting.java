package com.bof.barn.core.region.plot.container.settings.impl;

import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.container.settings.ContainerPlotSetting;
import com.bof.barn.core.region.setting.SettingState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Whether harvestables put into the container should be automatically sold.
 * Will sell only harvestables put into the silo while the setting is true.
 * Old harvestables won't be sold
 */
public class AutoSellSetting extends ContainerPlotSetting {
    public AutoSellSetting() {
        super("Auto Sell", new ItemBuilder(Material.BARREL)
                .displayName(Component.text("Auto Sell", Style.style(NamedTextColor.YELLOW, TextDecoration.BOLD)))
                .lore(List.of(
                        Component.text("Automatically sells harvestables", NamedTextColor.GRAY)
                ))
                .build(), 500, SettingState.LOCKED);
    }

    @Override
    public void upgradeAction(@NotNull AbstractPlot plot) {

    }
}
