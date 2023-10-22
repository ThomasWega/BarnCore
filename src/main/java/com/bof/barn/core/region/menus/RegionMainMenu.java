package com.bof.barn.core.region.menus;

import com.bof.barn.core.gui.premade.button.back.GoBackPane;
import com.bof.barn.core.gui.premade.sound.SoundedGUIButton;
import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.item.SkullBuilder;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.container.barn.BarnPlot;
import com.bof.barn.core.region.plot.container.barn.menus.BarnPlotMainMenu;
import com.bof.barn.core.region.plot.container.silo.SiloPlot;
import com.bof.barn.core.region.plot.container.silo.menus.SiloPlotMainMenu;
import com.bof.barn.core.region.plot.harvestable.animal.menus.AnimalPlotsMenu;
import com.bof.barn.core.region.plot.harvestable.farm.menus.FarmPlotsMenu;
import com.bof.barn.core.region.plot.harvestable.menus.HarvestableUpgradesMainMenu;
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

public class RegionMainMenu extends ChestGui {
    private final BarnRegion region;
    private final StaticPane mainPane = new StaticPane(1, 1, 7, 2);

    public RegionMainMenu(@NotNull BarnRegion region) {
        super(4, ComponentHolder.of(Component.text("Main Menu")));
        this.region = region;
        this.initialize();
    }

    private void initialize() {
        this.mainPane.addItem(this.getFarmPlotsButton(), 0, 0);
        this.mainPane.addItem(this.getAnimalPlotsButton(), 3, 0);
        this.mainPane.addItem(this.getSiloPlotButton(), 6, 0);

        this.mainPane.addItem(this.getBarnPlotButton(), 1, 1);
        this.mainPane.addItem(this.getModifyPlotsButton(), 5, 1);

        this.addPane(new GoBackPane(4, 3, null));
        this.addPane(this.mainPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private GuiItem getFarmPlotsButton() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#FF8378>Farm Plots</color></b>");
        return new SoundedGUIButton(
                new SkullBuilder()
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to see all plots", NamedTextColor.DARK_GRAY)
                        ))
                        .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjY0NTliZTA5OTk4ZTUwYWJkMmNjZjRjZDM4M2U2YjM4YWI1YmM5MDVmYWNiNjZkY2UwZTE0ZTAzOGJhMTk2OCJ9fX0=", null))
                        .build(), event -> new FarmPlotsMenu(this.region).show(event.getWhoClicked())
        );
    }

    private GuiItem getAnimalPlotsButton() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#FF8378>Animal Plots</color></b>");
        return new SoundedGUIButton(
                new ItemBuilder(Material.OAK_FENCE)
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to see all plots", NamedTextColor.DARK_GRAY)
                        ))
                        .build(), event -> new AnimalPlotsMenu(this.region).show(event.getWhoClicked())
        );
    }

    private GuiItem getBarnPlotButton() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#FF8378>Barn</color></b>");
        return new SoundedGUIButton(
                new SkullBuilder()
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to open barn", NamedTextColor.DARK_GRAY)
                        ))
                        .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmRhMGMyNDBjOGM3ZjMyOGYyZTYzOGYxYmY4NjJiODg5YjZlOTdiNjYwNzAwOTcxMTM5YmQ2MzQ4MWVjZDQzOSJ9fX0=", null))
                        .build(),
                event -> {
                    BarnPlot barnPlot = (BarnPlot) this.region.getPlots().get(PlotType.BARN).toArray(AbstractPlot[]::new)[0];
                    new BarnPlotMainMenu(barnPlot).show(event.getWhoClicked());
                }
        );
    }

    private GuiItem getSiloPlotButton() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#FF8378>Silo</color></b>");
        return new SoundedGUIButton(
                new ItemBuilder(Material.BARREL)
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to open silo", NamedTextColor.DARK_GRAY)
                        ))
                        .build(),
                event -> {
                    SiloPlot siloPlot = (SiloPlot) this.region.getPlots().get(PlotType.SILO).toArray(AbstractPlot[]::new)[0];
                    new SiloPlotMainMenu(siloPlot).show(event.getWhoClicked());
                }
        );
    }

    private GuiItem getModifyPlotsButton() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#FF8378>Modify Plots</color></b>");
        return new SoundedGUIButton(
                new ItemBuilder(Material.COMMAND_BLOCK)
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to open modify menu", NamedTextColor.DARK_GRAY)
                        ))
                        .build(),
                event -> new HarvestableUpgradesMainMenu(this.region).show(event.getWhoClicked())
        );
    }
}
