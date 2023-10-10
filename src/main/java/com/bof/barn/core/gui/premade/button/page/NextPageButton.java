package com.bof.barn.core.gui.premade.button.page;

import com.bof.barn.core.gui.premade.sound.SoundedGUIButton;
import com.bof.barn.core.item.SkullBuilder;
import com.bof.toolkit.skin.Skin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class NextPageButton extends SoundedGUIButton {

    public NextPageButton(@Nullable Consumer<InventoryClickEvent> action) {
        super(new SkullBuilder()
                .displayName(Component.text("Next page", NamedTextColor.GRAY))
                .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDRiZThhZWVjMTE4NDk2OTdhZGM2ZmQxZjE4OWIxNjY0MmRmZjE5ZjI5NTVjMDVkZWFiYTY4YzlkZmYxYmUifX19", null))
                .build(), action
        );
    }
}
