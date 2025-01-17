package com.bof.barn.core;

import com.bof.barn.core.enviromental.EnchantedRain;
import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.region.plot.harvestable.AbstractHarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.AdditionResult;
import com.bof.barn.core.region.plot.harvestable.HarvestableType;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Random;

/**
 * Handles the drops including bonus drops and multipliers
 */
public class HarvestableManager {

    private static final Random random = new Random();

    private HarvestableManager() {
    }

    /**
     * Calculates the drop. Handles chances for special drops
     *
     * @param type Type of harvestable
     * @return Drop from the harvestable
     */
    public static ItemStack getDrop(@NotNull HarvestableType type) {
        ItemStack clone = type.getItem().clone();

        // TODO change to 0.001 (1 in 1000)
        if (random.nextDouble() <= 0.5) {
            clone = new ItemBuilder(clone)
                    .addEnchantment(Enchantment.DURABILITY, 1)
                    .hideFlags()
                    .build();

            NBT.modify(clone, nbt -> {
                nbt.setBoolean("barn-enchanted-harvestable", true);
            });
        }

        return clone;
    }

    /**
     * @return Multiplier applied to enchanted harvestables
     */
    public static float getEnchantedHarvestableMultiplier() {
        return 8;
    }


    /**
     * @return Bonus multiplier or 0 if none is applied
     */
    public static float getBonusMultiplier() {
        if (EnchantedRain.isRunning) {
            return EnchantedRain.multiplier;
        }
        return 0;
    }

    /**
     * @param harvestables Items to calculate the bonus drops for
     * @return amount of bonus drops these items should give
     */
    public static int getBonusDrops(@NotNull Collection<?> harvestables) {
        float bonusAmount = harvestables.size() * getBonusMultiplier();
        return Math.round(bonusAmount);
    }

    /**
     * @param plot         Plot the items were harvested on
     * @param harvestables Items to handle the bonus drops for (if any bonus is applied)
     * @return amount of bonus drops handled
     * @see #handleBonusDrops(AbstractHarvestablePlot, int)
     */
    public static int handleBonusDrops(@NotNull AbstractHarvestablePlot<?> plot, @NotNull Collection<?> harvestables) {
        return handleBonusDrops(plot, getBonusDrops(harvestables));
    }

    /**
     * @param plot            Plot the items were harvested on
     * @param bonusDropAmount Amount of bonus drops to give
     * @return Amount of bonus drops actually given
     * @see #handleBonusDrops(AbstractHarvestablePlot, Collection)
     */
    public static int handleBonusDrops(@NotNull AbstractHarvestablePlot<?> plot, int bonusDropAmount) {
        int bonusCount = 0;
        for (int i = 0; i < bonusDropAmount; i++) {
            ItemStack itemStack = new ItemStack(plot.getCurrentlyHarvesting().getItem());
            AdditionResult result = plot.handleAddition(itemStack);
            if (result == AdditionResult.SUCCESS) {
                bonusCount++;
            }
        }
        return bonusCount;
    }
}
