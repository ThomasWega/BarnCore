package com.bof.core.region.plots.silo.menu;

import com.bof.core.item.ItemBuilder;
import com.bof.core.item.SkullBuilder;
import com.bof.core.menu.premade.back.GoBackPane;
import com.bof.core.region.plots.silo.SiloPlot;
import com.bof.core.skin.Skin;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SiloPlotMainMenu extends ChestGui {
    private final SiloPlot plot;
    private final OutlinePane mainPane = new OutlinePane(1, 1, 7, 2);

    public SiloPlotMainMenu(@NotNull SiloPlot plot) {
        super(4, ComponentHolder.of(Component.text("Silo " + plot.getId())));
        this.plot = plot;
        this.initialize();
    }

    private void initialize() {
        this.mainPane.setGap(2);

        this.addSections();

        this.addPane(new GoBackPane(4, 3, null));
        this.addPane(mainPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private void addSections() {
        this.mainPane.addItem(getSellCropsItem());
        this.mainPane.addItem(getOpenSiloItem());
    }

    private GuiItem getSellCropsItem() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#4AFF98>Sell Crops</color></b>");
        return new GuiItem(
                new SkullBuilder()
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to sell all", NamedTextColor.DARK_GRAY)
                        ))
                        .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzRjY2I1Mjc1MGU5N2U4MzBhZWJmYThhMjFkNWRhMGQzNjRkMGZkYWQ5ZmIwY2MyMjBmZTJjYTg0MTE4NDJjMyJ9fX0=", null))
                        .build(), event -> event.setCancelled(true)
        );
    }

    private GuiItem getOpenSiloItem() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#ffa83d>Open Silo</color></b>");
        return new GuiItem(
                new ItemBuilder(Material.BARREL)
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to open silo", NamedTextColor.DARK_GRAY)
                        ))
                        .build(), event -> event.setCancelled(true)
        );
    }
}
