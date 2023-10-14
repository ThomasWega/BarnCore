package com.bof.barn.core.region.plot.harvestable.farm.menu.upgrades;

import com.bof.barn.core.gui.premade.button.back.GoBackPane;
import com.bof.barn.core.gui.premade.button.plot.settings.AutoStoreSettingButton;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.harvestable.menu.HarvestableUpgradesMainMenu;
import com.bof.barn.core.region.plot.harvestable.setting.AutoStoreSetting;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FarmUpgradesMenu extends ChestGui {
    private final BarnRegion region;
    private final StaticPane mainPane = new StaticPane(1, 1, 7, 1);

    public FarmUpgradesMenu(@NotNull BarnRegion region) {
        super(3, ComponentHolder.of(Component.text("Select setting type")));
        this.region = region;
        this.initialize();
    }

    private void initialize() {
        this.mainPane.addItem(this.getAutoStoreButton(), 1, 0);

        this.addPane(new GoBackPane(4, 2, new HarvestableUpgradesMainMenu(this.region)));
        this.addPane(this.mainPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private GuiItem getAutoStoreButton() {
        Component unlockedPlots = MiniMessage.miniMessage().deserialize("<white>Unlocked Slots: <green>" + this.region.getSettingPlotsCount(AutoStoreSetting.class) + "/TO ADD</green></white>");
        List<Component> lore = List.of(
                Component.text("Automatically puts crops into the silo", NamedTextColor.GRAY),
                Component.empty(),
                unlockedPlots,
                Component.empty(),
                Component.text("Click to select plots", NamedTextColor.DARK_GRAY)
        );
        return new AutoStoreSettingButton(lore, event -> new FarmUpgradeAutoStoreMenu(this.region).show(event.getWhoClicked()));
    }
}
