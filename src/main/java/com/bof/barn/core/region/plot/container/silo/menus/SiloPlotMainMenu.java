package com.bof.barn.core.region.plot.container.silo.menus;

import com.bof.barn.core.gui.premade.button.back.GoBackPane;
import com.bof.barn.core.gui.premade.button.plot.UpgradesButton;
import com.bof.barn.core.gui.premade.sound.SoundedGUIButton;
import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.item.SkullBuilder;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.menus.RegionMainMenu;
import com.bof.barn.core.region.plot.container.silo.SiloPlot;
import com.bof.toolkit.skin.Skin;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class SiloPlotMainMenu extends ChestGui {
    private final SiloPlot plot;
    private final StaticPane mainPane = new StaticPane(1, 1, 7, 1);

    public SiloPlotMainMenu(@NotNull SiloPlot plot) {
        super(3, ComponentHolder.of(Component.text("Silo " + plot.getId())));
        this.plot = plot;
        this.initialize();
    }

    private void initialize() {
        this.addSections();

        this.addPane(new GoBackPane(4, 2, new RegionMainMenu(this.plot.getOwningRegion())));
        this.addPane(mainPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private void addSections() {
        this.mainPane.addItem(this.getSellCropsButton(), 0, 0);
        this.mainPane.addItem(this.getOpenSiloButton(), 2, 0);
        this.mainPane.addItem(this.getPutCropsButton(), 4, 0);
        this.mainPane.addItem(new UpgradesButton(this.plot, event -> new SiloPlotUpgradesMenu(this.plot).show(event.getWhoClicked())), 6, 0);
    }

    private GuiItem getSellCropsButton() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#4AFF98>Sell Crops</color></b>");
        return new SoundedGUIButton(
                new SkullBuilder()
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to sell all", NamedTextColor.DARK_GRAY)
                        ))
                        .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzRjY2I1Mjc1MGU5N2U4MzBhZWJmYThhMjFkNWRhMGQzNjRkMGZkYWQ5ZmIwY2MyMjBmZTJjYTg0MTE4NDJjMyJ9fX0=", null))
                        .build(), handleSellAll()
        );
    }

    private Consumer<InventoryClickEvent> handleSellAll() {
        return event -> {
            Player player = ((Player) event.getWhoClicked());
            float value = this.plot.sellHarvestables(this.plot.getStoredItems());
            if (value == 0) {
                player.sendMessage("TO ADD - No crops are in this silo");
                return;
            }
            player.sendMessage("TO ADD - Sold all crops for " + value);
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        };
    }

    private GuiItem getOpenSiloButton() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#ffa83d>Open Silo</color></b>");
        return new SoundedGUIButton(new ItemBuilder(Material.BARREL)
                .displayName(name)
                .lore(List.of(
                        Component.empty(),
                        Component.text("Click to open silo", NamedTextColor.DARK_GRAY)
                ))
                .build(), event -> new SiloContainerMenu(this.plot).show(event.getWhoClicked())
        );
    }

    private GuiItem getPutCropsButton() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#FF5A36>Put Crops</color></b>");
        return new SoundedGUIButton(new ItemBuilder(Material.HOPPER)
                .displayName(name)
                .lore(List.of(
                        Component.empty(),
                        Component.text("Click to put crops into the silo", NamedTextColor.DARK_GRAY)
                ))
                .build(), handlePutCropsToSilo()
        );
    }

    private Consumer<InventoryClickEvent> handlePutCropsToSilo() {
        return event -> {
            Player player = ((Player) event.getWhoClicked());
            BarnRegion region = this.plot.getOwningRegion();
            this.plot.addHarvestablesToContainer(region.getCropsInventory());
            float value = region.removeCropsFromInventory(region.getCropsInventory());

            if (value != 0) {
                player.sendMessage("TO ADD - Put crops of value " + value + " FarmCoins into the silo");
            }
            player.closeInventory();
        };
    }
}
