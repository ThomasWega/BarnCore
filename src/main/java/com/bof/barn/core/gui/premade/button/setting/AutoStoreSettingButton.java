package com.bof.barn.core.gui.premade.button.setting;

import com.bof.barn.core.item.SkullBuilder;
import com.bof.toolkit.skin.Skin;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class AutoStoreSettingButton extends GuiItem {
    public AutoStoreSettingButton(@NotNull List<Component> lore, @Nullable Consumer<InventoryClickEvent> action) {
        super (new SkullBuilder()
                .displayName(MiniMessage.miniMessage().deserialize("<b><color:#2b84ff>Auto Store</color></b>"))
                .lore(lore)
                .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTVhMGIwN2UzNmVhZmRlY2YwNTljOGNiMTM0YTdiZjBhMTY3ZjkwMDk2NmYxMDk5MjUyZDkwMzI3NjQ2MWNjZSJ9fX0=", null))
                .hideFlags()
                .build(), action
        );
    }
}
