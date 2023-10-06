package com.bof.barn.core.gui.premade.button.page;

import com.bof.barn.core.item.SkullBuilder;
import com.bof.toolkit.skin.Skin;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class PreviousPageButton extends GuiItem {

    public PreviousPageButton() {
        super(new SkullBuilder()
                .displayName(Component.text("Previous page", NamedTextColor.GRAY))
                .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzYyNTkwMmIzODllZDZjMTQ3NTc0ZTQyMmRhOGY4ZjM2MWM4ZWI1N2U3NjMxNjc2YTcyNzc3ZTdiMWQifX19", null))
                .build()
        );
    }
}
