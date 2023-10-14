package com.bof.barn.core.enviromental;

import com.bof.barn.core.Core;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import static com.bof.barn.core.Core.WORLD;

/**
 * Handles different environment events and their occurrence
 */
public class EnvironmentalHandler {
    public final EnchantedRain enchantedRain = new EnchantedRain();
    private final Core plugin;

    public EnvironmentalHandler(@NotNull Core plugin) {
        this.plugin = plugin;
        this.handle();
    }

    /**
     * Checks every N time and tries the chance.
     * If it is selected that it should rain, it will try starting the rain.
     * If the rain was successfully started (wasn't already started), it will handle the rain start.
     * If it's not, it will try to stop the rain. If the rain is successfully stopped (it was raining before), it will handle the rain stop
     */
    private void handle() {
        Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {
                    if (this.enchantedRain.shouldStartEnchantedRain()) {
                        if (this.enchantedRain.start()) {
                            this.handleRainStart();
                        }
                        return;
                    }

                    if (this.enchantedRain.stop()) {
                        this.handleRainStop();
                    }
                }, 20, //300 * 20, // start checking after first 5 mins
                600 * 20); // check every 10 mins
    }

    /**
     * Sends a title message and plays a sound for all players
     */
    private void handleRainStart() {
        WORLD.showTitle(Title.title(
                enchantedRain.getTitle(),
                Component.text("Drops are now multiplied (" + EnchantedRain.multiplier + "x)", NamedTextColor.GRAY)
        ));
        WORLD.playSound(Sound.sound(
                org.bukkit.Sound.ENTITY_LIGHTNING_BOLT_THUNDER.key(),
                Sound.Source.AMBIENT,
                1f, 1f
        ));
    }

    /**
     * Sends a title message and plays a sound for all players
     */
    private void handleRainStop() {
        WORLD.showTitle(Title.title(
                enchantedRain.getTitle(),
                Component.text("has ended", NamedTextColor.GRAY)
        ));
        // TODO don't know what sound to play yet
        // WORLD.playSound();
    }
}
