package com.bof.barn.core.region.plot.harvestable.farm.menu;

import com.bof.barn.core.item.SkullBuilder;
import com.bof.barn.core.menu.premade.back.GoBackPane;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.harvestable.menu.HarvestableSettingsMainMenu;
import com.bof.barn.core.region.plot.harvestable.settings.AutoStoreSetting;
import com.bof.toolkit.skin.Skin;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FarmSettingsMainMenu extends ChestGui {
    private final BarnRegion region;
    private final StaticPane mainPane = new StaticPane(1, 1, 7, 1);

    public FarmSettingsMainMenu(@NotNull BarnRegion region) {
        super(3, ComponentHolder.of(Component.text("Select setting type")));
        this.region = region;
        this.initialize();
    }

    private void initialize() {
        this.mainPane.addItem(this.getAutoStoreItem(), 1, 0);

        this.addPane(new GoBackPane(4, 2, new HarvestableSettingsMainMenu(this.region)));
        this.addPane(this.mainPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private GuiItem getAutoStoreItem() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#2b84ff>Auto Store</color></b>");
        Component unlockedPlots = MiniMessage.miniMessage().deserialize("<white>Unlocked Slots: <green>" + this.region.getSettingPlotsCount(AutoStoreSetting.class) + "/" + this.region.getAutoStoreSlots() + "</green></white>");
        return new GuiItem(new SkullBuilder()
                .displayName(name)
                .lore(List.of(
                        Component.text("Automatically puts crops into the silo", NamedTextColor.GRAY),
                        Component.empty(),
                        unlockedPlots,
                        Component.empty(),
                        Component.text("Click to select plots", NamedTextColor.DARK_GRAY)
                ))
                .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTVhMGIwN2UzNmVhZmRlY2YwNTljOGNiMTM0YTdiZjBhMTY3ZjkwMDk2NmYxMDk5MjUyZDkwMzI3NjQ2MWNjZSJ9fX0=", null))
                .hideFlags()
                .build(),
                event -> new FarmAutoStoreMenu(this.region).show(event.getWhoClicked())
        );
    }
}
