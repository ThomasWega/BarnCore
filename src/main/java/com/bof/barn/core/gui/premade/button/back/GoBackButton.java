package com.bof.barn.core.gui.premade.button.back;

import com.bof.barn.core.gui.premade.sound.SoundedGUIButton;
import com.bof.barn.core.item.SkullBuilder;
import com.bof.toolkit.skin.Skin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class GoBackButton extends SoundedGUIButton {
    public GoBackButton(@Nullable Consumer<InventoryClickEvent> action) {
        super(new SkullBuilder()
                .displayName(Component.text("Go back", NamedTextColor.GRAY))
                .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzUxY2VkMmU2NDczNjZmOGYzYWQyZGZlNDE1Y2NhODU2NTFiZmFmOTczOWE5NWNkNTdiNmYyMWNiYTA1MyJ9fX0=", null))
                .build(), action
        );
    }
}
