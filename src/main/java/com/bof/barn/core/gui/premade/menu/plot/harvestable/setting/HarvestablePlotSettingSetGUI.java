package com.bof.barn.core.gui.premade.menu.plot.harvestable.setting;

import com.bof.barn.core.gui.premade.button.back.GoBackPane;
import com.bof.barn.core.gui.premade.button.plot.settings.LockedSettingButton;
import com.bof.barn.core.gui.premade.sound.SoundedGUIButton;
import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.AbstractHarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.settings.HarvestablePlotSetting;
import com.bof.barn.core.region.plot.setting.PlotSetting;
import com.bof.barn.core.region.setting.SettingManager;
import com.bof.barn.core.region.setting.SettingState;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.function.Consumer;

/**
 * A menu which allows to set a specific setting for a plot which don't have this setting yet.
 * Will handle disabling the setting if any previous plot had that and was replaced by the current one.
 * <p></p>
 * Basically just a list of clickable plots which don't have the setting yet, and clicking on them will turn the setting on
 *
 * @param <S> Main setting menu
 */
class HarvestablePlotSettingSetGUI<S extends HarvestablePlotSettingGUI<? extends HarvestablePlotSetting>> extends ChestGui {
    private final BarnRegion region;
    private final OutlinePane mainPane = new OutlinePane(1, 1, 7, 2, Pane.Priority.NORMAL);
    @Nullable
    private final AbstractHarvestablePlot<?> previousSelectedPlot;
    private final PlotType plotType;
    private final S mainSettingMenu;
    private final Class<? extends HarvestablePlotSetting> settingClazz;
    private final SettingManager settingManager;

    public HarvestablePlotSettingSetGUI(@NotNull BarnRegion region, @NotNull PlotType plotType, @NotNull S mainSettingMenu, @Nullable AbstractHarvestablePlot<?> previousSelectedPlot) {
        super(4, ComponentHolder.of(Component.text("Toggle " + PlotSetting.getSettingName(mainSettingMenu.getSettingClazz()) + " for plot")));
        this.region = region;
        this.plotType = plotType;
        this.mainSettingMenu = mainSettingMenu;
        this.settingClazz = mainSettingMenu.getSettingClazz();
        this.settingManager = region.getPlugin().getSettingManager();
        this.previousSelectedPlot = previousSelectedPlot;
        this.initialize();
    }

    private void initialize() {
        this.addLockedSettingPlots();
        this.addUnToggledSettingPlots();

        this.addPane(new GoBackPane(4, 3, this.mainSettingMenu));
        this.addPane(this.mainPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private void addLockedSettingPlots() {
        this.region.getLockedSettingPlots(this.settingClazz).stream()
                .filter(plot -> plot.getType() == this.plotType)
                // sort by id, so first plot is always 1, second is 2, etc.
                .sorted(Comparator.comparingInt(AbstractPlot::getId))
                .map(plot -> ((AbstractHarvestablePlot<?>) plot))
                .forEach(plot -> {
                    PlotSetting setting = plot.getSetting(this.settingClazz);
                    this.mainPane.addItem(new LockedSettingButton(plot, setting, this.getLockedSettingAction(plot, setting)));
                });
    }

    private void addUnToggledSettingPlots() {
        this.region.getUnToggledSettingPlots(this.settingClazz).stream()
                .filter(plot -> plot.getType() == this.plotType)
                // sort by id, so first plot is always 1, second is 2, etc.
                .sorted(Comparator.comparingInt(AbstractPlot::getId))
                .map(plot -> ((AbstractHarvestablePlot<?>) plot))
                .forEach(plot -> {
                    PlotSetting setting = plot.getSetting(this.settingClazz);
                    ItemBuilder builder = new ItemBuilder(plot.getCurrentlyHarvesting().getItem())
                            .displayName(plot.getDisplayName());
                    ItemStack item = PlotSetting.getBuilderWithInfo(plot, setting, builder).build();

                    this.mainPane.addItem(new SoundedGUIButton(item, this.getUnlockedSettingAction(plot, setting)));
                });
    }

    private @NotNull Consumer<InventoryClickEvent> getLockedSettingAction(AbstractPlot plot, PlotSetting plotSetting) {
        return event -> {
            if (!event.isShiftClick()) return;
            Player player = ((Player) event.getWhoClicked());
            if (this.settingManager.unlockSetting(plot, plotSetting)) {
                player.sendMessage(Component.text("TO ADD - purchased upgrade " + plotSetting.getSettingName()));
            } else {
                player.sendMessage(Component.text("TO ADD - You don't have enough coins"));
            }
            new HarvestablePlotSettingSetGUI<>(this.region, this.plotType, this.mainSettingMenu, this.previousSelectedPlot).show(player);
            plot.updateHologram();
            event.setCancelled(true);
        };
    }

    private @NotNull Consumer<InventoryClickEvent> getUnlockedSettingAction(AbstractPlot plot, PlotSetting setting) {
        return event -> {
            Player player = ((Player) event.getWhoClicked());
            if (event.isShiftClick() && !setting.isAtMaxLevel()) {
                if (this.settingManager.purchaseSetting(plot, setting)) {
                    player.sendMessage(Component.text("TO ADD - purchased next level for upgrade " + setting.getSettingName()));
                    new HarvestablePlotSettingSetGUI<>(this.region, this.plotType, this.mainSettingMenu, this.previousSelectedPlot).show(player);
                } else {
                    player.sendMessage(Component.text("TO ADD - You don't have enough coins"));
                }
            } else {
                if (this.previousSelectedPlot != null) {
                    this.previousSelectedPlot.setSetting(this.settingClazz, SettingState.OFF);
                }
                plot.setSetting(this.settingClazz, SettingState.ON);
                new HarvestablePlotSettingGUI<>(this.region, this.plotType, this.settingClazz, this.mainSettingMenu.getGoBackGui())
                        .show(event.getWhoClicked());
            }
            plot.updateHologram();
            event.setCancelled(true);
        };
    }
}
