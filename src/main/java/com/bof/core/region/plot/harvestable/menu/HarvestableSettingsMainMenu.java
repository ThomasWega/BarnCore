package com.bof.core.region.plot.harvestable.menu;

import com.bof.core.item.ItemBuilder;
import com.bof.core.item.SkullBuilder;
import com.bof.core.menu.premade.back.GoBackPane;
import com.bof.core.region.BarnRegion;
import com.bof.core.region.menu.RegionMainMenu;
import com.bof.core.region.plot.harvestable.animal.menu.AnimalSettingsMainMenu;
import com.bof.core.region.plot.harvestable.farm.menu.FarmSettingsMainMenu;
import com.bof.toolkit.skin.Skin;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HarvestableSettingsMainMenu extends ChestGui {
    private final BarnRegion region;
    private final StaticPane mainPane = new StaticPane(1, 1, 7, 1);

    public HarvestableSettingsMainMenu(@NotNull BarnRegion region) {
        super(3, ComponentHolder.of(Component.text("Select plot type")));
        this.region = region;
        this.initialize();
    }

    private void initialize() {
        this.mainPane.addItem(this.getFarmPlotsItem(), 1, 0);
        this.mainPane.addItem(this.getAnimalPlotsItem(), 5, 0);

        this.addPane(new GoBackPane(4, 2, new RegionMainMenu(this.region)));
        this.addPane(this.mainPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private GuiItem getFarmPlotsItem() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#FF8378>Farm Plots</color></b>");
        return new GuiItem(
                new SkullBuilder()
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to modify plots", NamedTextColor.DARK_GRAY)
                        ))
                        .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjY0NTliZTA5OTk4ZTUwYWJkMmNjZjRjZDM4M2U2YjM4YWI1YmM5MDVmYWNiNjZkY2UwZTE0ZTAzOGJhMTk2OCJ9fX0=", null))
                        .build(), event -> new FarmSettingsMainMenu(this.region).show(event.getWhoClicked())
        );
    }

    private GuiItem getAnimalPlotsItem() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#FF8378>Animal Plots</color></b>");
        return new GuiItem(
                new ItemBuilder(Material.OAK_FENCE)
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to modify plots", NamedTextColor.DARK_GRAY)
                        ))
                        .build(), event -> new AnimalSettingsMainMenu(this.region).show(event.getWhoClicked())
        );
    }
}
