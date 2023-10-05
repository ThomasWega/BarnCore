package com.bof.barn.core.menu.premade.harvestable.settings;

import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.menu.premade.LockedSlotItem;
import com.bof.barn.core.menu.premade.UnlockedSlotItem;
import com.bof.barn.core.menu.premade.back.GoBackPane;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.Plot;
import com.bof.barn.core.region.plot.PlotSetting;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.HarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.settings.HarvestableSetting;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@Getter
public class HarvestablePlotSettingMenu<S extends HarvestableSetting> extends ChestGui {
    private final BarnRegion region;
    private final Class<S> setting;
    private final OutlinePane selectedPane = new OutlinePane(1, 1, 7, 2, Pane.Priority.HIGHEST);
    private final OutlinePane unlockedSlotsPane = selectedPane.copy();
    private final OutlinePane lockedSlotsPane = unlockedSlotsPane.copy();
    private final int unlockedSlots;
    private final PlotType plotType;
    private final Gui goBackGui;

    public HarvestablePlotSettingMenu(@NotNull BarnRegion region, @NotNull PlotType plotType, @NotNull Class<S> setting, @Nullable Gui goBackGui) {
        super(4, ComponentHolder.of(Component.text(PlotSetting.getSettingName(setting) + " Menu")));
        this.region = region;
        this.setting = setting;
        this.plotType = plotType;
        this.goBackGui = goBackGui;
        this.unlockedSlots = region.getAutoStoreSlots();
        this.unlockedSlotsPane.setPriority(Pane.Priority.HIGH);
        this.lockedSlotsPane.setPriority(Pane.Priority.NORMAL);
        this.initialize();
    }

    private void initialize() {
        this.addSelectedPlots();
        this.addUnlockedSlots();
        this.addLockedSlots();

        this.addPane(new GoBackPane(4, 3, this.goBackGui));
        this.addPane(selectedPane);
        this.addPane(unlockedSlotsPane);
        this.addPane(lockedSlotsPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private void addSelectedPlots() {
        this.region.getSettingPlots(this.setting).stream()
                .filter(plot -> plot.getType() == this.plotType)
                // sort by id, so first plot is always 1, second is 2, etc.
                .sorted(Comparator.comparingInt(Plot::getId))
                .map(plot -> ((HarvestablePlot<?>) plot))
                .forEach(plot -> {
                            List<Component> lore = new ArrayList<>(plot.getLore());
                            lore.addAll(List.of(
                                    Component.empty(),
                                    Component.text("Click to change plot", NamedTextColor.DARK_GRAY),
                                    Component.text("Shift-click to remove plot", NamedTextColor.DARK_GRAY)
                            ));
                            this.selectedPane.addItem(new GuiItem(
                                    new ItemBuilder(plot.getCurrentlyHarvesting().getItem())
                                            .displayName(plot.getDisplayName())
                                            .lore(lore)
                                            .build(), handleSelectAction(plot)
                            ));
                        }
                );
    }

    private void addUnlockedSlots() {
        IntStream.rangeClosed(1, unlockedSlots).forEach(value ->
                this.unlockedSlotsPane.addItem(new UnlockedSlotItem(this.handleSelectAction(null))));
    }

    private void addLockedSlots() {
        IntStream.rangeClosed(1, this.region.getLockedPlots(this.plotType).size()).forEach(value ->
                this.lockedSlotsPane.addItem(new LockedSlotItem()));
    }

    private Consumer<InventoryClickEvent> handleSelectAction(@Nullable HarvestablePlot<?> plot) {
        return event -> {
            if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                if (plot != null) {
                    plot.setSetting(this.setting, false);
                }
                // this doesn't work for some reason
                // this.update();
                new HarvestablePlotSettingMenu<>(this.region, this.plotType, this.setting, this.goBackGui).show(event.getWhoClicked());
                return;
            }
            // if not click shift, open the select menu
            new HarvestablePlotSettingSetMenu<>(this.region, this.plotType, this, plot).show(event.getWhoClicked());
        };
    }
}
