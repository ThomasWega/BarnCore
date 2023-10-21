package com.bof.barn.core.gui.premade.button.plot.settings.impl;

import com.bof.barn.core.gui.premade.sound.SoundedGUIButton;
import com.bof.barn.core.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class AutoHarvestSettingButton extends SoundedGUIButton {
    public AutoHarvestSettingButton(@NotNull List<Component> lore, @Nullable Consumer<InventoryClickEvent> action) {
        super(new ItemBuilder(Material.SHEARS)
                .displayName(MiniMessage.miniMessage().deserialize("<b><color:#96ff59>Auto Harvest</color></b>"))
                .lore(lore)
                .hideFlags()
                .build(), action
        );
    }
}
