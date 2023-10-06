package com.bof.barn.core.gui.premade.button.slot;

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

public class UnlockedSlotButton extends GuiItem {

    public UnlockedSlotButton(@Nullable Consumer<InventoryClickEvent> action) {
        super(new SkullBuilder()
                .displayName(MiniMessage.miniMessage().deserialize("<color:#42FF49>Unlocked Slot</color>"))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Click to select plot", NamedTextColor.DARK_GRAY)
                ))
                .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM4ZWExZjUxZjI1M2ZmNTE0MmNhMTFhZTQ1MTkzYTRhZDhjM2FiNWU5YzZlZWM4YmE3YTRmY2I3YmFjNDAifX19", null))
                .build(), action
        );
    }
}
