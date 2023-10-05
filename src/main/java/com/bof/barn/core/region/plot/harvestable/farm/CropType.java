package com.bof.barn.core.region.plot.harvestable.farm;

import com.bof.barn.core.region.plot.harvestable.HarvestableType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum CropType implements HarvestableType {
    NONE(new ItemStack(Material.BARRIER), Material.AIR, Component.text("None", NamedTextColor.RED), 0f),
    WHEAT(new ItemStack(Material.WHEAT), Material.WHEAT, Component.text("Wheat", TextColor.fromHexString("#F5DEB3")), 1f),
    CARROT(new ItemStack(Material.CARROT), Material.CARROTS, Component.text("Carrots", TextColor.fromHexString("#FFA500")), 2f),
    BEETROOT(new ItemStack(Material.BEETROOT), Material.BEETROOTS, Component.text("Beetroots", TextColor.fromHexString("#F24949")), 3f),
    POTATO(new ItemStack(Material.POTATO), Material.POTATOES, Component.text("Potatoes", TextColor.fromHexString("#D2B48C")), 4f);

    private final ItemStack item;
    private final Material material;
    private final TextComponent displayName;
    private final float value;

    public static Set<Material> getMaterials() {
        return Arrays.stream(CropType.values())
                .map(CropType::getMaterial)
                .collect(Collectors.toSet());
    }


    public static Optional<CropType> getByMaterial(@NotNull Material material) {
        for (CropType type : CropType.values()) {
            if (type.getMaterial() == material) return Optional.of(type);
        }
        return Optional.empty();
    }

    public static Optional<CropType> getByItemMaterial(@NotNull Material material) {
        for (CropType type : CropType.values()) {
            if (type.getItem().getType() == material) return Optional.of(type);
        }
        return Optional.empty();
    }
}
