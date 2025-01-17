package com.bof.barn.core.gui.premade.menu.plot.harvestable.setting;

import com.bof.barn.core.gui.premade.button.back.GoBackPane;
import com.bof.barn.core.gui.premade.button.slot.LockedSlotButton;
import com.bof.barn.core.gui.premade.button.slot.UnlockedSlotButton;
import com.bof.barn.core.gui.premade.sound.SoundedGUIButton;
import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.AbstractHarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.settings.HarvestablePlotSetting;
import com.bof.barn.core.region.plot.setting.PlotSetting;
import com.bof.barn.core.region.setting.SettingState;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;


/**
 * A menu which shows what plots of the same type have the specific setting on and allows
 * to select plots which the setting should be turned on or off for.
 * <p></p>
 * Handles occupied slots, unlocked (free) slots and locked plots
 *
 * @param <S>
 */
@Getter
public class HarvestablePlotSettingGUI<S extends HarvestablePlotSetting> extends ChestGui {
    private final BarnRegion region;
    private final Class<S> settingClazz;
    private final OutlinePane selectedPane = new OutlinePane(1, 1, 7, 2, Pane.Priority.HIGHEST);
    private final OutlinePane unlockedSlotsPane = selectedPane.copy();
    private final OutlinePane lockedSlotsPane = unlockedSlotsPane.copy();
    private final int unlockedSlots = 2;
    private final PlotType plotType;
    private final Gui goBackGui;

    public HarvestablePlotSettingGUI(@NotNull BarnRegion region, @NotNull PlotType plotType, @NotNull Class<S> settingClazz, @Nullable Gui goBackGui) {
        super(4, ComponentHolder.of(Component.text(PlotSetting.getSettingName(settingClazz) + " Menu")));
        this.region = region;
        this.settingClazz = settingClazz;
        this.plotType = plotType;
        this.goBackGui = goBackGui;
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
        this.region.getToggledSettingPlots(this.settingClazz).stream()
                .filter(plot -> plot.getType() == this.plotType)
                // sort by id, so first plot is always 1, second is 2, etc.
                .sorted(Comparator.comparingInt(AbstractPlot::getId))
                .map(plot -> ((AbstractHarvestablePlot<?>) plot))
                .forEach(plot -> {
                            List<Component> lore = new ArrayList<>(plot.getLore());
                            lore.addAll(List.of(
                                    Component.empty(),
                                    Component.text("Click to change plot", NamedTextColor.DARK_GRAY),
                                    Component.text("Shift-click to remove plot", NamedTextColor.DARK_GRAY)
                            ));
                            this.selectedPane.addItem(new SoundedGUIButton(
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
                this.unlockedSlotsPane.addItem(new UnlockedSlotButton(this.handleSelectAction(null))));
    }

    private void addLockedSlots() {
        IntStream.rangeClosed(1, this.region.getLockedPlots(this.plotType).size()).forEach(value ->
                this.lockedSlotsPane.addItem(new LockedSlotButton()));
    }

    private Consumer<InventoryClickEvent> handleSelectAction(@Nullable AbstractHarvestablePlot<?> plot) {
        return event -> {
            if (event.isShiftClick()) {
                if (plot != null) {
                    plot.setSetting(this.settingClazz, SettingState.OFF);
                }
                // this doesn't work for some reason
                // this.update();
                new HarvestablePlotSettingGUI<>(this.region, this.plotType, this.settingClazz, this.goBackGui).show(event.getWhoClicked());
                return;
            }
            // if not click shift, open the select menu
            new HarvestablePlotSettingSetGUI<>(this.region, this.plotType, this, plot).show(event.getWhoClicked());
        };
    }
}
