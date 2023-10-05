package com.bof.barn.core.utils;

import com.bof.barn.core.HarvestableManager;
import com.bof.barn.core.region.plot.harvestable.HarvestableType;
import de.tr7zw.changeme.nbtapi.NBT;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

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
     * Calculates the total value of a collection of crops based on their type and item properties.
     *
     * @param <T>    The type of crops that can be harvested
     * @param type   The Class representing the specific type of crops to calculate the value for
     * @param harvestables  ItemStacks which are harvestables
     * @return       The total value of the harvestables
     * @throws       NoSuchElementException if no matching crop type is found in the 'type' enum constants
     */
    public static <T extends HarvestableType> float getValue(@NotNull Class<T> type, @NotNull Collection<ItemStack> harvestables) {
        return (float) harvestables.stream()
                .mapToDouble(itemStack -> {
                    Optional<T> optType = Arrays.stream(type.getEnumConstants())
                            .filter(t -> t.getItem().getType() == itemStack.getType())
                            .findAny();
                    T realType = optType.orElseThrow();
                    return getModifiedValue(realType, itemStack);
                })
                .sum();
    }

    /**
     * Calculates the value of the harvestable. Includes checking for upgrades, enchanted harvestables etc...
     *
     * @param type Type of the harvestable
     * @param itemStack ItemStack that is a harvestable
     * @return Value of the harvestable
     */
    public static float getModifiedValue(@NotNull HarvestableType type, @NotNull ItemStack itemStack) {
        float value = type.getValue();
        if (NBT.readNbt(itemStack).hasTag("barn-enchanted-harvestable")) {
            value *= HarvestableManager.getEnchantedHarvestableMultiplier();
        }
        return value;
    }

    /**
     * @param type Type of the harvestable
     * @param itemStack ItemStack that is a harvestable
     * @return Display name with included amount and Special Type included (e.g. 2x Enchanted N)
     */
    public static @NotNull TextComponent getModifiedDisplayName(@NotNull HarvestableType type, @NotNull ItemStack itemStack) {
        TextComponent amount = Component.text(itemStack.getAmount() + "x ", NamedTextColor.GRAY);
        TextComponent nonNumberedFullName = type.getDisplayName();
        if (NBT.readNbt(itemStack).hasTag("barn-enchanted-harvestable")) {
            nonNumberedFullName = nonNumberedFullName.content("Enchanted " + nonNumberedFullName.content());
        }
        return amount.append(nonNumberedFullName);
    }
}
