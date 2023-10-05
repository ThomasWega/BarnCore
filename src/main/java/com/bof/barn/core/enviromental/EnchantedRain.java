package com.bof.barn.core.enviromental;

import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Random;

import static com.bof.barn.core.Core.WORLD;

/**
 * Enchanted Rain boosts the drops of harvestables (doubles)
 * Handles the starting and stopping.
 */
@Data
public class EnchantedRain {
    private final Random random = new Random();
    public static boolean isRunning = false;
    private final Component title = MiniMessage.miniMessage().deserialize("<rainbow:!>Enchanted Rain</rainbow>");

    public boolean shouldStartEnchantedRain() {
       return this.random.nextDouble() <= 1; //0.075;
    }

    public boolean start() {
        if (isRunning) return false;

        WORLD.setStorm(true);
        isRunning = true;

        return true;
    }

    public boolean stop() {
        if (isRunning) {
            WORLD.setStorm(false);
            isRunning = false;
            return true;
        }
        return false;
    }
}
