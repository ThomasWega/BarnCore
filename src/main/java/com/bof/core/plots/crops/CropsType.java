package com.bof.core.plots.crops;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

@RequiredArgsConstructor
@Getter
public enum CropsType {
    NONE(Material.AIR, Material.BARRIER, Component.text("None", NamedTextColor.RED)),
    WHEAT(Material.WHEAT, Material.WHEAT, Component.text("Wheat", TextColor.fromHexString("#F5DEB3"))),
    CARROT(Material.CARROTS, Material.CARROT, Component.text("Carrots", TextColor.fromHexString("#FFA500"))),
    BEETROOT(Material.BEETROOTS, Material.BEETROOT, Component.text("Beetroots", TextColor.fromHexString("#F24949"))),
    POTATO(Material.POTATOES, Material.POTATO, Component.text("Potatoes", TextColor.fromHexString("#D2B48C")));

    private final Material material;
    private final Material displayMaterial;
    private final Component displayName;
}
