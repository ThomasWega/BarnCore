package com.bof.barn.core.region.plot.container.silo;

import com.bof.barn.core.Core;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.container.ContainerPlot;
import com.bof.barn.core.region.plot.container.silo.menu.SiloPlotMainMenu;
import com.bof.barn.core.region.plot.harvestable.farm.CropType;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import com.github.unldenis.hologram.line.BlockLine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;


public class SiloPlot extends ContainerPlot<CropType> {

    public SiloPlot(@NotNull Core plugin, @NotNull BarnRegion owningRegion, @NotNull BoundingBox box, int id) {
        super(plugin, PlotType.SILO, owningRegion, box, id, CropType.class);
    }

    @Override
    public void updateHologram() {
        this.getHologram().getLines().stream()
                .filter(iLine -> iLine instanceof BlockLine)
                .map(iLine -> ((BlockLine) iLine))
                .forEach(blockLine -> {
                    Material material = this.isFull() ? Material.BARRIER : Material.BARREL;
                    blockLine.setObj(new ItemStack(material));
                });

        this.getHologram().getLines().forEach(iLine -> iLine.update(this.getOwningRegion().getAllOnlinePlayers()));
    }


    @Override
    public Consumer<PlayerHologramInteractEvent> getHologramAction() {
        return event -> {
            if (event.getHologram().equals(this.getHologram())) {
                new SiloPlotMainMenu(this).show(event.getPlayer());
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return MiniMessage.miniMessage().deserialize("<b><color:#CCD609>Silo " + getId() + "</color></b>");
    }

    @Override
    public List<Component> getLore() {
        return this.parsePlaceholdersAndConvertToComponent(List.of(
                "<color:#FCDB03>Capacity: <red>%barn_plot_silo_capacity_" + this.getId() + "%</red></color>",
                "<color:#D4F542>Filled: <red>%barn_plot_silo_filled_" + this.getId() + "%/%barn_plot_silo_capacity_" + this.getId() + "% (%barn_plot_silo_percentage_filled_" + this.getId() + "%%)</red></color>"
        ));
    }
}
