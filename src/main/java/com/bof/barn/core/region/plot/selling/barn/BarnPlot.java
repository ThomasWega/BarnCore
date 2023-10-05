package com.bof.barn.core.region.plot.selling.barn;

import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.selling.ContainerPlot;
import com.bof.barn.core.region.plot.selling.barn.menu.BarnPlotMainMenu;
import com.bof.barn.core.utils.BoxUtils;
import com.bof.barn.core.utils.HarvestableUtils;
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
public class BarnPlot implements ContainerPlot {
    private final BarnRegion owningRegion;
    private final PlotType type = PlotType.BARN;
    private final BoundingBox box;
    private final int id;
    private final Set<Block> boxBlocks;
    private final int capacity = 1000;
    private final List<ItemStack> stored = new ArrayList<>();
    private Hologram hologram;
    private boolean autoSell = false;

    public BarnPlot(BarnRegion owningRegion, BoundingBox box, int id) {
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

    /**
     * Tries to add the items to the barn. Handles the {@link #autoSell}
     *
     * @param animals Animal items to try to add
     * @return List of items that couldn't be added
     */
    public @NotNull List<ItemStack> addAnimalsToBarn(@NotNull ItemStack... animals) {
        return this.addAnimalsToBarn(Arrays.asList(animals));
    }

    /**
     * Tries to add the items to the barn. Handles the {@link #autoSell}
     *
     * @param animals Animal items to try to add
     * @return List of items that couldn't be added
     */
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
                this.stored.add(itemStack);
            }
        }

        this.updateHologram();
        return unAdded;
    }

    /**
     * Remove the animals from the barn inventory and calculate the value of them.
     * Then add that value in farm coins to the regions balance
     *
     * @param animals Animal items to sell
     * @return Value of the animals sold
     */
    public float sellAnimals(@NotNull ItemStack... animals) {
        return this.sellAnimals(Arrays.asList(animals));
    }

    /**
     * Remove the animals from the barn inventory and calculate the value of them.
     * Then add that value in farm coins to the regions balance
     *
     * @param animals Animal items to sell
     * @return Value of the animals sold
     */
    public float sellAnimals(@NotNull Collection<ItemStack> animals) {
        float value = HarvestableUtils.getValueOfAnimals(animals);
        this.removeAnimalsFromBarn(animals);
        this.getOwningRegion().addFarmCoins(value);
        this.updateHologram();
        return value;
    }

    /**
     * Remove the given animals from the barn inventory
     *
     * @param animals Animal items to remove
     */
    private void removeAnimalsFromBarn(Collection<ItemStack> animals) {
        List<ItemStack> cropsToRemove = new ArrayList<>(animals);
        cropsToRemove.forEach(this.stored::remove);
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
        return this.parsePlaceholdersAndConvertToComponent(List.of(
                "<color:#FCDB03>Capacity: <red>%barn_plot_barn_capacity_" + this.id + "%</red></color>",
                "<color:#D4F542>Filled: <red>%barn_plot_barn_filled_" + this.id + "%/%barn_plot_barn_capacity_" + this.id + "% (%barn_plot_barn_percentage_filled_" + this.id + "%%)</red></color>",
                "<color:#2B84FF>Auto Sell: %barn_plot_barn_colored_status_autosell_" + this.id + "%</color>"
        ));
    }
}
