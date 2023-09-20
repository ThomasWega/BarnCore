package com.bof.core.region.storage;

import com.bof.barn.world_generator.data.SchematicsStorage;
import com.bof.core.Core;
import com.bof.core.plots.Plot;
import com.bof.core.plots.PlotType;
import com.bof.core.plots.crops.FarmPlot;
import com.bof.core.region.BarnRegion;
import com.bof.core.utils.BoxUtils;
import com.bof.core.utils.PlotUtils;
import com.bof.toolkit.utils.ComponentUtils;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.HologramPool;
import com.github.unldenis.hologram.animation.Animation;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class RegionsStorage {
    public static List<BarnRegion> regions = new ArrayList<>();

    public static void convertToRegions(@NotNull Core plugin) {
        regions = SchematicsStorage.getPastedRegions().stream()
                .map(BarnRegion::new)
                .peek(region -> {
                    handleSpawnLocation(region);
                    handlePlots(plugin, region);
                })
                .toList();
    }

    private static void handleSpawnLocation(BarnRegion region) {
        Optional<Location> optSpawnLoc = BoxUtils.identifySpawn(region.getBox());
        // remove the sign
        optSpawnLoc.ifPresent(loc -> loc.getBlock().setType(Material.AIR));
        region.setSpawnLocation(optSpawnLoc.map(loc -> loc.add(0, 2, 0))
                .orElse(region.getBox().getCenter().toLocation(Bukkit.getWorld("world"))));
    }

    private static void handlePlots(Core plugin, BarnRegion region) {
        Map<PlotType, Set<Plot>> plots = new HashMap<>();
        Arrays.stream(PlotType.values()).forEach(plotType -> plots.put(plotType, BoxUtils.identifyPlots(plotType, region.getBox()).stream()
                .map(box -> Plot.newPlot(plotType, box))
                .collect(Collectors.toSet())
        ));
        region.setPlots(plots);

        HologramPool holoPool = new HologramPool(plugin, 100);
        region.setHologramPool(holoPool);

        // handle the holograms for all plots
        plots.forEach((key, value) -> value.forEach(plot -> PlotUtils.identifyHologram(plot).ifPresent(loc -> {
            loc.getBlock().setType(Material.AIR);
            Location holoLoc = loc.add(0.5, 0.75, 0);

            TextComponent name = Component.text("Farm Plot", TextColor.fromHexString("#FFA500"));
            Hologram holo = Hologram.builder(plugin, holoLoc)
                    .addBlockLine(new ItemStack(((FarmPlot) plot).getCurrentCrop().getDisplayMaterial()), Animation.AnimationType.CIRCLE)
                    .addLine(ComponentUtils.toColoredString(name))
                    // upgrades (with enchanted rain)
                    .loadAndBuild(holoPool);


            // add the ItemStack holo, which should be moved a little
            // holo.getLines().add(0, new BlockLine(new Line(plugin, loc.add(0, 0, 0.5)), new ItemStack(((FarmPlot) plot).getCurrentCrop().getDisplayMaterial())));

            plot.setHologram(holo);
        })));
    }
}
