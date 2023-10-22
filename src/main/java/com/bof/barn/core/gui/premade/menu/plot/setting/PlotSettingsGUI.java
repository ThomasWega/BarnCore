package com.bof.barn.core.gui.premade.menu.plot.setting;

import com.bof.barn.core.gui.premade.button.back.GoBackPane;
import com.bof.barn.core.gui.premade.button.plot.settings.LockedSettingButton;
import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.setting.PlotSetting;
import com.bof.barn.core.region.setting.ChanceSetting;
import com.bof.barn.core.region.setting.TimerSetting;
import com.bof.toolkit.utils.NumberUtils;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Menu which shows upgrades for the given plot
 *
 * @param <T> Plot to get upgrades for
 */
public class PlotSettingsGUI<T extends AbstractPlot> extends ChestGui {
    private final T plot;
    private final Supplier<Gui> goBackGuiSupplier;
    private final OutlinePane mainPane = new OutlinePane(1, 1, 7, 1);

    public PlotSettingsGUI(@NotNull T plot, @NotNull Supplier<Gui> goBackGuiSupplier) {
        super(3, ComponentHolder.of(Component.text(WordUtils.capitalize(plot.getType().getIdentifier()) + " upgrades")));
        this.plot = plot;
        this.goBackGuiSupplier = goBackGuiSupplier;
        this.initialize();
    }

    private void initialize() {
        this.fillWithUpgradeButtons();

        this.addPane(new GoBackPane(4, 2, this.goBackGuiSupplier.get()));
        this.addPane(this.mainPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private void fillWithUpgradeButtons() {
        this.plot.getUnlockedSettings().stream()
                .sorted(Comparator.comparing(PlotSetting::getSettingName))
                .forEach(plotSetting -> {
                    ItemStack displayItem = this.createUnlockedSettingItem(plotSetting);
                    this.mainPane.addItem(new GuiItem(displayItem, this.getUnlockedSettingAction(plotSetting)));
                });

        this.plot.getLockedSettings().stream()
                .sorted(Comparator.comparing(PlotSetting::getSettingName))
                .forEach(plotSetting -> this.mainPane.addItem(
                        new LockedSettingButton(this.plot, plotSetting, this.getLockedSettingAction(plotSetting)))
                );
    }

    private @NotNull ItemStack createUnlockedSettingItem(PlotSetting setting) {
        ItemBuilder builder = new ItemBuilder(setting.getItem())
                .appendLoreLine(Component.empty())
                .appendLoreLine(Component.text("Status: ", NamedTextColor.WHITE).append(setting.isToggled()
                        ? Component.text("ON", NamedTextColor.GREEN)
                        : Component.text("OFF", NamedTextColor.RED)
                ));


        if (setting instanceof ChanceSetting chanceSetting) {
            builder.appendLoreLine(Component.empty())
                    .appendLoreLine(Component.text("Chance: " + NumberUtils.roundBy(chanceSetting.getCurrentChance(), 2) + "%", NamedTextColor.WHITE));

            if (!setting.isAtMaxLevel()) {
                builder.appendLoreLine(Component.text("Next Chance: " + NumberUtils.roundBy(chanceSetting.getNextChance(), 2) + "%", NamedTextColor.WHITE));
            }
        }

        if (setting instanceof TimerSetting timerSetting) {
            builder.appendLoreLine(Component.empty())
                    .appendLoreLine(Component.text("Interval: " + NumberUtils.roundBy((float) timerSetting.getCurrentTickSpeed() / 20, 2) + "s", NamedTextColor.WHITE));

            if (!setting.isAtMaxLevel()) {
                builder.appendLoreLine(Component.text("Next Interval: " + NumberUtils.roundBy((float) timerSetting.getNextTickSpeed() / 20, 2) + "s", NamedTextColor.WHITE));
            }
        }

        builder.appendLoreLine(Component.empty())
                .appendLoreLine(Component.text("Level: " + setting.getCurrentLevel() + "/" + setting.getMaxLevel(), NamedTextColor.WHITE));

        if (!setting.isAtMaxLevel()) {
            builder
                    .appendLoreLine(Component.text("Next level price: " + setting.getNextLevelPrice(), NamedTextColor.WHITE))
                    .appendLoreLine(Component.text("Your balance: " + plot.getOwningRegion().getFarmCoinsRounded(2) + "$", NamedTextColor.WHITE))
                    .appendLoreLine(Component.empty())
                    .appendLoreLine(Component.text("Shift-click to upgrade level", NamedTextColor.YELLOW));
        }

        return builder
                .appendLoreLine(Component.text("Click to change status", NamedTextColor.GREEN))
                .build();
    }

    private @NotNull Consumer<InventoryClickEvent> getUnlockedSettingAction(PlotSetting setting) {
        return event -> {
            Player player = ((Player) event.getWhoClicked());
            if (event.isShiftClick() && !setting.isAtMaxLevel()) {
                if (this.plot.getOwningRegion().hasEnoughCoins(setting.getNextLevelPrice())) {
                    setting.upgradeLevel(this.plot);
                    this.plot.getOwningRegion().removeFarmCoins(setting.getNextLevelPrice());
                    player.sendMessage(Component.text("TO ADD - purchased next level for upgrade " + setting.getSettingName()));
                } else {
                    player.sendMessage(Component.text("TO ADD - You don't have enough coins"));
                }
            } else {
                player.sendMessage(Component.text("TO ADD - Switched " + setting.getSettingName() + " for this plot to " + plot.switchSettingToggle(setting.getClass())));
            }
            new PlotSettingsGUI<>(this.plot, this.goBackGuiSupplier).show(event.getWhoClicked());
            this.plot.updateHologram();
            event.setCancelled(true);
        };
    }

    private @NotNull Consumer<InventoryClickEvent> getLockedSettingAction(PlotSetting plotSetting) {
        return event -> {
            if (!event.isShiftClick()) return;
            float price = plotSetting.getPrice();
            Player player = ((Player) event.getWhoClicked());
            if (this.plot.getOwningRegion().hasEnoughCoins(price)) {
                plotSetting.setUnlocked(true);
                this.plot.getOwningRegion().removeFarmCoins(price);
                player.sendMessage(Component.text("TO ADD - purchased upgrade " + plotSetting.getSettingName()));
            } else {
                player.sendMessage(Component.text("TO ADD - You don't have enough coins"));
            }
            new PlotSettingsGUI<>(this.plot, this.goBackGuiSupplier).show(event.getWhoClicked());
            this.plot.updateHologram();
            event.setCancelled(true);
        };
    }
}
