package com.bof.barn.core.gui.premade.menu.plot.upgrades;

import com.bof.barn.core.gui.premade.button.back.GoBackPane;
import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.item.SkullBuilder;
import com.bof.barn.core.region.plot.Plot;
import com.bof.barn.core.region.plot.setting.PlotSetting;
import com.bof.barn.core.region.setting.LeveledSetting;
import com.bof.toolkit.skin.Skin;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.function.Consumer;

/**
 * Menu which shows upgrades for the given plot
 *
 * @param <T> Plot to get upgrades for
 */
public class PlotUpgradesMenuGUI<T extends Plot> extends ChestGui {
    private final T plot;
    private final Gui goBackGui;
    private final OutlinePane mainPane = new OutlinePane(1, 1, 7, 1);

    public PlotUpgradesMenuGUI(@NotNull T plot, @Nullable Gui goBackGui) {
        super(3, ComponentHolder.of(Component.text(WordUtils.capitalize(plot.getType().getIdentifier()) + " upgrades")));
        this.plot = plot;
        this.goBackGui = goBackGui;
        this.initialize();
    }

    private void initialize() {
        this.fillWithUpgradeButtons();

        this.addPane(new GoBackPane(4, 2, this.goBackGui));
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
                .forEach(plotSetting -> {
                    ItemStack displayItem = this.createLockedSettingItem(plotSetting);
                    this.mainPane.addItem(new GuiItem(displayItem, this.getLockedSettingAction(plotSetting)));
                });
    }

    private @NotNull ItemStack createUnlockedSettingItem(PlotSetting plotSetting) {
        ItemBuilder displayItemBuilder = new ItemBuilder(plotSetting.getItem());

        displayItemBuilder.appendLoreLine(Component.empty())
                .appendLoreLine(Component.text("Status: ", NamedTextColor.WHITE).append(plotSetting.isToggled()
                        ? Component.text("ON", NamedTextColor.GREEN)
                        : Component.text("OFF", NamedTextColor.RED)
                ));

        if (plotSetting instanceof LeveledSetting levelSetting) {
            displayItemBuilder.appendLoreLine(Component.empty())
                    .appendLoreLine(Component.text("Level: " + levelSetting.getCurrentLevel() + "/" + levelSetting.getMaxLevel(), NamedTextColor.WHITE));

            if (!levelSetting.isAtMaxLevel()) {
                displayItemBuilder
                        .appendLoreLine(Component.text("Next level price: " + levelSetting.getNextLevelPrice(), NamedTextColor.WHITE))
                        .appendLoreLine(Component.text("Your balance: " + plot.getOwningRegion().getFarmCoinsRounded(2) + "$", NamedTextColor.WHITE))
                        .appendLoreLine(Component.empty())
                        .appendLoreLine(Component.text("Shift-click to upgrade level", NamedTextColor.YELLOW));
            }
        }

        return displayItemBuilder
                .appendLoreLine(Component.text("Click to change status", NamedTextColor.GREEN))
                .build();
    }

    private @NotNull Consumer<InventoryClickEvent> getUnlockedSettingAction(PlotSetting plotSetting) {
        return event -> {
            Player player = ((Player) event.getWhoClicked());
            if (event.isShiftClick() && plotSetting instanceof LeveledSetting levelSetting && !levelSetting.isAtMaxLevel()) {
                if (this.plot.getOwningRegion().hasEnoughCoins(levelSetting.getNextLevelPrice())) {
                    levelSetting.upgradeLevel();
                    this.plot.getOwningRegion().removeFarmCoins(levelSetting.getNextLevelPrice());
                    player.sendMessage(Component.text("TO ADD - purchased next level for upgrade " + plotSetting.getSettingName()));
                } else {
                    player.sendMessage(Component.text("TO ADD - You don't have enough coins"));
                }
            } else {
                player.sendMessage(Component.text("TO ADD - Switched " + plotSetting.getSettingName() + " for this plot to " + plot.switchSettingToggle(plotSetting.getClass())));
            }
            new PlotUpgradesMenuGUI<>(this.plot, this.goBackGui).show(event.getWhoClicked());
            this.plot.updateHologram();
            event.setCancelled(true);
        };
    }

    private @NotNull ItemStack createLockedSettingItem(PlotSetting plotSetting) {
        // use the ItemStack but change the material
        ItemStack displayItem = new SkullBuilder(new ItemBuilder(plotSetting.getItem()).material(Material.PLAYER_HEAD))
                .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODE2MjNkNTIzOGRhYjdkZWNkMzIwMjY1Y2FlMWRjNmNhOTFiN2ZhOTVmMzQ2NzNhYWY0YjNhZDVjNmJhMTZlMSJ9fX0=", null))
                .appendLoreLine(Component.empty())
                .appendLoreLine(Component.text("Price: " + plotSetting.getPrice() + "$", NamedTextColor.WHITE))
                .appendLoreLine(Component.text("Your balance: " + plot.getOwningRegion().getFarmCoinsRounded(2) + "$", NamedTextColor.WHITE))
                .appendLoreLine(Component.empty())
                .appendLoreLine(Component.text("Shift-click to purchase this upgrade", NamedTextColor.RED))
                .build();

        return displayItem;
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
            new PlotUpgradesMenuGUI<>(this.plot, this.goBackGui).show(event.getWhoClicked());
            this.plot.updateHologram();
            event.setCancelled(true);
        };
    }
}
