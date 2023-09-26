package com.bof.core.region.plot.animal;

import com.bof.core.item.SkullBuilder;
import com.bof.core.region.plot.HarvestableType;
import com.bof.core.skin.Skin;
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
    COW(EntityType.COW, new SkullBuilder()
            .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzVhOWNkNThkNGM2N2JjY2M4ZmIxZjVmNzU2YTJkMzgxYzlmZmFjMjkyNGI3ZjRjYjcxYWE5ZmExM2ZiNWMifX19", null))
            .build(),
            Component.text("Cow", TextColor.fromHexString("#A36B21")), 1f),
    PIG(EntityType.PIG, new SkullBuilder()
            .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmVlODUxNDg5MmYzZDc4YTMyZTg0NTZmY2JiOGM2MDgxZTIxYjI0NmQ4MmYzOThiZDk2OWZlYzE5ZDNjMjdiMyJ9fX0=", null))
            .build(),
            Component.text("Pig", TextColor.fromHexString("#FFC0CB")), 2f),
    CHICKEN(EntityType.CHICKEN, new SkullBuilder()
            .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2EzNTgyY2U0ODg5MzMzZGFkMzI5ZTRlMjQzNzJhMDNhNWRhYTJjMzQyODBjNTYyNTZhZjUyODNlZGIwNDNmOCJ9fX0=", null))
            .build(),
            Component.text("Chicken", TextColor.fromHexString("#D0D28C")), 3f),
    SHEEP(EntityType.SHEEP, new SkullBuilder()
            .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjMxZjljY2M2YjNlMzJlY2YxM2I4YTExYWMyOWNkMzNkMThjOTVmYzczZGI4YTY2YzVkNjU3Y2NiOGJlNzAifX19", null))
            .build(),
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
        for (AnimalType type : AnimalType.values()) {
            if (type.getItem() == itemStack) return Optional.of(type);
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
