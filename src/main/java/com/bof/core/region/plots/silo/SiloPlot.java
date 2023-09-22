package com.bof.core.region.plots.silo;

import com.bof.core.region.BarnRegion;
import com.bof.core.region.plots.Plot;
import com.bof.core.region.plots.PlotType;
import com.bof.core.region.plots.silo.menu.SiloPlotMainMenu;
import com.bof.core.utils.BoxUtils;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private final Set<ItemStack> items = new HashSet<>();
    private boolean autoSell = false;

    public SiloPlot(BarnRegion owningRegion, BoundingBox box, int id) {
        this.owningRegion = owningRegion;
        this.box = box;
        this.id = id;
        this.boxBlocks = BoxUtils.getBlocksInBox(box, true);
    }

    public int getFilledAmount() {
        return this.items.stream()
                .mapToInt(ItemStack::getAmount)
                .sum();
    }

    public float getFilledPercentage() {
        float percentage = (float) this.getFilledAmount() / this.capacity * 100;
        return Math.min(100, percentage); // Ensure the percentage doesn't exceed 100
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
