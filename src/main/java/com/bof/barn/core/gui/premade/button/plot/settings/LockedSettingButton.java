package com.bof.barn.core.gui.premade.button.plot.settings;

import com.bof.barn.core.gui.premade.sound.SoundedGUIButton;
import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.item.SkullBuilder;
import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.setting.PlotSetting;
import com.bof.toolkit.skin.Skin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class LockedSettingButton extends SoundedGUIButton {
    public LockedSettingButton(@NotNull AbstractPlot plot, @NotNull PlotSetting setting, @Nullable Consumer<InventoryClickEvent> action) {
        // use the item from the setting but change the material
        super(new SkullBuilder(new ItemBuilder(setting.getItem()).material(Material.PLAYER_HEAD))
                .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODE2MjNkNTIzOGRhYjdkZWNkMzIwMjY1Y2FlMWRjNmNhOTFiN2ZhOTVmMzQ2NzNhYWY0YjNhZDVjNmJhMTZlMSJ9fX0=", null))
                .appendLoreLine(Component.empty())
                .appendLoreLine(Component.text("Price: " + setting.getPrice() + "$", NamedTextColor.WHITE))
                .appendLoreLine(Component.text("Your balance: " + plot.getOwningRegion().getFarmCoinsRounded(2) + "$", NamedTextColor.WHITE))
                .appendLoreLine(Component.empty())
                .appendLoreLine(Component.text("Shift-click to purchase this upgrade", NamedTextColor.RED))
                .build(), action
        );
    }
}
