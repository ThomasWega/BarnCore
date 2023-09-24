package com.bof.core.region.plot.silo;

import com.bof.core.region.BarnRegion;
import com.bof.core.region.plot.Plot;
import com.bof.core.region.plot.PlotType;
import com.bof.core.region.plot.silo.menu.SiloPlotMainMenu;
import com.bof.core.utils.BoxUtils;
import com.bof.core.utils.CropUtils;
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
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

@Data
public class SiloPlot implements Plot {
    private final BarnRegion owningRegion;
    private final PlotType type = PlotType.SILO;
    private final BoundingBox box;
    private final int id;
    private final Set<Block> boxBlocks;
    private Hologram hologram;
    private final int capacity = 1000;
    private final List<ItemStack> cropsStored = new ArrayList<>();
    private boolean autoSell = false;

    public SiloPlot(BarnRegion owningRegion, BoundingBox box, int id) {
        this.owningRegion = owningRegion;
        this.box = box;
        this.id = id;
        this.boxBlocks = BoxUtils.getBlocksInBox(box, true);
    }

    public boolean isFull() {
        return this.getFilledAmount() >= capacity;
    }

    public int getFilledAmount() {
        return this.cropsStored.stream()
                .mapToInt(ItemStack::getAmount)
                .sum();
    }

    public float getFilledPercentage() {
        float percentage = (float) this.getFilledAmount() / this.capacity * 100;
        return Math.min(100, percentage); // Ensure the percentage doesn't exceed 100
    }

    public float getFilledPercentageRounded(int roundNum) {
        return Math.round(this.getFilledPercentage() * Math.pow(10, roundNum)) / (float) Math.pow(10, roundNum);
    }


    public void updateHologram() {
        this.hologram.getLines().stream()
                .filter(iLine -> iLine instanceof BlockLine)
                .map(iLine -> ((BlockLine) iLine))
                .forEach(blockLine -> {
                    Material material = this.isFull() ? Material.BARRIER : Material.BARREL;
                    blockLine.setObj(new ItemStack(material));
                });

        this.hologram.getLines().forEach(iLine -> iLine.update(this.owningRegion.getAllPlayers()));
    }

    public @NotNull List<ItemStack> addCropsToSilo(@NotNull ItemStack... crops) {
        return this.addCropsToSilo(Arrays.asList(crops));
    }

    public @NotNull List<ItemStack> addCropsToSilo(@NotNull Collection<ItemStack> crops) {
        List<ItemStack> unAdded = new ArrayList<>();
        for (ItemStack itemStack : crops) {
            if (this.isFull()) {
                unAdded.add(itemStack);
                continue;
            }

            if (this.isAutoSell()) {
                this.sellCrops(itemStack);
            } else {
                this.cropsStored.add(itemStack);
            }
        }

        this.updateHologram();
        return unAdded;
    }

    public float sellCrops(@NotNull ItemStack... crops) {
        return this.sellCrops(Arrays.asList(crops));
    }


    public float sellCrops(@NotNull Collection<ItemStack> crops) {
        // Create a copy of the collection to avoid ConcurrentModificationException
        // (the crops can be referenced from cropStored)
        List<ItemStack> cropsToRemove = new ArrayList<>(crops);
        // can't use removeAll, because that will remove all crops of the same type
        cropsToRemove.forEach(this.cropsStored::remove);

        float value = CropUtils.getValueOf(cropsToRemove);
        this.getOwningRegion().addFarmCoins(value);
        this.updateHologram();
        return value;
    }


    public void setAutoSell(boolean autoSell) {
        this.autoSell = autoSell;
        this.updateHologram();
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
        return List.of(
                MiniMessage.miniMessage().deserialize("<color:#FCDB03>Capacity: <red>%barn_plot_silo_capacity_" + this.id + "%</red></color>"),
                MiniMessage.miniMessage().deserialize("<color:#D4F542>Filled: <red>%barn_plot_silo_filled_" + this.id + "%/%barn_plot_silo_capacity_" + this.id + "% (%barn_plot_silo_percentage_filled_" + this.id + "%%)</red></color>"),
                MiniMessage.miniMessage().deserialize("<color:#2B84FF>Auto Sell: %barn_plot_silo_colored_status_autosell_" + this.id + "%</color>")
        );
    }
}
