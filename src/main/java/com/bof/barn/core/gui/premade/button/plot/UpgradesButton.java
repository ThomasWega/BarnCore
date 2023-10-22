package com.bof.barn.core.gui.premade.button.plot;

import com.bof.barn.core.gui.premade.sound.SoundedGUIButton;
import com.bof.barn.core.item.SkullBuilder;
import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.setting.PlotSetting;
import com.bof.toolkit.skin.Skin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.function.Consumer;

public class UpgradesButton extends SoundedGUIButton {
    public UpgradesButton(@NotNull AbstractPlot plot, @Nullable Consumer<InventoryClickEvent> action) {
        super(createItem(plot), action);
    }

    private static @NotNull ItemStack createItem(AbstractPlot plot) {
        SkullBuilder builder = new SkullBuilder()
                .displayName(MiniMessage.miniMessage().deserialize("<b><color:#4FFFD3>Upgrades</color></b>"))
                .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmI0OWEyY2I5MDczNzk5MzIwMWZlNzJhMWYxYWI3NWM1YzkzYzI4ZjA0N2Y2ODVmZmFkNWFiMjBjN2IwY2FmMCJ9fX0=", null));

        // add unlocked settings toggled ON or OFF
        plot.getUnlockedSettings().stream()
                .sorted(Comparator.comparing(PlotSetting::getSettingName))
                .forEach(plotSetting -> {
                    Component line = Component.text(plotSetting.getSettingName() + ": ", NamedTextColor.GRAY)
                            .append(plotSetting.isToggled() ? Component.text("ON", NamedTextColor.GREEN) : Component.text("OFF", NamedTextColor.RED))
                            .append(getLevel(plotSetting));
                    builder.appendLoreLine(line);
                });

        // add locked settings
        plot.getLockedSettings().stream()
                .sorted(Comparator.comparing(PlotSetting::getSettingName))
                .forEach(plotSetting ->
                        builder.appendLoreLine(Component.text(plotSetting.getSettingName() + ": ", NamedTextColor.GRAY)
                                .append(Component.text("LOCKED", NamedTextColor.DARK_GRAY))));

        builder.appendLoreLine(Component.empty())
                .appendLoreLine(Component.text("Click to open upgrades", NamedTextColor.DARK_GRAY));

        return builder.build();
    }

    private static Component getLevel(PlotSetting plotSetting) {
        return Component.text(" (lvl. " + plotSetting.getCurrentLevel() + "/" + plotSetting.getMaxLevel() + ")", NamedTextColor.YELLOW);
    }
}
