package com.bof.barn.core.gui.premade.sound;

import com.bof.barn.core.sound.BoFSound;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
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

    public SoundedGUIButton(@NotNull ItemStack item, @Nullable Consumer<InventoryClickEvent> action) {
        super(item, addSoundToAction(action));
    }

    public SoundedGUIButton(@NotNull ItemStack item, @Nullable Consumer<InventoryClickEvent> action, @NotNull Plugin plugin, @Nullable BoFSound sound) {
        super(item, addSoundToAction(action, sound), plugin);
    }

    public SoundedGUIButton(@NotNull ItemStack item, @Nullable Consumer<InventoryClickEvent> action, @Nullable BoFSound sound) {
        super(item, addSoundToAction(action, sound));
    }

    public SoundedGUIButton(@NotNull ItemStack item, @NotNull Plugin plugin) {
        super(item, plugin);
    }
    public SoundedGUIButton(@NotNull ItemStack item) {
        super(item);
    }


    private static @Nullable Consumer<InventoryClickEvent> addSoundToAction(@Nullable Consumer<InventoryClickEvent> action) {
        return addSoundToAction(action, ButtonSound.PEEP);
    }

    private static @Nullable Consumer<InventoryClickEvent> addSoundToAction(@Nullable Consumer<InventoryClickEvent> action, @Nullable BoFSound sound) {
        if (action == null) {
            return null;
        }

        return action.andThen(event -> {
            if (sound == null) return;
            Player player = ((Player) event.getWhoClicked());
            player.playSound(sound.getSound());
        });
    }

    @Override
    public void setAction(@NotNull Consumer<InventoryClickEvent> action) {
        super.setAction(addSoundToAction(action));
    }

    public void setAction(@NotNull Consumer<InventoryClickEvent> action, @Nullable BoFSound sound) {
        super.setAction(addSoundToAction(action, sound));
    }
}
