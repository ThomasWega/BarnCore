package com.bof.barn.core.region.plot.selling.barn.menu;

import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.menu.premade.back.GoBackPane;
import com.bof.barn.core.menu.premade.page.PaginatedNavGUI;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalType;
import com.bof.barn.core.region.plot.selling.barn.BarnPlot;
import com.bof.barn.core.utils.HarvestableUtils;
import com.bof.barn.core.utils.ItemStackUtils;
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
        List<ItemStack> mergedItems = ItemStackUtils.mergeItemStacks(this.plot.getStored());
        List<GuiItem> items = mergedItems.stream()
                .map(itemStack -> {
                    // should always return okay, as the only items which are put into the inventory are animals
                    AnimalType animalType = AnimalType.getByItemMaterial(itemStack.getType()).get();
                    return this.getAnimalItem(itemStack, animalType);
                }).toList();

        this.paginatedPane.populateWithGuiItems(items);
    }

    private GuiItem getAnimalItem(ItemStack itemStack, AnimalType animalType) {
        GuiItem guiItem = new GuiItem(new ItemBuilder(itemStack)
                .displayName(HarvestableUtils.getModifiedDisplayName(animalType, itemStack))
                .lore(List.of(
                        Component.text("Price per piece: ", NamedTextColor.WHITE)
                                .append(Component.text(HarvestableUtils.getModifiedValue(animalType, itemStack), NamedTextColor.RED)),
                        Component.empty(),
                        Component.text("Click to sell", NamedTextColor.GREEN)
                ))
                .amount(itemStack.getAmount())
                .build());

        guiItem.setAction(handleSell(guiItem, animalType, itemStack));
        return guiItem;
    }

    private Consumer<InventoryClickEvent> handleSell(GuiItem guiItem, AnimalType type, ItemStack itemStack) {
        return event -> {
            Player player = ((Player) event.getWhoClicked());
            this.paginatedPane.getPanes().stream()
                    .map(pane -> ((OutlinePane) pane))
                    .forEach(pane -> {
                        if (!pane.getItems().contains(guiItem)) return;

                        itemStack.setAmount(itemStack.getAmount() - 1);
                        if (itemStack.getAmount() == 0) {
                            pane.removeItem(guiItem);
                        } else {
                            pane.getItems().set(pane.getItems().indexOf(guiItem), this.getAnimalItem(itemStack, type));
                        }

                        // sell only one
                        ItemStack clone = itemStack.clone();
                        clone.setAmount(1);
                        float value = this.plot.sellAnimals(clone);
                        this.update();
                        player.sendMessage("TO ADD - Sold animal for " + value);
                    });
        };
    }

    @Override
    public void update() {
        this.setTitle("Container page " + (this.paginatedPane.getPage() + 1));
        super.update();
    }
}
