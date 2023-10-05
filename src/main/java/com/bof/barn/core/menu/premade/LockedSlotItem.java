package com.bof.barn.core.menu.premade;

import com.bof.barn.core.item.SkullBuilder;
import com.bof.toolkit.skin.Skin;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class LockedSlotItem extends GuiItem {
    public LockedSlotItem() {
        super(new SkullBuilder()
                        .displayName(MiniMessage.miniMessage().deserialize("<color:#38243b>Locked Slot</color>"))
                        .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjcwNWZkOTRhMGM0MzE5MjdmYjRlNjM5YjBmY2ZiNDk3MTdlNDEyMjg1YTAyYjQzOWUwMTEyZGEyMmIyZTJlYyJ9fX0=", null))
                        .build(),
                event -> event.setCancelled(true)
        );
    }
}
