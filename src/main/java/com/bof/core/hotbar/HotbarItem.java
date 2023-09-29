package com.bof.core.hotbar;

import com.bof.core.Core;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Item which will be placed into the player's inventory and has action when clicked.
 * Any movement of the item will be blocked
 */
@Data
public abstract class HotbarItem implements Listener {
    private final ItemStack itemStack;
    private Consumer<PlayerInteractEvent> action;

    public HotbarItem(@NotNull Core core, @NotNull ItemStack itemStack, @NotNull Consumer<PlayerInteractEvent> action) {
        this.itemStack = itemStack;
        this.action = action;
        this.registerListener(core);
    }

    public HotbarItem(@NotNull Core core, @NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
        this.registerListener(core);
    }

    private void registerListener(Core core) {
        Bukkit.getPluginManager().registerEvents(this, core);
    }

    /**
     * Put the item to the index in the player's inventory
     *
     * @param player Player to give the item to
     * @param index  Index to put the item at
     */
    public void setToPlayer(@NotNull Player player, int index) {
        player.getInventory().setItem(index, this.itemStack);
    }


    // needs to be public, otherwise won't listen!
    @EventHandler
    public void onHotbarItemClick(PlayerInteractEvent event) {
        if (this.itemStack.equals(event.getItem()) && this.action != null) {
            event.setCancelled(true);
            this.action.accept(event);
        }
    }

    // needs to be public, otherwise won't listen!
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player && e.getClickedInventory() != null) {
            List<ItemStack> items = new ArrayList<>();
            items.add(e.getCursor());
            items.add((e.getClick() == ClickType.NUMBER_KEY) ? e.getWhoClicked().getInventory().getItem(e.getHotbarButton()) : e.getCurrentItem());
            for (ItemStack item : items) {
                if (item == null) continue;
                if (item.equals(this.itemStack)) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
