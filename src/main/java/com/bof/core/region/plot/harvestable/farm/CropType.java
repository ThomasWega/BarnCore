package com.bof.core.region.plot.harvestable.farm;

import com.bof.core.region.plot.harvestable.HarvestableType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum CropType implements HarvestableType {
    NONE(Material.AIR, Material.BARRIER, Component.text("None", NamedTextColor.RED), 0f),
    WHEAT(Material.WHEAT, Material.WHEAT, Component.text("Wheat", TextColor.fromHexString("#F5DEB3")), 1f),
    CARROT(Material.CARROTS, Material.CARROT, Component.text("Carrots", TextColor.fromHexString("#FFA500")), 2f),
    BEETROOT(Material.BEETROOTS, Material.BEETROOT, Component.text("Beetroots", TextColor.fromHexString("#F24949")), 3f),
    POTATO(Material.POTATOES, Material.POTATO, Component.text("Potatoes", TextColor.fromHexString("#D2B48C")), 4f);

    private final Material material;
    private final Material itemMaterial;
    private final Component displayName;
    private final float value;

    public static Set<Material> getMaterials() {
        return Arrays.stream(CropType.values())
                .map(CropType::getMaterial)
                .collect(Collectors.toSet());
    }

    public static Set<Material> getItemMaterials() {
        return Arrays.stream(CropType.values())
                .map(CropType::getItemMaterial)
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
            if (type.getItemMaterial() == material) return Optional.of(type);
        }
        return Optional.empty();
    }
}
