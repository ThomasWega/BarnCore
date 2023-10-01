package com.bof.barn.core.enviromental;

import java.util.Random;

import static com.bof.barn.core.Core.WORLD;

public class EnchantedRain {
    private final Random random = new Random();
    public static boolean isRunning = false;

    public boolean shouldStartEnchantedRain() {
       return this.random.nextDouble() <= 0.075;
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
