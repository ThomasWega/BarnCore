package com.bof.barn.core.region.plot.harvestable.menus;

import com.bof.barn.core.gui.premade.button.back.GoBackPane;
import com.bof.barn.core.gui.premade.sound.SoundedGUIButton;
import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.item.SkullBuilder;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.menus.RegionMainMenu;
import com.bof.barn.core.region.plot.harvestable.animal.menus.AnimalUpgradesMenu;
import com.bof.barn.core.region.plot.harvestable.farm.menus.FarmUpgradesMenu;
import com.bof.toolkit.skin.Skin;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Menu which lets the player select between {@link com.bof.barn.core.region.plot.harvestable.animal.AnimalPlot} and
 * {@link com.bof.barn.core.region.plot.harvestable.farm.FarmPlot} buttons.
 */
public class HarvestableUpgradesMainMenu extends ChestGui {
    private final BarnRegion region;
    private final StaticPane mainPane = new StaticPane(1, 1, 7, 1);

    public HarvestableUpgradesMainMenu(@NotNull BarnRegion region) {
        super(3, ComponentHolder.of(Component.text("Select plot type")));
        this.region = region;
        this.initialize();
    }

    private void initialize() {
        this.mainPane.addItem(this.getFarmPlotsButton(), 1, 0);
        this.mainPane.addItem(this.getAnimalPlotsButton(), 5, 0);

        this.addPane(new GoBackPane(4, 2, new RegionMainMenu(this.region)));
        this.addPane(this.mainPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private GuiItem getFarmPlotsButton() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#FF8378>Farm Plots</color></b>");
        return new SoundedGUIButton(
                new SkullBuilder()
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to modify plots", NamedTextColor.DARK_GRAY)
                        ))
                        .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjY0NTliZTA5OTk4ZTUwYWJkMmNjZjRjZDM4M2U2YjM4YWI1YmM5MDVmYWNiNjZkY2UwZTE0ZTAzOGJhMTk2OCJ9fX0=", null))
                        .build(), event -> new FarmUpgradesMenu(this.region).show(event.getWhoClicked())
        );
    }

    private GuiItem getAnimalPlotsButton() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#FF8378>Animal Plots</color></b>");
        return new SoundedGUIButton(
                new ItemBuilder(Material.OAK_FENCE)
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to modify plots", NamedTextColor.DARK_GRAY)
                        ))
                        .build(), event -> new AnimalUpgradesMenu(this.region).show(event.getWhoClicked())
        );
    }
}
