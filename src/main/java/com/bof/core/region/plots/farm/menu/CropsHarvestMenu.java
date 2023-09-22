package com.bof.core.region.plots.farm.menu;

import com.bof.core.item.ItemBuilder;
import com.bof.core.item.SkullBuilder;
import com.bof.core.menu.premade.back.GoBackPane;
import com.bof.core.region.BarnRegion;
import com.bof.core.region.plots.Plot;
import com.bof.core.region.plots.PlotType;
import com.bof.core.region.plots.farm.FarmPlot;
import com.bof.core.skin.Skin;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class CropsHarvestMenu extends ChestGui {
    private final BarnRegion region;
    private final OutlinePane selectedPane = new OutlinePane(1, 1, 7, 2);
    private final OutlinePane unlockedSlotsPane = selectedPane.copy();
    private final OutlinePane lockedSlotsPane = unlockedSlotsPane.copy();
    private final int unlockedSlots;

    public CropsHarvestMenu(@NotNull BarnRegion region) {
        super(4, ComponentHolder.of(Component.text("Harvest Menu")));
        this.region = region;
        this.unlockedSlots = region.getAutoHarvestSlots();
        this.initialize();
    }

    private void initialize() {
        this.selectedPane.setPriority(Pane.Priority.HIGHEST);
        this.unlockedSlotsPane.setPriority(Pane.Priority.HIGH);
        this.lockedSlotsPane.setPriority(Pane.Priority.NORMAL);

        this.addSelectedPlots();
        this.addUnlockedSlots();
        this.addLockedSlots();

        this.addPane(new GoBackPane(4, 3, null));
        this.addPane(selectedPane);
        this.addPane(unlockedSlotsPane);
        this.addPane(lockedSlotsPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private void addSelectedPlots() {
        region.getPlots().entrySet().stream()
                .filter(entry -> entry.getKey() == PlotType.FARM)
                .map(Map.Entry::getValue)
                .forEach(plots -> plots.stream()
                        // sort by id, so first plot is always 1, second is 2, etc.
                        .sorted(Comparator.comparingInt(Plot::getId))
                        .map(plot -> ((FarmPlot) plot))
                        .filter(FarmPlot::isAutoHarvest)
                        .forEach(plot -> {
                                    List<Component> lore = new ArrayList<>(plot.getLore());
                                    lore.addAll(List.of(
                                            Component.empty(),
                                            Component.text("Click to change plot", NamedTextColor.DARK_GRAY),
                                            Component.text("Shift-click to change plot", NamedTextColor.DARK_GRAY)
                                    ));
                                    this.selectedPane.addItem(new GuiItem(
                                            new ItemBuilder(plot.getCurrentCrop().getItemMaterial())
                                                    .displayName(plot.getDisplayName())
                                                    .lore(lore)
                                                    .build(), handleSelectAction(plot)
                                    ));
                                }
                        ));

    }

    private void addUnlockedSlots() {
        IntStream.rangeClosed(1, unlockedSlots).forEach(value -> this.unlockedSlotsPane.addItem(this.getUnlockedSlotButton()));
    }

    private void addLockedSlots() {
        IntStream.rangeClosed(1, plugin.getConfig().getInt("plots.farm.amount")).forEach(value -> this.lockedSlotsPane.addItem(this.getLockedSlotButton()));
    }

    private GuiItem getUnlockedSlotButton() {
        Component name = MiniMessage.miniMessage().deserialize("<color:#42FF49>Unlocked Slot</color>");

        return new GuiItem(new SkullBuilder()
                .displayName(name)
                .lore(List.of(
                        Component.empty(),
                        Component.text("Click to select plot", NamedTextColor.DARK_GRAY)
                ))
                .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjM3MGNhNDdiNjE3M2FiNThlNmE4MDE4NDg2ZTJmOGJhYTgzOTdhYjYxNGFlMmU2OTY4NDkxOTZiYWE3YyJ9fX0=", null))
                .build(), handleSelectAction()
        );
    }

    private GuiItem getLockedSlotButton() {
        Component name = MiniMessage.miniMessage().deserialize("<color:#38243b>Locked Slot</color>");
        return new GuiItem(new SkullBuilder()
                .displayName(name)
                .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjcwNWZkOTRhMGM0MzE5MjdmYjRlNjM5YjBmY2ZiNDk3MTdlNDEyMjg1YTAyYjQzOWUwMTEyZGEyMmIyZTJlYyJ9fX0=", null))
                .build(), event -> event.setCancelled(true));
    }

    private Consumer<InventoryClickEvent> handleSelectAction() {
        return event -> new CropsHarvestSetMenu(region, null).show(event.getWhoClicked());
    }

    private Consumer<InventoryClickEvent> handleSelectAction(FarmPlot plot) {
        return event -> {
            if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                plot.setAutoHarvest(false);
                // this doesn't work for some reason
                // this.update()
                new CropsHarvestMenu(this.region).show(event.getWhoClicked());
                return;
            }
            // if not click shift, open the select menu
            new CropsHarvestSetMenu(region, plot).show(event.getWhoClicked());
        };
    }
}
