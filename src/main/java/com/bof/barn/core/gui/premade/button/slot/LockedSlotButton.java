package com.bof.barn.core.gui.premade.button.slot;

import com.bof.barn.core.gui.premade.sound.ButtonSound;
import com.bof.barn.core.gui.premade.sound.SoundedGUIButton;
import com.bof.barn.core.item.SkullBuilder;
import com.bof.toolkit.skin.Skin;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class LockedSlotButton extends SoundedGUIButton {
    public LockedSlotButton() {
        super(new SkullBuilder()
                        .displayName(MiniMessage.miniMessage().deserialize("<color:#38243b>Locked Slot</color>"))
                        .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjcwNWZkOTRhMGM0MzE5MjdmYjRlNjM5YjBmY2ZiNDk3MTdlNDEyMjg1YTAyYjQzOWUwMTEyZGEyMmIyZTJlYyJ9fX0=", null))
                        .build(),
                event -> event.setCancelled(true), ButtonSound.PEEP
        );
    }
}
