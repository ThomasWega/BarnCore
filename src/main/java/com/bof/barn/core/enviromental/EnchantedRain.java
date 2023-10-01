package com.bof.barn.core.enviromental;

import lombok.Data;

import java.util.Random;

import static com.bof.barn.core.Core.WORLD;

@Data
public class EnchantedRain {
    private final Random random = new Random();
    private boolean isRunning = false;

    public boolean shouldStartEnchantedRain() {
       return this.random.nextDouble() <= 0.075;
    }

    public boolean start() {
        if (this.isRunning) return false;

        WORLD.setStorm(true);
        this.isRunning = true;

        return true;
    }

    public boolean stop() {
        if (this.isRunning) {
            WORLD.setStorm(false);
            this.isRunning = false;
            return true;
        }
        return false;
    }
}
