package com.bof.core.region.plot.harvestable.animal.menu;

import com.bof.core.item.ItemBuilder;
import com.bof.core.item.SkullBuilder;
import com.bof.core.menu.premade.back.GoBackPane;
import com.bof.core.region.BarnRegion;
import com.bof.core.region.plot.Plot;
import com.bof.core.region.plot.PlotType;
import com.bof.core.region.plot.harvestable.animal.AnimalPlot;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class AnimalAutoStoreSetMenu extends ChestGui {
    private final BarnRegion region;
    private final OutlinePane mainPane = new OutlinePane(1, 1, 7, 2);
    private final OutlinePane lockedPane = mainPane.copy();
    @Nullable
    private final AnimalPlot previousSelectedPlot;

    public AnimalAutoStoreSetMenu(@NotNull BarnRegion region, @Nullable AnimalPlot previousSelectedPlot) {
        super(4, ComponentHolder.of(Component.text("Select plot to Auto Store")));
        this.region = region;
        this.previousSelectedPlot = previousSelectedPlot;
        this.initialize();
    }

    private void initialize() {
        this.mainPane.setPriority(Pane.Priority.NORMAL);
        this.lockedPane.setPriority(Pane.Priority.LOW);

        this.addLockedPlots();
        this.addSettablePlots();

        this.addPane(new GoBackPane(4, 3, new AnimalAutoStoreMenu(this.region)));
        this.addPane(mainPane);
        this.addPane(lockedPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private void addLockedPlots() {
        IntStream.rangeClosed(1, this.region.getLockedPlots(PlotType.ANIMAL).size()).forEach(value -> {
            Component name = MiniMessage.miniMessage().deserialize("<color:#38243b>Locked Animal Plot</color>");
            this.lockedPane.addItem(new GuiItem(new SkullBuilder()
                    .displayName(name)
                    .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjcwNWZkOTRhMGM0MzE5MjdmYjRlNjM5YjBmY2ZiNDk3MTdlNDEyMjg1YTAyYjQzOWUwMTEyZGEyMmIyZTJlYyJ9fX0=", null))
                    .build(), event -> event.setCancelled(true)));
        });
    }

    private void addSettablePlots() {
        this.region.getNonAutoStorePlots().stream()
                .filter(plot -> plot instanceof AnimalPlot)
                // sort by id, so first plot is always 1, second is 2, etc.
                .sorted(Comparator.comparingInt(Plot::getId))
                .map(plot -> ((AnimalPlot) plot))
                .forEach(plot -> {
                    List<Component> lore = new ArrayList<>(plot.getLore());
                    lore.addAll(List.of(
                            Component.empty(),
                            Component.text("Click to select this plot plot", NamedTextColor.DARK_GRAY)
                    ));
                    this.mainPane.addItem(new GuiItem(
                            new ItemBuilder(plot.getCurrentlyHarvesting().getItem())
                                    .displayName(plot.getDisplayName())
                                    .lore(lore)
                                    .build(),
                            event -> {
                                if (previousSelectedPlot != null) {
                                    previousSelectedPlot.setAutoStore(false);
                                }
                                plot.setAutoStore(true);
                                new AnimalAutoStoreMenu(region).show(event.getWhoClicked());
                            }
                    ));
                });
    }
}
