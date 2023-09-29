package com.bof.core.menu.premade.back;

import com.bof.core.item.SkullBuilder;
import com.bof.toolkit.skin.Skin;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class GoBackItem extends GuiItem {
    public GoBackItem() {
        super(new SkullBuilder()
                .displayName(Component.text("Go back", NamedTextColor.GRAY))
                .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzUxY2VkMmU2NDczNjZmOGYzYWQyZGZlNDE1Y2NhODU2NTFiZmFmOTczOWE5NWNkNTdiNmYyMWNiYTA1MyJ9fX0=", null))
                .build()
        );
    }
}
