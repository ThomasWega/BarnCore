package com.bof.barn.core.gui.premade;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SoundedGUIButton extends GuiItem {

    public SoundedGUIButton(@NotNull ItemStack item, @Nullable Consumer<InventoryClickEvent> action, @NotNull Plugin plugin) {
        super(item, addSoundToAction(action), plugin);
    }

    public SoundedGUIButton(@NotNull ItemStack item, @NotNull Plugin plugin) {
        super(item, plugin);
    }

    public SoundedGUIButton(@NotNull ItemStack item, @Nullable Consumer<InventoryClickEvent> action) {
        super(item, addSoundToAction(action));
    }

    public SoundedGUIButton(@NotNull ItemStack item) {
        super(item);
    }

    private static @Nullable Consumer<InventoryClickEvent> addSoundToAction(@Nullable Consumer<InventoryClickEvent> action) {
        if (action == null) {
            return null;
        }
        return action.andThen(event -> {
            Player player = ((Player) event.getWhoClicked());
            player.playSound(Sound.sound(
                    org.bukkit.Sound.BLOCK_NOTE_BLOCK_BIT.key(),
                    Sound.Source.AMBIENT, 1f, 1f)
            );
        });
    }
}
