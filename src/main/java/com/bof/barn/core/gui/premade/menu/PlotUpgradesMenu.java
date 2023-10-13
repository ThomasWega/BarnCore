package com.bof.barn.core.gui.premade.menu;

import com.bof.barn.core.gui.premade.button.back.GoBackPane;
import com.bof.barn.core.region.plot.Plot;
import com.bof.barn.core.region.plot.PlotSetting;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.WordUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlotUpgradesMenu<T extends Plot> extends ChestGui {
    private final T plot;
    private final Gui goBackGui;
    private final OutlinePane mainPane = new OutlinePane(1, 1, 7, 1);

    public PlotUpgradesMenu(@NotNull T plot, @Nullable Gui goBackGui) {
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
                .map(PlotSetting::getItem)
                .forEach(item -> this.mainPane.addItem(new GuiItem(item)));
    }
}
