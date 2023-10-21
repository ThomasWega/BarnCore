package com.bof.barn.core.region.plot.container.settings;

import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.region.setting.SettingState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

import java.util.List;

/**
 * Whether harvestables put into the container should be automatically sold.
 * Will sell only harvestables put into the silo while the setting is true.
 * Old harvestables won't be sold
 */
public class AutoSellSetting extends ContainerPlotSetting {
    public AutoSellSetting() {
        super("Auto Sell", new ItemBuilder(Material.BARREL)
                .displayName(Component.text("Auto Sell"))
                .lore(List.of(
                        Component.text("Automatically sells harvestables", NamedTextColor.GRAY)
                ))
                .build(), 500, SettingState.LOCKED);
    }
}
