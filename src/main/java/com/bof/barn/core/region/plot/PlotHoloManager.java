package com.bof.barn.core.region.plot;

import com.bof.barn.core.Core;
import com.bof.barn.core.item.SkullBuilder;
import com.bof.barn.core.placeholders.holo.HoloPlaceholders;
import com.bof.barn.core.region.plot.event.PlotCreatedEvent;
import com.bof.barn.core.region.plot.harvestable.AbstractHarvestablePlot;
import com.bof.barn.core.utils.PlotUtils;
import com.bof.toolkit.skin.Skin;
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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

/**
 * Handles the setting of hologram for the plot
 */
@RequiredArgsConstructor
public class PlotHoloManager implements Listener {
    private final Core plugin;

    @EventHandler
    private void onPlotCreate(PlotCreatedEvent event) {
        this.handleHologram(event.getPlot());
    }

    /**
     * Create the holograms
     *
     * @param plot Plot to create holograms for
     */
    private void handleHologram(AbstractPlot plot) {
        PlotUtils.identifyHologram(plot).ifPresent(loc -> {
            loc.getBlock().setType(Material.AIR);
            plot.setHologram(this.getHologram(plugin, plot, loc, plot.getOwningRegion().getHologramPool()));
        });
    }

    /**
     * Constructs the hologram
     *
     * @param plugin   Core instance
     * @param plot     Plot the hologram will belong to
     * @param loc      Exact location where the holo sign was at
     * @param holoPool The regions hologram pool
     * @return Constructed hologram with all it's texts and attributes
     */
    private Hologram getHologram(Core plugin, AbstractPlot plot, Location loc, HologramPool holoPool) {
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

    /**
     * Get the offset for the block item from the config
     *
     * @param type Type of the plot
     * @param loc  The block line location
     * @return new Calculated location
     */
    private Location setOffsetValues(PlotType type, Location loc) {
        Location newLoc = loc.clone();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("plots." + type.getIdentifier() + ".holo-item-offset");
        assert section != null;
        double x = section.getDouble("x", 0);
        double y = section.getDouble("y", 0);
        double z = section.getDouble("z", 0);

        newLoc.add(x, y, z);
        return newLoc;
    }

    /**
     * Adds the block line and handles all it's modifications
     *
     * @param plot Plot the hologram belongs to
     * @param holo Hologram instance
     */
    private void addBlockLine(AbstractPlot plot, Hologram holo) {
        Location loc = holo.getLines().get(0).getLocation().clone();
        Location offSetLoc = this.setOffsetValues(plot.getType(), loc);

        ItemStack item = new ItemStack(Material.BARRIER);
        switch (plot.getType()) {
            case BARN -> item = new SkullBuilder()
                    .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmRhMGMyNDBjOGM3ZjMyOGYyZTYzOGYxYmY4NjJiODg5YjZlOTdiNjYwNzAwOTcxMTM5YmQ2MzQ4MWVjZDQzOSJ9fX0=", null))
                    .build();
            case FARM, ANIMAL ->
                    item = new ItemStack(((AbstractHarvestablePlot<?>) plot).getCurrentlyHarvesting().getItem());
            case SILO -> item = new ItemStack(Material.BARREL);
        }

        BlockLine line = new BlockLine(new Line(plugin, offSetLoc), item);
        holo.getLines().add(0, line);
    }
}
