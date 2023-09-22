package com.bof.core.region.plots;

import com.bof.core.Core;
import com.bof.core.placeholders.holo.HoloPlaceholders;
import com.bof.core.region.plots.event.PlotCreatedEvent;
import com.bof.core.region.plots.farm.FarmPlot;
import com.bof.core.utils.PlotUtils;
import com.bof.toolkit.utils.ComponentUtils;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.HologramBuilder;
import com.github.unldenis.hologram.HologramPool;
import com.github.unldenis.hologram.line.BlockLine;
import com.github.unldenis.hologram.line.Line;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class PlotHoloManager implements Listener {
    private final Core plugin;

    @EventHandler
    private void onPlotCreate(PlotCreatedEvent event) {
        this.handleHologram(event.getPlot());
    }

    private void handleHologram(HarvestablePlot plot) {
        PlotUtils.identifyHologram(plot).ifPresent(loc -> {
            loc.getBlock().setType(Material.AIR);
            plot.setHologram(this.getHologram(plugin, plot, loc, plot.getOwningRegion().getHologramPool()));
        });
    }

    private Hologram getHologram(Core plugin, HarvestablePlot plot, Location loc, HologramPool holoPool) {
        Location holoLoc = loc.add(0.5, 0.75, 0);

        HologramBuilder builder = Hologram.builder(plugin, holoLoc)
                .addLine(ComponentUtils.toColoredString(plot.getDisplayName()))
                .addLine("");

        plot.getLore().forEach(component -> builder.addLine(ComponentUtils.toColoredString(component)));

        Hologram holo = builder
                .addLine("")
                .addClickableLine(ComponentUtils.toColoredString(Component.text("Open menu", NamedTextColor.GRAY)))
                .placeholders(HoloPlaceholders.getPlaceholders())
                .loadAndBuild(holoPool);


        this.addBlockLine(plot, holo);
        return holo;
    }

    private void addBlockLine(Plot plot, Hologram holo) {
        Location loc = holo.getLines().get(0).getLocation().clone().add(0, 0.6, 0.25);

        ItemStack item = new ItemStack(Material.BARRIER);
        switch (plot.getType()) {
            case FARM -> item = new ItemStack(((FarmPlot) plot).getCurrentCrop().getItemMaterial());
            case SILO -> item = new ItemStack(Material.BARREL);
        }

        BlockLine line = new BlockLine(new Line(plugin, loc), item);
        holo.getLines().add(0, line);
    }
}
