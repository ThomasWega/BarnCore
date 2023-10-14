package com.bof.barn.core.gui.premade.button.plot;

import com.bof.barn.core.gui.premade.sound.SoundedGUIButton;
import com.bof.barn.core.item.SkullBuilder;
import com.bof.toolkit.skin.Skin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class UpgradesButton extends SoundedGUIButton {
    public UpgradesButton(@Nullable Consumer<InventoryClickEvent> action) {
        super (new SkullBuilder()
                .displayName(MiniMessage.miniMessage().deserialize("<b><color:#4FFFD3>Upgrades</color></b>"))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Click to open upgrades", NamedTextColor.DARK_GRAY)
                ))
                .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmI0OWEyY2I5MDczNzk5MzIwMWZlNzJhMWYxYWI3NWM1YzkzYzI4ZjA0N2Y2ODVmZmFkNWFiMjBjN2IwY2FmMCJ9fX0=", null))
                .build(), action
        );
    }
}
