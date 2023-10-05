package com.bof.barn.core.menu.premade.harvestable.settings;

import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.menu.premade.LockedPlotItem;
import com.bof.barn.core.menu.premade.back.GoBackPane;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.Plot;
import com.bof.barn.core.region.plot.PlotSetting;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.HarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.settings.AutoStoreSetting;
import com.bof.barn.core.region.plot.harvestable.settings.HarvestableSetting;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class HarvestablePlotSettingSetMenu<S extends HarvestablePlotSettingMenu<? extends HarvestableSetting>> extends ChestGui {
    private final BarnRegion region;
    private final OutlinePane mainPane = new OutlinePane(1, 1, 7, 2, Pane.Priority.NORMAL);
    private final OutlinePane lockedPane = mainPane.copy();
    @Nullable
    private final HarvestablePlot<?> previousSelectedPlot;
    private final PlotType plotType;
    private final S mainSettingMenu;

    public HarvestablePlotSettingSetMenu(@NotNull BarnRegion region, @NotNull PlotType plotType, @NotNull S mainSettingMenu, @Nullable HarvestablePlot<?> previousSelectedPlot) {
        super(4, ComponentHolder.of(Component.text("Toggle " + PlotSetting.getSettingName(mainSettingMenu.getSetting()) + " for plot")));
        this.region = region;
        this.plotType = plotType;
        this.mainSettingMenu = mainSettingMenu;
        this.previousSelectedPlot = previousSelectedPlot;
        this.lockedPane.setPriority(Pane.Priority.LOW);
        this.initialize();
    }

    private void initialize() {
        this.addLockedPlots();
        this.addSettablePlots();

        this.addPane(new GoBackPane(4, 3, mainSettingMenu));
        this.addPane(mainPane);
        this.addPane(lockedPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private void addLockedPlots() {
        IntStream.rangeClosed(1, this.region.getLockedPlots(this.plotType).size()).forEach(value ->
                this.lockedPane.addItem(new LockedPlotItem(this.plotType)));
    }

    private void addSettablePlots() {
        this.region.getSettingPlots(AutoStoreSetting.class, false).stream()
                .filter(plot -> plot.getType() == this.plotType)
                // sort by id, so first plot is always 1, second is 2, etc.
                .sorted(Comparator.comparingInt(Plot::getId))
                .map(plot -> ((HarvestablePlot<?>) plot))
                .forEach(plot -> {
                    List<Component> lore = new ArrayList<>(plot.getLore());
                    lore.addAll(List.of(
                            Component.empty(),
                            Component.text("Click to select this plot", NamedTextColor.DARK_GRAY)
                    ));
                    this.mainPane.addItem(new GuiItem(
                            new ItemBuilder(plot.getCurrentlyHarvesting().getItem())
                                    .displayName(plot.getDisplayName())
                                    .lore(lore)
                                    .build(),
                            event -> {
                                if (this.previousSelectedPlot != null) {
                                    this.previousSelectedPlot.setSetting(this.mainSettingMenu.getSetting(), false);
                                }
                                plot.setSetting(this.mainSettingMenu.getSetting(), true);
                                new HarvestablePlotSettingMenu<>(this.region, this.plotType, this.mainSettingMenu.getSetting(), this.mainSettingMenu.getGoBackGui())
                                        .show(event.getWhoClicked());
                            }
                    ));
                });
    }
}
