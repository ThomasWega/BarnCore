package com.bof.core.region.plot.barn;

import com.bof.core.region.BarnRegion;
import com.bof.core.region.plot.PlotType;
import com.bof.core.region.plot.SellingPlot;
import com.bof.core.region.plot.barn.menu.BarnPlotMainMenu;
import com.bof.core.utils.BoxUtils;
import com.bof.core.utils.HarvestableUtils;
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
public class BarnPlot implements SellingPlot {
    private final BarnRegion owningRegion;
    private final PlotType type = PlotType.SILO;
    private final BoundingBox box;
    private final int id;
    private final Set<Block> boxBlocks;
    private Hologram hologram;
    private final int capacity = 1000;
    private final List<ItemStack> animalsStored = new ArrayList<>();
    private boolean autoSell = false;

    public BarnPlot(BarnRegion owningRegion, BoundingBox box, int id) {
        this.owningRegion = owningRegion;
        this.box = box;
        this.id = id;
        this.boxBlocks = BoxUtils.getBlocksInBox(box, true);
    }

    public boolean isFull() {
        return this.getFilledAmount() >= capacity;
    }

    public int getFilledAmount() {
        return this.animalsStored.stream()
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

    public @NotNull List<ItemStack> addAnimalsToBarn(@NotNull ItemStack... animals) {
        return this.addAnimalsToBarn(Arrays.asList(animals));
    }

    public @NotNull List<ItemStack> addAnimalsToBarn(@NotNull Collection<ItemStack> animals) {
        List<ItemStack> unAdded = new ArrayList<>();
        for (ItemStack itemStack : animals) {
            if (this.isFull()) {
                unAdded.add(itemStack);
                continue;
            }

            if (this.isAutoSell()) {
                this.sellAnimals(itemStack);
            } else {
                this.animalsStored.add(itemStack);
            }
        }

        this.updateHologram();
        return unAdded;
    }

    public float sellAnimals(@NotNull ItemStack... animals) {
        return this.sellAnimals(Arrays.asList(animals));
    }


    public float sellAnimals(@NotNull Collection<ItemStack> animals) {
        float value = HarvestableUtils.getValueOfAnimals(animals);
        this.removeAnimalsFromBarn(animals);
        this.getOwningRegion().addFarmCoins(value);
        this.updateHologram();
        return value;
    }

    private void removeAnimalsFromBarn(Collection<ItemStack> animals) {
        List<ItemStack> cropsToRemove = new ArrayList<>(animals);
        cropsToRemove.forEach(this.animalsStored::remove);
    }


    public void setAutoSell(boolean autoSell) {
        this.autoSell = autoSell;
        this.updateHologram();
    }


    @Override
    public Consumer<PlayerHologramInteractEvent> getHologramAction() {
        return event -> {
            if (event.getHologram().equals(this.hologram)) {
                 new BarnPlotMainMenu(this).show(event.getPlayer());
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return MiniMessage.miniMessage().deserialize("<b><color:#ff2d26>Barn</color></b>");
    }

    @Override
    public List<Component> getLore() {
        return List.of(
                MiniMessage.miniMessage().deserialize("<color:#FCDB03>Capacity: <red>%barn_plot_barn_capacity_" + this.id + "%</red></color>"),
                MiniMessage.miniMessage().deserialize("<color:#D4F542>Filled: <red>%barn_plot_barn_filled_" + this.id + "%/%barn_plot_barn_capacity_" + this.id + "% (%barn_plot_barn_percentage_filled_" + this.id + "%%)</red></color>"),
                MiniMessage.miniMessage().deserialize("<color:#2B84FF>Auto Sell: %barn_plot_barn_colored_status_autosell_" + this.id + "%</color>")
        );
    }
}
