package com.bof.barn.core.menu.premade.back;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class GoBackPaneCustom extends StaticPane {

    public GoBackPaneCustom(int x, int y, @NotNull Consumer<InventoryClickEvent> eventConsumer) {
        super(x, y, 1, 1, Pane.Priority.HIGHEST);

        GuiItem backItem = new GoBackItem();
        backItem.setAction(eventConsumer);
        this.addItem(backItem, 0, 0);
    }
}
