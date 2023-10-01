package com.bof.core.enviromental;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Random;

@Data
public class EnchantedRain {
    private final Random random = new Random();
    private boolean isRunning = false;
    private final World world = Bukkit.getWorld("world");

    public boolean shouldStartEnchantedRain() {
        return this.random.nextDouble() <= 0.05;
    }

    public boolean start() {
        if (this.isRunning) return false;

        this.world.setStorm(true);
        this.isRunning = true;

        return true;
    }

    public void stop() {
        if (this.isRunning) {
            this.world.setStorm(false);
            this.isRunning = false;
        }
    }
}
