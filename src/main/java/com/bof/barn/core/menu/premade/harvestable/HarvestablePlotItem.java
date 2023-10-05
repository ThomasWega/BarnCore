package com.bof.barn.core.menu.premade.harvestable;

import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.region.plot.harvestable.HarvestablePlot;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class HarvestablePlotItem extends GuiItem {
    public HarvestablePlotItem(@NotNull HarvestablePlot<?> plot, @Nullable Consumer<List<Component>> loreAction,
                               @Nullable Consumer<InventoryClickEvent> action) {
        super(createItem(plot, loreAction), action);
    }

    private static ItemStack createItem(@NotNull HarvestablePlot<?> plot, @Nullable Consumer<List<Component>> loreAction) {
        List<Component> lore = plot.getLore();
        if (loreAction != null) {
            loreAction.accept(lore);
        }

        return new ItemBuilder(plot.getCurrentlyHarvesting().getItem())
                .displayName(plot.getDisplayName())
                .lore(lore)
                .build();
    }
}
