package com.bof.core.region.plots.silo.menu;

import com.bof.core.menu.premade.back.GoBackPane;
import com.bof.core.menu.premade.page.NextPageItem;
import com.bof.core.menu.premade.page.PreviousPageItem;
import com.bof.core.region.plots.silo.SiloPlot;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class SiloContainerMainMenu extends ChestGui {
    private final SiloPlot plot;
    private final PaginatedPane paginatedPane = new PaginatedPane(0, 0, 9, 5);
    private final StaticPane nextPagePane = new StaticPane(8, 5, 1, 1);
    private final StaticPane previousPagePane = new StaticPane(0, 5, 1, 1);

    public SiloContainerMainMenu(@NotNull SiloPlot plot) {
        super(6, ComponentHolder.of(Component.text("Container page 1")));
        this.plot = plot;
        this.initialize();
    }

    private void initialize() {
        this.addNavigationItems();

        this.addPane(new GoBackPane(4, 5, null));
        this.addPane(this.paginatedPane);
        System.out.println(this.paginatedPane.getPage());
        System.out.println(this.paginatedPane.getPages());

        if (this.paginatedPane.getPage() < this.paginatedPane.getPages()) {
            this.addPane(this.nextPagePane);
        }

        if (this.paginatedPane.getPage() > 0 && this.paginatedPane.getPages() > 0) {
            this.addPane(this.previousPagePane);
        }

        this.addPane(this.nextPagePane);
        this.addPane(this.previousPagePane);

        this.setOnGlobalClick(event -> event.setCancelled(true));

        this.fillWithCrops();
    }

    private void addNavigationItems() {
        GuiItem nextPageItem = new NextPageItem();
        nextPageItem.setAction(event -> {
            this.paginatedPane.setPage(this.paginatedPane.getPage() + 1);
            this.update();
        });

        GuiItem prevPageItem = new PreviousPageItem();
        prevPageItem.setAction(event -> {
            this.paginatedPane.setPage(this.paginatedPane.getPage() - 1);
            this.update();
        });

        this.nextPagePane.addItem(nextPageItem, 0, 0);
        this.previousPagePane.addItem(prevPageItem, 0, 0);
    }

    private void fillWithCrops() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            items.add(String.valueOf(i));
        }
        this.paginatedPane.populateWithNames(items, Material.WHEAT);
    }
}
