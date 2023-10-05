package com.bof.barn.core.menu.premade;

import com.bof.barn.core.item.SkullBuilder;
import com.bof.toolkit.skin.Skin;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class UnlockedSlotItem extends GuiItem {

    public UnlockedSlotItem(@Nullable Consumer<InventoryClickEvent> action) {
        super(new SkullBuilder()
                .displayName(MiniMessage.miniMessage().deserialize("<color:#42FF49>Unlocked Slot</color>"))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Click to select plot", NamedTextColor.DARK_GRAY)
                ))
                .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjM3MGNhNDdiNjE3M2FiNThlNmA4MDE4NDg6ZTJmOGJhYTgzOTdhYjYxNGFlMmU2OTY4NDkxOTZiYWE3Yzp7InJ0b21kIjoiIn19fQ==", null))
                .build(), action
        );
    }
}
