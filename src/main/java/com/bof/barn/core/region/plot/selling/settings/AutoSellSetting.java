package com.bof.barn.core.region.plot.selling.settings;

import com.bof.barn.core.item.ItemBuilder;
import net.kyori.adventure.text.Component;
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
                        Component.text("Automatically sells harvestables")
                ))
                .build(),false);
    }
}
