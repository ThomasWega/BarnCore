package com.bof.barn.core.region.plot.harvestable.settings;

import com.bof.barn.core.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

import java.util.List;

/**
 * Whether harvestables should be replanted after being harvested with a button
 */
public class ReplantAllSetting extends HarvestablePlotSetting {
    public ReplantAllSetting() {
        super("Replant All", new ItemBuilder(Material.BONE_MEAL)
                .displayName(Component.text("Replant All"))
                .lore(List.of(
                        Component.text("Automatically replants all harvestables ", NamedTextColor.GRAY),
                        Component.text("when harvesting all at once", NamedTextColor.GRAY)
                ))
                .build(), false);
    }
}
