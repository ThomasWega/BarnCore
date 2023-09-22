package com.bof.core.menu.premade.back;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.Nullable;

public class GoBackPane extends StaticPane {

    public GoBackPane(int x, int y, @Nullable Gui previousGui) {
        super(x, y, 1, 1, Priority.HIGHEST);

        GuiItem backItem = new GoBackItem();
        backItem.setAction(event -> {
            Player player = ((Player) event.getWhoClicked());
            event.setCancelled(true);
            if (previousGui == null) {
                player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                return;
            }
            previousGui.show(player);
        });
        this.addItem(backItem, 0, 0);
    }
}
