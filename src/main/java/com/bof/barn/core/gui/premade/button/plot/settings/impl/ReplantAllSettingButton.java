package com.bof.barn.core.gui.premade.button.plot.settings.impl;

import com.bof.barn.core.gui.premade.sound.SoundedGUIButton;
import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.region.plot.harvestable.settings.impl.ReplantAllSetting;
import com.bof.barn.core.region.plot.setting.PlotSetting;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class ReplantAllSettingButton extends SoundedGUIButton {
    public ReplantAllSettingButton(@NotNull List<Component> appendLore, @Nullable Consumer<InventoryClickEvent> action) {
        super(new ItemBuilder(PlotSetting.getItem(ReplantAllSetting.class))
                .displayName(MiniMessage.miniMessage().deserialize("<b><color:#ff530f>Replant All</color></b>"))
                .appendLore(appendLore)
                .hideFlags()
                .build(), action
        );
    }
}
