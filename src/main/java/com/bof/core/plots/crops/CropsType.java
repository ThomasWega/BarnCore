package com.bof.core.plots.crops;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

@RequiredArgsConstructor
@Getter
public enum CropsType {
    WHEAT(Material.WHEAT),
    CARROT(Material.CARROTS),
    BEETROOT(Material.BEETROOTS),
    POTATO(Material.POTATOES);

    private final Material material;
}
