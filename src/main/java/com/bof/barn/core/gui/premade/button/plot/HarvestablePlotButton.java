package com.bof.barn.core.gui.premade.button.plot;

import com.bof.barn.core.gui.premade.sound.SoundedGUIButton;
import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.region.plot.harvestable.AbstractHarvestablePlot;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class HarvestablePlotButton extends SoundedGUIButton {
    public HarvestablePlotButton(@NotNull AbstractHarvestablePlot<?> plot, @Nullable Consumer<List<Component>> loreAction,
                                 @Nullable Consumer<InventoryClickEvent> action) {
        super(createItem(plot, loreAction), action);
    }

    private static ItemStack createItem(@NotNull AbstractHarvestablePlot<?> plot, @Nullable Consumer<List<Component>> loreAction) {
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
