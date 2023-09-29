package com.bof.core.region.plot.selling.barn.menu;

import com.bof.core.item.ItemBuilder;
import com.bof.core.menu.premade.back.GoBackPane;
import com.bof.core.menu.premade.page.PaginatedNavGUI;
import com.bof.core.region.plot.harvestable.animal.AnimalType;
import com.bof.core.region.plot.selling.barn.BarnPlot;
import com.bof.core.utils.ItemStackUtils;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class BarnContainerMenu extends PaginatedNavGUI {
    private final BarnPlot plot;

    public BarnContainerMenu(@NotNull BarnPlot plot) {
        super(6, ComponentHolder.of(Component.text("Container page 1")), 0, 0, 9, 5);
        this.plot = plot;

        this.initialize();
    }

    private void initialize() {
        this.addPane(new GoBackPane(4, 5, new BarnPlotMainMenu(this.plot)));

        this.setOnGlobalClick(event -> event.setCancelled(true));

        this.fillWithAnimals();
        this.update();
    }

    private void fillWithAnimals() {
        List<ItemStack> mergedItems = ItemStackUtils.mergeItemStacks(this.plot.getAnimalsStored());
        List<GuiItem> items = mergedItems.stream()
                .map(itemStack -> {
                    // should always return okay, as the only items which are put into the inventory are animals
                    AnimalType animalType = AnimalType.getByItem(itemStack, true).get();
                    GuiItem guiItem = this.getAnimalItem(itemStack, animalType);
                    guiItem.setAction(handleSell(guiItem, animalType));
                    return guiItem;
                }).toList();

        this.paginatedPane.populateWithGuiItems(items);
    }

    private GuiItem getAnimalItem(ItemStack itemStack, AnimalType animalType) {
        GuiItem guiItem = new GuiItem(new ItemBuilder(animalType.getItem())
                .displayName(Component.text(itemStack.getAmount() + "x ", NamedTextColor.GRAY).append(animalType.getDisplayName()))
                .lore(List.of(
                        Component.text("Price per piece: ", NamedTextColor.WHITE).append(Component.text(animalType.getValue(), NamedTextColor.RED)),
                        Component.empty(),
                        Component.text("Click to sell", NamedTextColor.GREEN)
                ))
                .amount(itemStack.getAmount())
                .build());

        guiItem.setAction(handleSell(guiItem, animalType));
        return guiItem;
    }

    private Consumer<InventoryClickEvent> handleSell(GuiItem guiItem, AnimalType animalType) {
        return event -> {
            Player player = ((Player) event.getWhoClicked());
            this.paginatedPane.getPanes().stream()
                    .map(pane -> ((OutlinePane) pane))
                    .forEach(pane -> {
                        if (!pane.getItems().contains(guiItem)) return;

                        ItemStack itemStack = guiItem.getItem();
                        itemStack.setAmount(itemStack.getAmount() - 1);
                        if (itemStack.getAmount() == 0) {
                            pane.removeItem(guiItem);
                        } else {
                            pane.getItems().set(pane.getItems().indexOf(guiItem), this.getAnimalItem(itemStack, animalType));
                        }

                        // sell only one
                        float value = this.plot.sellAnimals(animalType.getItem());
                        this.update();
                        player.sendMessage("TO ADD - Sold crop for " + value);
                    });
        };
    }

    @Override
    public void update() {
        this.setTitle("Container page " + (this.paginatedPane.getPage() + 1));
        super.update();
    }
}
