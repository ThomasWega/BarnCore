package com.bof.core.menu;

import com.bof.core.item.SkullBuilder;
import com.bof.core.skin.Skin;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.Nullable;

public class GoBackPane extends StaticPane {

    public GoBackPane(int x, int y, @Nullable Gui previousGui) {
        super(x, y, 1, 1, Priority.HIGHEST);

        this.addItem(new GuiItem(new SkullBuilder()
                        .displayName(Component.text("Go back", NamedTextColor.GRAY))
                        .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzUxY2VkMmU2NDczNjZmOGYzYWQyZGZlNDE1Y2NhODU2NTFiZmFmOTczOWE5NWNkNTdiNmYyMWNiYTA1MyJ9fX0=", null))
                        .build(),
                        event -> {
                            Player player = ((Player) event.getWhoClicked());
                            event.setCancelled(true);
                            if (previousGui == null) {
                                player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                                return;
                            }
                            previousGui.show(player);
                        }),
                0, 0);
    }
}
