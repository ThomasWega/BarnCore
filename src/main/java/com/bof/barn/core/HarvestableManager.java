package com.bof.barn.core;

import com.bof.barn.core.enviromental.EnchantedRain;
import com.bof.barn.core.region.plot.harvestable.AdditionResult;
import com.bof.barn.core.region.plot.harvestable.HarvestablePlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class HarvestableManager {

    private HarvestableManager() {}

    public static float getBonusAmount() {
        if (EnchantedRain.isRunning) {
            return 0.15f;
        }
        return 0;
    }

    public static int getBonusDrops(@NotNull Collection<?> harvestables) {
        float bonusAmount = harvestables.size() * getBonusAmount();
        return Math.round(bonusAmount);
    }

    public static int handleBonusDrops(@NotNull HarvestablePlot<?> plot, @NotNull Collection<?> harvestables) {
        return handleBonusDrops(plot, getBonusDrops(harvestables));
    }

    public static int handleBonusDrops(@NotNull HarvestablePlot<?> plot, int bonusDropAmount) {
        int bonusCount = 0;
        for (int i = 0; i < bonusDropAmount; i++) {
            AdditionResult result = plot.handleAddition(new ItemStack(plot.getCurrentlyHarvesting().getItem()));
            if (result == AdditionResult.SUCCESS) {
                bonusCount++;
            }
        }
        return bonusCount;
    }
}
