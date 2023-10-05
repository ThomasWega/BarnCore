package com.bof.barn.core.menu.premade.harvestable;

import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.menu.premade.back.GoBackPane;
import com.bof.barn.core.region.plot.harvestable.HarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.HarvestableType;
import com.github.stefvanschie.inventoryframework.adventuresupport.TextHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public abstract class ChangeHarvestablesMenu<T extends HarvestableType, P extends HarvestablePlot<T>> extends ChestGui {
    private final Class<T> plotType;
    private final P plot;
    private final OutlinePane mainPane = new OutlinePane(1, 1, 7, 1);
    private final Gui goBackGui;
    
    public ChangeHarvestablesMenu(@NotNull Class<T> plotType, @NotNull P plot, @Nullable Gui goBackGui, @NotNull TextHolder title) {
        super(3, title);
        this.plotType = plotType;
        this.plot = plot;
        this.goBackGui = goBackGui;
        this.initialize();
    }

    private void initialize() {
        this.addHarvestablesItems();
        this.addSpaceItem();

        this.addPane(new GoBackPane(4, 2, this.goBackGui));
        this.addPane(this.mainPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private void addHarvestablesItems() {
        Arrays.stream(plotType.getEnumConstants()).forEach(type -> {
            Component currentlyPlanted = this.getCurrentlyHarvestingText(type);
            Component changeToType = this.getChangeToTypeText(type);

            if (type == plot.getCurrentlyHarvesting()) {
                this.mainPane.addItem(new GuiItem(new ItemBuilder(type.getItem())
                        .displayName(type.getDisplayName()
                                .decorate(TextDecoration.BOLD))
                        .lore(List.of(
                                Component.empty(),
                                currentlyPlanted
                        ))
                        .addEnchantment(Enchantment.MENDING, 1)
                        .hideFlags()
                        .build()
                ));
            } else {
                this.mainPane.addItem(new GuiItem(new ItemBuilder(type.getItem())
                        .displayName(type.getDisplayName()
                                .decorate(TextDecoration.BOLD))
                        .lore(List.of(
                                Component.empty(),
                                changeToType
                        ))
                        .build(),
                        event -> {
                            Player player = ((Player) event.getWhoClicked());
                            player.sendMessage(this.getChangedToTypeMessage(type));
                            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                            this.plot.changeType(type);
                        }
                ));
            }
        });
    }

    private void addSpaceItem() {
        GuiItem guiItem = new GuiItem(new ItemStack(Material.BEDROCK));
        guiItem.setVisible(false);
        this.mainPane.insertItem(guiItem, 1);
    }

    public abstract Component getChangeToTypeText(@NotNull T type);

    public abstract Component getChangedToTypeMessage(@NotNull T type);

    public abstract Component getCurrentlyHarvestingText(@NotNull T Type);
}
