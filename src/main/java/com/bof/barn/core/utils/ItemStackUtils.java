package com.bof.barn.core.utils;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ItemStackUtils {

    private ItemStackUtils() {
    }

    /**
     * Merges the ItemStacks from the given list to their maximum stack amount
     *
     * @param inputItems ItemStack to stack up
     * @return List of stacked up items
     */
    public static List<ItemStack> mergeItemStacks(List<ItemStack> inputItems) {
        List<ItemStack> copiedItems = new ArrayList<>(inputItems);

        return new ArrayList<>(copiedItems.stream()
                .collect(Collectors.toMap(
                        ItemStack::getType,
                        Function.identity(),
                        (existing, replacement) -> {
                            ItemStack mergedItem = existing.clone();
                            mergedItem.setAmount(existing.getAmount() + replacement.getAmount());
                            return mergedItem;
                        }
                ))
                .values());
    }

}