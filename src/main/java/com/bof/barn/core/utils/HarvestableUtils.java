package com.bof.barn.core.utils;

import com.bof.barn.core.HarvestableManager;
import com.bof.barn.core.region.plot.harvestable.HarvestableType;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalType;
import com.bof.barn.core.region.plot.harvestable.farm.CropType;
import de.tr7zw.changeme.nbtapi.NBT;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class HarvestableUtils {

    private HarvestableUtils() {
    }


    /**
     * @param items ItemStacks which are harvestables
     * @return total value of all the harvestables
     */
    public static float getValue(@NotNull HarvestableType type, @NotNull Collection<ItemStack> items) {
        return (float) items.stream()
                .mapToDouble(itemStack -> getModifiedValue(type, itemStack))
                .sum();
    }

    /**
     * @param crops ItemStacks which are crops
     * @return total value of all the crops
     */
    public static float getValueOfCrops(@NotNull Collection<ItemStack> crops) {
        return (float) crops.stream()
                .mapToDouble(itemStack -> {
                    CropType cropType = CropType.getByItemMaterial(itemStack.getType()).orElse(CropType.NONE);
                    return getModifiedValue(cropType, itemStack);
                })
                .sum();
    }

    /**
     * @param crops ItemStacks which are animal items
     * @return total value of all the animals
     */
    public static float getValueOfAnimals(@NotNull Collection<ItemStack> crops) {
        return (float) crops.stream()
                .mapToDouble(itemStack -> {
                    AnimalType type = AnimalType.getByItemMaterial(itemStack.getType()).orElse(AnimalType.NONE);
                    return getModifiedValue(type, itemStack);
                })
                .sum();
    }

    public static float getModifiedValue(@NotNull HarvestableType type, @NotNull ItemStack itemStack) {
        float value = type.getValue();
        if (NBT.readNbt(itemStack).hasTag("barn-enchanted-harvestable")) {
            value *= HarvestableManager.getEnchantedHarvestableMultiplier();
        }
        return value;
    }

    public static @NotNull TextComponent getModifiedDisplayName(@NotNull HarvestableType type, @NotNull ItemStack itemStack) {
        TextComponent amount = Component.text(itemStack.getAmount() + "x ", NamedTextColor.GRAY);
        TextComponent nonNumberedFullName = type.getDisplayName();
        if (NBT.readNbt(itemStack).hasTag("barn-enchanted-harvestable")) {
            nonNumberedFullName = nonNumberedFullName.content("Enchanted " + nonNumberedFullName.content());
        }
        return amount.append(nonNumberedFullName);
    }
}
