package com.bof.core.plots.crops;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

@RequiredArgsConstructor
@Getter
public enum CropsType {
    NONE(Material.AIR, Material.BARRIER),
    WHEAT(Material.WHEAT, Material.WHEAT),
    CARROT(Material.CARROTS, Material.CARROTS),
    BEETROOT(Material.BEETROOTS, Material.BEETROOTS),
    POTATO(Material.POTATOES, Material.POTATOES);

    private final Material material;
    private final Material displayMaterial;
}
