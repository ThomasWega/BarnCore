package com.bof.barn.core.utils;

import com.bof.barn.core.region.plot.harvestable.animal.AnimalType;
import com.bof.barn.core.region.plot.harvestable.farm.CropType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class HarvestableUtils {

    private HarvestableUtils() {
    }


    /**
     * @param crops ItemStacks which are crops
     * @return total value of all the crops
     */
    public static float getValueOfCrops(@NotNull Collection<ItemStack> crops) {
        return (float) crops.stream()
                .map(itemStack -> CropType.getByItemMaterial(itemStack.getType()).orElse(CropType.NONE))
                .mapToDouble(CropType::getValue)
                .sum();
    }

    /**
     * @param crops ItemStacks which are animal items
     * @return total value of all the animals
     */
    public static float getValueOfAnimals(@NotNull Collection<ItemStack> crops) {
        return (float) crops.stream()
                .map(itemStack -> AnimalType.getByItemMaterial(itemStack.getType()).orElse(AnimalType.NONE))
                .mapToDouble(AnimalType::getValue)
                .sum();
    }
}
