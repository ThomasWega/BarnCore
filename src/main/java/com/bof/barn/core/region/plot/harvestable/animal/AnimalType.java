package com.bof.barn.core.region.plot.harvestable.animal;

import com.bof.barn.core.region.plot.harvestable.HarvestableType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum AnimalType implements HarvestableType {
    NONE(EntityType.UNKNOWN, new ItemStack(Material.BARRIER), Component.text("None", NamedTextColor.RED), 0f),
    COW(EntityType.COW, new ItemStack(Material.BEEF),
            Component.text("Cow", TextColor.fromHexString("#A36B21")), 1f),
    PIG(EntityType.PIG, new ItemStack(Material.PORKCHOP),
            Component.text("Pig", TextColor.fromHexString("#FFC0CB")), 2f),
    CHICKEN(EntityType.CHICKEN, new ItemStack(Material.CHICKEN),
            Component.text("Chicken", TextColor.fromHexString("#D0D28C")), 3f),
    SHEEP(EntityType.SHEEP, new ItemStack(Material.MUTTON),
            Component.text("Sheep", TextColor.fromHexString("#FFFFFF")), 4f);

    private final EntityType entityType;
    private final ItemStack item;
    private final Component displayName;
    private final float value;

    public static Set<ItemStack> getItemMaterials() {
        return Arrays.stream(AnimalType.values())
                .map(AnimalType::getItem)
                .collect(Collectors.toSet());
    }

    public static Optional<AnimalType> getByItem(@NotNull ItemStack itemStack) {
        return getByItem(itemStack, false);
    }

    public static Optional<AnimalType> getByItem(@NotNull ItemStack itemStack, boolean ignoreAmount) {
        ItemStack clone = itemStack.clone();
        if (ignoreAmount) {
            clone.setAmount(1);
        }
        for (AnimalType type : AnimalType.values()) {
            if (type.getItem().equals(clone)) return Optional.of(type);
        }
        return Optional.empty();
    }

    public static Optional<AnimalType> getByEntityType(@NotNull EntityType entityType) {
        for (AnimalType type : AnimalType.values()) {
            if (type.getEntityType() == entityType) return Optional.of(type);
        }
        return Optional.empty();
    }
}
