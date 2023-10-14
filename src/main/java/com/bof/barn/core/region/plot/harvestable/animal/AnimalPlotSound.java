package com.bof.barn.core.region.plot.harvestable.animal;

import com.bof.barn.core.sound.BoFSound;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.sound.Sound;

/**
 * Premade sounds for {@link AnimalPlot}
 */
@Getter
@RequiredArgsConstructor
public enum AnimalPlotSound implements BoFSound {
    SUMMON(Sound.sound(
            org.bukkit.Sound.ENTITY_HORSE_BREATHE.key(),
            Sound.Source.AMBIENT, 1f, 1f)
    );

    private final Sound sound;
}
