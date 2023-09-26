package com.bof.core.utils;

import com.bof.core.region.plot.animal.AnimalType;
import com.bof.core.region.plot.farm.CropType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class HarvestableUtils {

    private HarvestableUtils() {}

    public static float getValueOfCrops(@NotNull Collection<ItemStack> crops) {
        // should never happen, as the only items which are sold should be valid items
        //noinspection OptionalGetWithoutIsPresent
        return (float) crops.stream()
                .map(itemStack -> CropType.getByMaterial(itemStack.getType()).get())
                .mapToDouble(CropType::getValue)
                .sum();
    }

    public static float getValueOfAnimals(@NotNull Collection<ItemStack> crops) {
        // should never happen, as the only items which are sold should be valid items
        //noinspection OptionalGetWithoutIsPresent
        return (float) crops.stream()
                .map(itemStack -> AnimalType.getByItem(itemStack).get())
                .mapToDouble(AnimalType::getValue)
                .sum();
    }
}
