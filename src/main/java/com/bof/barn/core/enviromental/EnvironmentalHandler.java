package com.bof.barn.core.enviromental;

import com.bof.barn.core.Core;
import com.bof.toolkit.utils.ColorUtils;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import static com.bof.barn.core.Core.WORLD;


public class EnvironmentalHandler {
    private final Core core;
    public final EnchantedRain enchantedRain = new EnchantedRain();

    public EnvironmentalHandler(@NotNull Core core) {
        this.core = core;
        this.handle();
    }

    private void handle() {
        Bukkit.getScheduler().runTaskTimer(this.core, () -> {
            if (this.enchantedRain.shouldStartEnchantedRain()) {
                if (this.enchantedRain.start()) {
                    this.handleRainStart();
                }
                return;
            }

            if (this.enchantedRain.stop()) {
                this.handleRainStop();
            }
        }, 300 * 20, // start checking after first 5 mins
                600 * 20); // check every 10 mins
    }

    private void handleRainStart() {
        Component title = ColorUtils.color("&#ff0000&l&oE&#ff2a00&l&on&#ff5500&l&oc&#ff7f00&l&oh&#ffaa00&l&oa&#ffd400&l&on&#ffff00&l&ot&#aaff00&l&oe&#55ff00&l&od &#00ff00&l&oR&#00aa55&l&oa&#0055aa&l&oi&#0000ff&l&on");
        WORLD.showTitle(Title.title(
                title,
                Component.text("Drops are multiplied", NamedTextColor.GRAY)
        ));
        WORLD.playSound(Sound.sound(
                org.bukkit.Sound.ENTITY_LIGHTNING_BOLT_THUNDER.key(),
                Sound.Source.AMBIENT,
                1f, 1f
        ));
    }

    private void handleRainStop() {

    }
}
