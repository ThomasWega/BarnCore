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
    public static final float multiplier = 0.15f;
    private final Random random = new Random();
    public static boolean isRunning = false;
    private final Component title = MiniMessage.miniMessage().deserialize("<rainbow:!>Enchanted Rain</rainbow>");

    public boolean shouldStartEnchantedRain() {
       return this.random.nextDouble() <= 1; //0.075;
    }

    /**
     * Start the enchanted rain if not already running
     * @return whether the enchanted rain was started
     */
    public boolean start() {
        if (isRunning) return false;

        WORLD.setStorm(true);
        isRunning = true;

        return true;
    }

    /**
     * Stop the enchanted rain if running
     * @return whether the enchanted rain was stopped
     */
    public boolean stop() {
        if (isRunning) {
            WORLD.setStorm(false);
            isRunning = false;
            return true;
        }
        return false;
    }
}
