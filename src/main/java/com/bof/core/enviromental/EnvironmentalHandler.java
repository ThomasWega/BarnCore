package com.bof.core.enviromental;

import com.bof.core.Core;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;


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
                this.enchantedRain.start();
                System.out.println("START RAINING");
                return;
            }

            System.out.println("STOP RAINING");
            this.enchantedRain.stop();
        }, 1, 600 * 20); // every 10 mins
    }
}
