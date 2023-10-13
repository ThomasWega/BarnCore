package com.bof.barn.core.region.plot.harvestable.settings;

import com.bof.barn.core.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

import java.util.List;

/**
 * Whether harvested harvestables should go straight to the container
 */
public class AutoStoreSetting extends HarvestablePlotSetting {
    public AutoStoreSetting() {
        super("Auto Store", new ItemBuilder(Material.BARREL)
                .displayName(Component.text("Auto Store"))
                .lore(List.of(
                        Component.text("Automatically stores harvestables", NamedTextColor.GRAY)
                ))
                .build(), false
        );
    }
}
