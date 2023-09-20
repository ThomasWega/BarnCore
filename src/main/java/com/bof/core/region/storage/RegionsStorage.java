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
import com.github.unldenis.hologram.InteractiveHologramPool;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import com.github.unldenis.hologram.line.BlockLine;
import com.github.unldenis.hologram.line.ClickableTextLine;
import com.github.unldenis.hologram.line.Line;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
        Arrays.stream(PlotType.values()).forEach(plotType -> plots.put(plotType, BoxUtils.identifyPlots(plotType, region.getBox()).entrySet().stream()
                .map(entry -> Plot.newPlot(region, plotType, entry.getValue(), Integer.parseInt(entry.getKey())))
                .collect(Collectors.toSet())
        ));
        region.setPlots(plots);

        handleHolograms(plugin, region);
    }

    private static void handleHolograms(Core plugin, BarnRegion region) {
        HologramPool holoPool = new HologramPool(plugin, 100);
        region.setHologramPool(holoPool);

        InteractiveHologramPool interactiveHoloPool = new InteractiveHologramPool(holoPool, 0f, 5f);
        region.setInteractiveHologramPool(interactiveHoloPool);

        region.getPlots().values().forEach(plots -> plots.forEach(plot -> PlotUtils.identifyHologram(plot).ifPresent(loc -> {
            loc.getBlock().setType(Material.AIR);
            Location holoLoc = loc.add(0.5, 0.75, 0);

            Component name = MiniMessage.miniMessage().deserialize("<b><color:#FC7B03>Farm Plot " + plot.getId() + "</color></b>");
            Component upgrade = MiniMessage.miniMessage().deserialize("<color:#FCDB03>Upgrades: <red>❌</red></color>");
            Component enchRain = MiniMessage.miniMessage().deserialize("<color:#D4F542>Enchanted rain: <red>❌</red></color>");

            Hologram holo = Hologram.builder(plugin, holoLoc)
                    .addLine(ComponentUtils.toColoredString(name))
                    .addLine("")
                    .addLine(ComponentUtils.toColoredString(upgrade))
                    .addClickableLine(ComponentUtils.toColoredString(enchRain))
                    .loadAndBuild(holoPool);

            // add the type line, but move it slightly
            BlockLine line = new BlockLine(new Line(plugin, holo.getLines().get(0).getLocation().clone().add(0, 0.6, 0.25)), new ItemStack(((FarmPlot) plot).getCurrentCrop().getDisplayMaterial()));
            holo.getLines().add(0, line);

            plot.setHologram(holo);
        })));
    }
}
