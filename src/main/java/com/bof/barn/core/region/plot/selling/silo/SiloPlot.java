package com.bof.barn.core.region.plot.selling.silo;

import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.PlotSetting;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.farm.CropType;
import com.bof.barn.core.region.plot.selling.ContainerPlot;
import com.bof.barn.core.region.plot.selling.silo.menu.SiloPlotMainMenu;
import com.bof.barn.core.utils.BoxUtils;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import com.github.unldenis.hologram.line.BlockLine;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

import java.util.*;
import java.util.function.Consumer;

@Data
public class SiloPlot implements ContainerPlot<CropType> {
    private final Map<Class<? extends PlotSetting>, PlotSetting> settings = new HashMap<>();
    private final BarnRegion owningRegion;
    private final PlotType type = PlotType.SILO;
    private final Class<CropType> storeType = CropType.class;
    private final BoundingBox box;
    private final int id;
    private final Set<Block> boxBlocks;
    private final int capacity = 1000;
    private final List<ItemStack> stored = new ArrayList<>();
    private Hologram hologram;
    private boolean autoSell = false;

    public SiloPlot(BarnRegion owningRegion, BoundingBox box, int id) {
        this.owningRegion = owningRegion;
        this.box = box;
        this.id = id;
        this.boxBlocks = BoxUtils.getBlocksInBox(box, true);
    }

    @Override
    public boolean isFull() {
        return this.getFilledAmount() >= capacity;
    }


    @Override
    public int getFilledAmount() {
        return this.stored.stream()
                .mapToInt(ItemStack::getAmount)
                .sum();
    }

    @Override
    public float getFilledPercentage() {
        float percentage = (float) this.getFilledAmount() / this.capacity * 100;
        return Math.min(100, percentage); // Ensure the percentage doesn't exceed 100
    }

    @Override
    public float getFilledPercentageRounded(int roundNum) {
        return Math.round(this.getFilledPercentage() * Math.pow(10, roundNum)) / (float) Math.pow(10, roundNum);
    }

    @Override
    public void updateHologram() {
        this.hologram.getLines().stream()
                .filter(iLine -> iLine instanceof BlockLine)
                .map(iLine -> ((BlockLine) iLine))
                .forEach(blockLine -> {
                    Material material = this.isFull() ? Material.BARRIER : Material.BARREL;
                    blockLine.setObj(new ItemStack(material));
                });

        this.hologram.getLines().forEach(iLine -> iLine.update(this.owningRegion.getAllOnlinePlayers()));
    }


    @Override
    public Consumer<PlayerHologramInteractEvent> getHologramAction() {
        return event -> {
            if (event.getHologram().equals(this.hologram)) {
                new SiloPlotMainMenu(this).show(event.getPlayer());
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return MiniMessage.miniMessage().deserialize("<b><color:#CCD609>Silo " + id + "</color></b>");
    }

    @Override
    public List<Component> getLore() {
        return this.parsePlaceholdersAndConvertToComponent(List.of(
                "<color:#FCDB03>Capacity: <red>%barn_plot_silo_capacity_" + this.id + "%</red></color>",
                "<color:#D4F542>Filled: <red>%barn_plot_silo_filled_" + this.id + "%/%barn_plot_silo_capacity_" + this.id + "% (%barn_plot_silo_percentage_filled_" + this.id + "%%)</red></color>",
                "<color:#2B84FF>Auto Sell: %barn_plot_silo_colored_status_autosell_" + this.id + "%</color>"
        ));
    }
}
