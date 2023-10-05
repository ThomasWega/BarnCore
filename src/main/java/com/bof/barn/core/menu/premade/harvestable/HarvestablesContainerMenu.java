package com.bof.barn.core.menu.premade.harvestable;

import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.menu.premade.back.GoBackPane;
import com.bof.barn.core.menu.premade.page.PaginatedNavGUI;
import com.bof.barn.core.region.plot.harvestable.HarvestableType;
import com.bof.barn.core.region.plot.selling.ContainerPlot;
import com.bof.barn.core.utils.HarvestableUtils;
import com.bof.barn.core.utils.ItemStackUtils;
import com.github.stefvanschie.inventoryframework.adventuresupport.StringHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class HarvestablesContainerMenu<T extends HarvestableType, P extends ContainerPlot<T>> extends PaginatedNavGUI {
    private final P plot;
    private final Class<T> type;
    private final Gui goBackGui;

    public HarvestablesContainerMenu(@NotNull P plot, @NotNull Class<T> type, @Nullable Gui goBackGui, int rows, int x, int y, int length, int height) {
        super(rows, StringHolder.of("Container page N"), x, y, length, height);
        this.plot = plot;
        this.type = type;
        this.goBackGui = goBackGui;
        this.initialize();
    }

    private void initialize() {
        this.addPane(new GoBackPane(4, this.getRows() - 1, this.goBackGui));

        this.setOnGlobalClick(event -> event.setCancelled(true));

        this.fillWithHarvestables();
        this.update();
    }

    private void fillWithHarvestables() {
        List<ItemStack> mergedItems = ItemStackUtils.mergeItemStacks(this.plot.getStored());
        List<GuiItem> items = mergedItems.stream()
                .map(itemStack -> {
                    Optional<T> optType = Arrays.stream(type.getEnumConstants())
                            .filter(t -> t.getItem().getType() == itemStack.getType())
                            .findAny();
                    // should always return okay, as the only items which are put into the inventory are harvestables
                    T type = optType.orElseThrow();
                    return this.getHarvestableItem(itemStack, type);
                }).toList();

        this.paginatedPane.populateWithGuiItems(items);
    }

    private GuiItem getHarvestableItem(ItemStack itemStack, T type) {
        GuiItem guiItem = new GuiItem(new ItemBuilder(itemStack)
                .displayName(HarvestableUtils.getModifiedDisplayName(type, itemStack))
                .lore(List.of(
                        Component.text("Price per piece: ", NamedTextColor.WHITE)
                                .append(Component.text(HarvestableUtils.getModifiedValue(type, itemStack), NamedTextColor.RED)),
                        Component.empty(),
                        Component.text("Click to sell", NamedTextColor.GREEN)
                ))
                .amount(itemStack.getAmount())
                .build());

        guiItem.setAction(handleSell(guiItem, type, itemStack));
        return guiItem;
    }

    private Consumer<InventoryClickEvent> handleSell(GuiItem guiItem, T type, ItemStack itemStack) {
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
                            pane.getItems().set(pane.getItems().indexOf(guiItem), this.getHarvestableItem(itemStack, type));
                        }

                        // sell only one
                        ItemStack clone = itemStack.clone();
                        clone.setAmount(1);
                        float value = this.plot.sellHarvestables(clone);
                        this.update();
                        player.sendMessage(this.getSellMessage(type, value));
                    });
        };
    }

    public abstract Component getSellMessage(@NotNull T type, float sellValue);

    @Override
    public void update() {
        this.setTitle("Container page " + (this.paginatedPane.getPage() + 1));
        super.update();
    }
}
