package com.bof.barn.core.gui.premade.sound;

import com.bof.barn.core.sound.BoFSound;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.sound.Sound;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public enum ButtonSound implements BoFSound {
    PEEP(Sound.sound(
            org.bukkit.Sound.BLOCK_NOTE_BLOCK_BIT.key(),
            Sound.Source.AMBIENT, 1f, 1f)
    );

    private final @NotNull Sound sound;
}
