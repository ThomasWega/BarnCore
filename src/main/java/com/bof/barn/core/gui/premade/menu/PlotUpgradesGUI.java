package com.bof.barn.core.gui.premade.menu;

import com.bof.barn.core.gui.premade.button.back.GoBackPane;
import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.region.plot.Plot;
import com.bof.barn.core.region.plot.PlotSetting;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

/**
 * Menu which shows upgrades for the given plot
 * @param <T> Plot to get upgrades for
 */
public class PlotUpgradesGUI<T extends Plot> extends ChestGui {
    private final T plot;
    private final Gui goBackGui;
    private final OutlinePane mainPane = new OutlinePane(1, 1, 7, 1);

    public PlotUpgradesGUI(@NotNull T plot, @Nullable Gui goBackGui) {
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
        this.plot.getSettings().values().stream()
                .sorted(Comparator.comparing(PlotSetting::getSettingName))
                .forEach(plotSetting -> {
                    ItemStack displayItem = new ItemBuilder(plotSetting.getItem())
                            .appendLoreLine(Component.empty())
                            .appendLoreLine(plotSetting.isToggled()
                                    ? Component.text("ON", NamedTextColor.GREEN)
                                    : Component.text("OFF", NamedTextColor.RED)
                            )
                            .build();
                    this.mainPane.addItem(new GuiItem(displayItem, event -> {
                        Player player = ((Player) event.getWhoClicked());
                        player.sendMessage(Component.text("TO ADD - Switched " + plotSetting.getSettingName() + " for this plot to " + plot.switchSettingToggle(plotSetting.getClass())));
                        player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                        this.plot.updateHologram();
                    }));
                });
    }
}
