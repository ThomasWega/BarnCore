package com.bof.core.menu.premade.page;

import com.bof.core.item.SkullBuilder;
import com.bof.toolkit.skin.Skin;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class NextPageItem extends GuiItem {

    public NextPageItem() {
        super(new SkullBuilder()
                .displayName(Component.text("Next page", NamedTextColor.GRAY))
                .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDRiZThhZWVjMTE4NDk2OTdhZGM2ZmQxZjE4OWIxNjY0MmRmZjE5ZjI5NTVjMDVkZWFiYTY4YzlkZmYxYmUifX19", null))
                .build()
        );
    }
}
