package com.bof.barn.core.region.plot.selling.barn.menu;

import com.bof.barn.core.gui.premade.sound.SoundedGUIButton;
import com.bof.barn.core.gui.premade.button.back.GoBackPane;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.item.SkullBuilder;
import com.bof.barn.core.region.menu.RegionMainMenu;
import com.bof.barn.core.region.plot.selling.barn.BarnPlot;
import com.bof.barn.core.region.plot.selling.settings.AutoSellSetting;
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

public class BarnPlotMainMenu extends ChestGui {
    private final BarnPlot plot;
    private final StaticPane mainPane = new StaticPane(1, 1, 7, 1);

    public BarnPlotMainMenu(@NotNull BarnPlot plot) {
        super(3, ComponentHolder.of(Component.text("Barn")));
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
        this.mainPane.addItem(this.getSellAnimalsButton(),0, 0);
        this.mainPane.addItem(this.getSeeAnimalsButton(),2, 0);
        this.mainPane.addItem(this.getPutAnimalsButton(),4, 0);
        this.mainPane.addItem(this.getAutoSellButton(),6, 0);
    }

    private GuiItem getSellAnimalsButton() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#4AFF98>Sell Animals</color></b>");
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
            float value = this.plot.sellHarvestables(this.plot.getStored());
            if (value == 0) {
                player.sendMessage("TO ADD - No animals are in this silo");
                return;
            }
            player.sendMessage("TO ADD - Sold all animals for " + value);
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        };
    }

    private GuiItem getSeeAnimalsButton() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#ffa83d>See Animals</color></b>");
        return new SoundedGUIButton(new ItemBuilder(Material.BARREL)
                .displayName(name)
                .lore(List.of(
                        Component.empty(),
                        Component.text("Click to see animals in barn", NamedTextColor.DARK_GRAY)
                ))
                .build(), event -> new BarnContainerMenu(this.plot).show(event.getWhoClicked())
        );
    }

    private GuiItem getAutoSellButton() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#2b84ff>Auto Sell</color></b>");
        String statusStr = this.plot.isSetting(AutoSellSetting.class) ? "<green>ON</green>" : "<red>OFF</red>";
        Component status = MiniMessage.miniMessage().deserialize("<white>Status: " + statusStr + "</white>");
        return new SoundedGUIButton(new SkullBuilder()
                .displayName(name)
                .lore(List.of(
                        status,
                        Component.empty(),
                        Component.text("Click to change status", NamedTextColor.DARK_GRAY)
                ))
                .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNlZDM0MjExZmVkNDAxMGE4Yzg1NzI0YTI3ZmE1ZmIyMDVkNjc2ODRiM2RhNTE3YjY4MjEyNzljNmI2NWQzZiJ9fX0=", null))
                .hideFlags()
                .build(),
                event -> {
                    Player player = ((Player) event.getWhoClicked());
                    this.plot.setSetting(AutoSellSetting.class, !this.plot.getSettingToggle(AutoSellSetting.class));
                    player.sendMessage("TO ADD - changed auto sell");
                    player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                }
        );
    }

    private GuiItem getPutAnimalsButton() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#FF5A36>Put Animals</color></b>");
        return new SoundedGUIButton(new ItemBuilder(Material.HOPPER)
                .displayName(name)
                .lore(List.of(
                        Component.empty(),
                        Component.text("Click to put animals into the barn", NamedTextColor.DARK_GRAY)
                ))
                .build(), handlePutAnimalsToSilo()
        );
    }

    private Consumer<InventoryClickEvent> handlePutAnimalsToSilo() {
        return event -> {
            Player player = ((Player) event.getWhoClicked());
            BarnRegion region = this.plot.getOwningRegion();
            this.plot.addHarvestablesToContainer(region.getAnimalInventory());
            float value = region.removeAnimalsFromInventory(region.getAnimalInventory());

            if (value != 0) {
                player.sendMessage("TO ADD - Put animals of value " + value + " FarmCoins into the barn");
            }
            player.closeInventory();
        };
    }
}
