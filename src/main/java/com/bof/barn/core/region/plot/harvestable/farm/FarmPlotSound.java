package com.bof.barn.core.region.plot.harvestable.farm;

import com.bof.barn.core.sound.BoFSound;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.sound.Sound;

/**
 * Premade sounds for {@link FarmPlot}
 */
@Getter
@RequiredArgsConstructor
public enum FarmPlotSound implements BoFSound {
    CROP(Sound.sound(
            org.bukkit.Sound.BLOCK_GROWING_PLANT_CROP.key(),
            Sound.Source.AMBIENT, 1f, 1f)
    );

    private final Sound sound;
}
