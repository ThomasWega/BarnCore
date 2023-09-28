package com.bof.core.region.plot.selling.silo.menu;

import com.bof.core.item.ItemBuilder;
import com.bof.core.menu.premade.back.GoBackPane;
import com.bof.core.menu.premade.page.PaginatedNavGUI;
import com.bof.core.region.plot.harvestable.farm.CropType;
import com.bof.core.region.plot.selling.silo.SiloPlot;
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

public class SiloContainerMenu extends PaginatedNavGUI {
    private final SiloPlot plot;

    public SiloContainerMenu(@NotNull SiloPlot plot) {
        super(6, ComponentHolder.of(Component.text("Container page 1")), 0, 0, 9, 5);
        this.plot = plot;

        this.initialize();
    }

    private void initialize() {
        this.addPane(new GoBackPane(4, 5, new SiloPlotMainMenu(this.plot)));

        this.setOnGlobalClick(event -> event.setCancelled(true));

        this.fillWithCrops();
        this.update();
    }

    private void fillWithCrops() {
        List<ItemStack> mergedItems = ItemStackUtils.mergeItemStacks(this.plot.getCropsStored());
        List<GuiItem> items = mergedItems.stream()
                .map(itemStack -> {
                    @SuppressWarnings("OptionalGetWithoutIsPresent")
                    // should always return okay, as the only items which are put into the inventory are crops
                    CropType cropType = CropType.getByMaterial(itemStack.getType()).get();
                    GuiItem guiItem = this.getCropItem(itemStack, cropType);
                    guiItem.setAction(handleSell(guiItem, cropType));
                    return guiItem;
                }).toList();

        this.paginatedPane.populateWithGuiItems(items);
    }

    private GuiItem getCropItem(ItemStack itemStack, CropType cropType) {
        GuiItem guiItem = new GuiItem(new ItemBuilder(cropType.getItemMaterial())
                .displayName(Component.text(itemStack.getAmount() + "x ", NamedTextColor.GRAY).append(cropType.getDisplayName()))
                .lore(List.of(
                        Component.text("Price per piece: ", NamedTextColor.WHITE).append(Component.text(cropType.getValue(), NamedTextColor.RED)),
                        Component.empty(),
                        Component.text("Click to sell", NamedTextColor.GREEN)
                ))
                .amount(itemStack.getAmount())
                .build());

        guiItem.setAction(handleSell(guiItem, cropType));
        return guiItem;
    }

    private Consumer<InventoryClickEvent> handleSell(GuiItem guiItem, CropType cropType) {
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
                            pane.getItems().set(pane.getItems().indexOf(guiItem), this.getCropItem(itemStack, cropType));
                        }

                        // sell only one
                        float value = this.plot.sellCrops(new ItemStack(cropType.getMaterial()));
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
