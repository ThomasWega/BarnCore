package com.bof.core.region.menu;

import com.bof.core.item.ItemBuilder;
import com.bof.core.item.SkullBuilder;
import com.bof.core.menu.premade.back.GoBackPane;
import com.bof.core.region.BarnRegion;
import com.bof.core.region.plot.Plot;
import com.bof.core.region.plot.PlotType;
import com.bof.core.region.plot.harvestable.animal.menu.AnimalMainMenu;
import com.bof.core.region.plot.harvestable.farm.menu.FarmMainMenu;
import com.bof.core.region.plot.harvestable.menu.HarvestableAutoStoreMainMenu;
import com.bof.core.region.plot.selling.barn.BarnPlot;
import com.bof.core.region.plot.selling.barn.menu.BarnPlotMainMenu;
import com.bof.core.region.plot.selling.silo.SiloPlot;
import com.bof.core.region.plot.selling.silo.menu.SiloPlotMainMenu;
import com.bof.core.skin.Skin;
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

public class RegionMainMenu extends ChestGui {
    private final BarnRegion region;
    private final StaticPane mainPane = new StaticPane(1, 1, 7, 2);

    public RegionMainMenu(@NotNull BarnRegion region) {
        super(4, ComponentHolder.of(Component.text("Main Menu")));
        this.region = region;
        this.initialize();
    }

    private void initialize() {
        this.mainPane.addItem(this.getFarmPlotsItem(), 0, 0);
        this.mainPane.addItem(this.getAnimalPlotsItem(), 3, 0);
        this.mainPane.addItem(this.getSiloPlotItem(), 6, 0);

        this.mainPane.addItem(this.getBarnPlotItem(), 1, 1);
        this.mainPane.addItem(this.getModifyPlotsItem(), 5, 1);

        this.addPane(new GoBackPane(4, 3, null));
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
                                Component.text("Click to see all plots", NamedTextColor.DARK_GRAY)
                        ))
                        .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjY0NTliZTA5OTk4ZTUwYWJkMmNjZjRjZDM4M2U2YjM4YWI1YmM5MDVmYWNiNjZkY2UwZTE0ZTAzOGJhMTk2OCJ9fX0=", null))
                        .build(), event -> new FarmMainMenu(this.region).show(event.getWhoClicked())
        );
    }

    private GuiItem getAnimalPlotsItem() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#FF8378>Animal Plots</color></b>");
        return new GuiItem(
                new ItemBuilder(Material.OAK_FENCE)
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to see all plots", NamedTextColor.DARK_GRAY)
                        ))
                        .build(), event -> new AnimalMainMenu(this.region).show(event.getWhoClicked())
        );
    }

    private GuiItem getBarnPlotItem() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#FF8378>Barn</color></b>");
        return new GuiItem(
                new SkullBuilder()
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to open barn", NamedTextColor.DARK_GRAY)
                        ))
                        .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmRhMGMyNDBjOGM3ZjMyOGYyZTYzOGYxYmY4NjJiODg5YjZlOTdiNjYwNzAwOTcxMTM5YmQ2MzQ4MWVjZDQzOSJ9fX0=", null))
                        .build(),
                event -> {
                    BarnPlot barnPlot = (BarnPlot) this.region.getPlots().get(PlotType.BARN).toArray(Plot[]::new)[0];
                    new BarnPlotMainMenu(barnPlot).show(event.getWhoClicked());
                }
        );
    }

    private GuiItem getSiloPlotItem() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#FF8378>Silo</color></b>");
        return new GuiItem(
                new ItemBuilder(Material.BARREL)
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to open silo", NamedTextColor.DARK_GRAY)
                        ))
                        .build(),
                event -> {
                    SiloPlot siloPlot = (SiloPlot) this.region.getPlots().get(PlotType.SILO).toArray(Plot[]::new)[0];
                    new SiloPlotMainMenu(siloPlot).show(event.getWhoClicked());
                }
        );
    }

    private GuiItem getModifyPlotsItem() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#FF8378>Modify Plots</color></b>");
        return new GuiItem(
                new ItemBuilder(Material.COMMAND_BLOCK)
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to open modify menu", NamedTextColor.DARK_GRAY)
                        ))
                        .build(),
                event -> new HarvestableAutoStoreMainMenu(this.region).show(event.getWhoClicked())
        );
    }
}
