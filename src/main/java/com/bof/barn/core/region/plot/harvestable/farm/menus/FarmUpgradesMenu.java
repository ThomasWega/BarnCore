package com.bof.barn.core.region.plot.harvestable.farm.menus;

import com.bof.barn.core.gui.premade.button.back.GoBackPane;
import com.bof.barn.core.gui.premade.button.plot.settings.impl.AutoHarvestSettingButton;
import com.bof.barn.core.gui.premade.button.plot.settings.impl.AutoStoreSettingButton;
import com.bof.barn.core.gui.premade.button.plot.settings.impl.ReplantAllSettingButton;
import com.bof.barn.core.gui.premade.menu.plot.harvestable.setting.HarvestablePlotSettingGUI;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.menus.HarvestableUpgradesMainMenu;
import com.bof.barn.core.region.plot.harvestable.settings.impl.AutoHarvestSetting;
import com.bof.barn.core.region.plot.harvestable.settings.impl.AutoStoreSetting;
import com.bof.barn.core.region.plot.harvestable.settings.impl.ReplantAllSetting;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FarmUpgradesMenu extends ChestGui {
    private final BarnRegion region;
    private final StaticPane mainPane = new StaticPane(1, 1, 7, 1);
    private final PlotType type = PlotType.FARM;

    public FarmUpgradesMenu(@NotNull BarnRegion region) {
        super(3, ComponentHolder.of(Component.text("Select setting type")));
        this.region = region;
        this.initialize();
    }

    private void initialize() {
        this.mainPane.addItem(this.getAutoStoreButton(), 1, 0);
        this.mainPane.addItem(this.getAutoHarvestButton(), 2, 0);
        this.mainPane.addItem(this.getReplantAllButton(), 3, 0);

        this.addPane(new GoBackPane(4, 2, new HarvestableUpgradesMainMenu(this.region)));
        this.addPane(this.mainPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private GuiItem getAutoStoreButton() {
        List<Component> lore = List.of(
                Component.empty(),
                Component.text("Click to select plots", NamedTextColor.DARK_GRAY)
        );

        return new AutoStoreSettingButton(lore, event -> new HarvestablePlotSettingGUI<>(this.region, this.type, AutoStoreSetting.class, new FarmUpgradesMenu(this.region)).show(event.getWhoClicked()));
    }

    private GuiItem getAutoHarvestButton() {
        List<Component> lore = List.of(
                Component.empty(),
                Component.text("Click to select plots", NamedTextColor.DARK_GRAY)
        );
        return new AutoHarvestSettingButton(lore, event -> new HarvestablePlotSettingGUI<>(this.region, this.type, AutoHarvestSetting.class, new FarmUpgradesMenu(this.region)).show(event.getWhoClicked()));
    }

    private GuiItem getReplantAllButton() {
        List<Component> lore = List.of(
                Component.empty(),
                Component.text("Click to select plots", NamedTextColor.DARK_GRAY)
        );
        return new ReplantAllSettingButton(lore, event -> new HarvestablePlotSettingGUI<>(this.region, this.type, ReplantAllSetting.class, new FarmUpgradesMenu(this.region)).show(event.getWhoClicked()));
    }
}
