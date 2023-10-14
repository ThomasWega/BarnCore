package com.bof.barn.core.gui.premade.menu.harvestable.settings;

import com.bof.barn.core.gui.premade.sound.SoundedGUIButton;
import com.bof.barn.core.gui.premade.button.plot.LockedPlotButton;
import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.gui.premade.button.back.GoBackPane;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.Plot;
import com.bof.barn.core.region.plot.setting.PlotSetting;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.HarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.setting.AutoStoreSetting;
import com.bof.barn.core.region.plot.harvestable.setting.HarvestablePlotSetting;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
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

/**
 * A menu which allows to set a specific setting for a plot which don't have this setting yet.
 * Will handle disabling the setting if any previous plot had that and was replaced by the current one.
 * <p></p>
 * Basically just a list of clickable plots which don't have the setting yet, and clicking on them will turn the setting on
 *
 * @param <S> Main setting menu
 */
public class HarvestablePlotSettingSetGUI<S extends HarvestablePlotSettingGUI<? extends HarvestablePlotSetting>> extends ChestGui {
    private final BarnRegion region;
    private final OutlinePane mainPane = new OutlinePane(1, 1, 7, 2, Pane.Priority.NORMAL);
    private final OutlinePane lockedPane = mainPane.copy();
    @Nullable
    private final HarvestablePlot<?> previousSelectedPlot;
    private final PlotType plotType;
    private final S mainSettingMenu;

    public HarvestablePlotSettingSetGUI(@NotNull BarnRegion region, @NotNull PlotType plotType, @NotNull S mainSettingMenu, @Nullable HarvestablePlot<?> previousSelectedPlot) {
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
                this.lockedPane.addItem(new LockedPlotButton(this.plotType)));
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
                    this.mainPane.addItem(new SoundedGUIButton(
                            new ItemBuilder(plot.getCurrentlyHarvesting().getItem())
                                    .displayName(plot.getDisplayName())
                                    .lore(lore)
                                    .build(),
                            event -> {
                                if (this.previousSelectedPlot != null) {
                                    this.previousSelectedPlot.setSetting(this.mainSettingMenu.getSetting(), false);
                                }
                                plot.setSetting(this.mainSettingMenu.getSetting(), true);
                                new HarvestablePlotSettingGUI<>(this.region, this.plotType, this.mainSettingMenu.getSetting(), this.mainSettingMenu.getGoBackGui())
                                        .show(event.getWhoClicked());
                            }
                    ));
                });
    }
}
