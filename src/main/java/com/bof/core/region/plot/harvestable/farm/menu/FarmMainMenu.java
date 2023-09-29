package com.bof.core.region.plot.harvestable.farm.menu;

import com.bof.core.item.ItemBuilder;
import com.bof.core.item.SkullBuilder;
import com.bof.core.menu.premade.back.GoBackPane;
import com.bof.core.region.BarnRegion;
import com.bof.core.region.menu.RegionMainMenu;
import com.bof.core.region.plot.Plot;
import com.bof.core.region.plot.PlotType;
import com.bof.core.region.plot.harvestable.farm.FarmPlot;
import com.bof.toolkit.skin.Skin;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class FarmMainMenu extends ChestGui {
    private final BarnRegion region;
    private final OutlinePane mainPane = new OutlinePane(1, 2, 7, 2);
    private final OutlinePane lockedPane = mainPane.copy();

    public FarmMainMenu(@NotNull BarnRegion region) {
        super(5, ComponentHolder.of(Component.text("Farm Plots Menu")));
        this.region = region;
        this.initialize();
    }

    private void initialize() {
        this.mainPane.setPriority(Pane.Priority.NORMAL);
        this.lockedPane.setPriority(Pane.Priority.LOW);

        this.addLockedPlots();
        this.addActivePlots();

        this.addPane(new GoBackPane(4, 4, new RegionMainMenu(this.region)));
        this.addPane(mainPane);
        this.addPane(lockedPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private void addLockedPlots() {
        IntStream.rangeClosed(1, this.region.getLockedPlots(PlotType.FARM).size()).forEach(value -> {
            Component name = MiniMessage.miniMessage().deserialize("<color:#38243b>Locked Farm Plot</color>");
            this.lockedPane.addItem(new GuiItem(new SkullBuilder()
                    .displayName(name)
                    .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjcwNWZkOTRhMGM0MzE5MjdmYjRlNjM5YjBmY2ZiNDk3MTdlNDEyMjg1YTAyYjQzOWUwMTEyZGEyMmIyZTJlYyJ9fX0=", null))
                    .build(), event -> event.setCancelled(true)));
        });
    }

    private void addActivePlots() {
        region.getPlots().entrySet().stream()
                .filter(entry -> entry.getKey() == PlotType.FARM)
                .map(Map.Entry::getValue)
                .forEach(plots -> plots.stream()
                        // sort by id, so first plot is always 1, second is 2, etc.
                        .sorted(Comparator.comparingInt(Plot::getId))
                        .map(plot -> ((FarmPlot) plot))
                        .forEach(plot -> {
                            List<Component> lore = new ArrayList<>(plot.getLore());
                            lore.addAll(List.of(
                                    Component.empty(),
                                    Component.text("Click to modify this plot", NamedTextColor.DARK_GRAY)
                            ));
                            this.mainPane.addItem(new GuiItem(
                                    new ItemBuilder(plot.getCurrentlyHarvesting().getItemMaterial())
                                            .displayName(plot.getDisplayName())
                                            .lore(lore)
                                            .build(), event -> new FarmPlotMainMenu(plot, false).show(event.getWhoClicked())
                            ));
                        }));
    }
}
