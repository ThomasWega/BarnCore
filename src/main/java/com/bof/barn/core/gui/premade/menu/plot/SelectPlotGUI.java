package com.bof.barn.core.gui.premade.menu.plot;

import com.bof.barn.core.gui.premade.button.back.GoBackPane;
import com.bof.barn.core.gui.premade.button.plot.HarvestablePlotButton;
import com.bof.barn.core.gui.premade.button.plot.LockedPlotButton;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.AbstractHarvestablePlot;
import com.github.stefvanschie.inventoryframework.adventuresupport.TextHolder;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Menu which prompts to select plots of a specific type.
 * In other words, a menu which has all the plots of the specific type and
 * the one you click on will open the specific plot's menu
 */
public abstract class SelectPlotGUI extends ChestGui {
    private final BarnRegion region;
    private final OutlinePane mainPane;
    private final OutlinePane lockedPane;
    private final Gui goBackGui;
    private final PlotType plotType;

    public SelectPlotGUI(@NotNull BarnRegion region, @NotNull PlotType plotType, @Nullable Gui goBackGui, int rows, int x, int y, int length, int height, @NotNull TextHolder title) {
        super(rows, title);
        this.region = region;
        this.mainPane = new OutlinePane(x, y, length, height, Pane.Priority.NORMAL);
        this.lockedPane = mainPane.copy();
        this.lockedPane.setPriority(Pane.Priority.LOW);
        this.plotType = plotType;
        this.goBackGui = goBackGui;
        this.initialize();
    }

    private void initialize() {
        this.addLockedPlots();
        this.addActivePlots();

        this.addPane(new GoBackPane(4, this.getRows() - 1, goBackGui));
        this.addPane(mainPane);
        this.addPane(lockedPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private void addLockedPlots() {
        IntStream.rangeClosed(1, this.region.getLockedPlots(plotType).size()).forEach(value -> this.lockedPane.addItem(new LockedPlotButton(plotType)));
    }

    private void addActivePlots() {
        region.getPlots().get(plotType).stream()
                // sort by id, so first plot is always 1, second is 2, etc.
                .sorted(Comparator.comparingInt(AbstractPlot::getId))
                .map(plot -> ((AbstractHarvestablePlot<?>) plot))
                .forEach(plot -> {
                    Consumer<List<Component>> loreAdd = lore -> lore.addAll(List.of(
                            Component.empty(),
                            Component.text("Click to open this plot", NamedTextColor.DARK_GRAY)
                    ));
                    this.mainPane.addItem(new HarvestablePlotButton(plot, loreAdd,
                            event -> this.getActivePlotMenu(plot).show(event.getWhoClicked()))
                    );
                });
    }


    public abstract Gui getActivePlotMenu(@NotNull AbstractHarvestablePlot<?> plot);
}
