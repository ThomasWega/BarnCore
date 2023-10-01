package com.bof.barn.core.region.plot.harvestable.animal.menu;

import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.menu.premade.back.GoBackPane;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalPlot;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalType;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class AnimalChangeAnimalMenu extends ChestGui {
    private final AnimalPlot plot;
    private final OutlinePane mainPane = new OutlinePane(1, 1, 7, 1);
    private final boolean closeFarmPlotMenuOnGoBack;

    public AnimalChangeAnimalMenu(@NotNull AnimalPlot plot, boolean closeFarmPlotMenuOnGoBack) {
        super(3, ComponentHolder.of(Component.text("Animals on Plot " + plot.getId())));
        this.plot = plot;
        this.closeFarmPlotMenuOnGoBack = closeFarmPlotMenuOnGoBack;
        this.initialize();
    }

    private void initialize() {
        this.mainPane.setPriority(Pane.Priority.NORMAL);

        this.addCropsItems();
        this.addSpaceItem();

        this.addPane(new GoBackPane(4, 2, new AnimalPlotMainMenu(this.plot, this.closeFarmPlotMenuOnGoBack)));
        this.addPane(mainPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private void addCropsItems() {
        Arrays.stream(AnimalType.values()).forEach(animalType -> {
            String animalName = StringUtils.capitalize(animalType.name().toLowerCase());
            Component currentlyPlanted = MiniMessage.miniMessage().deserialize("<dark_gray>Currently selected</dark_gray>");
            Component changeToType = MiniMessage.miniMessage().deserialize("<white>Change animals to " + animalName + "</white>");

            if (animalType == plot.getCurrentlyHarvesting()) {
                this.mainPane.addItem(new GuiItem(new ItemBuilder(animalType.getItem())
                        .displayName(animalType.getDisplayName()
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
                this.mainPane.addItem(new GuiItem(new ItemBuilder(animalType.getItem())
                        .displayName(animalType.getDisplayName()
                                .decorate(TextDecoration.BOLD))
                        .lore(List.of(
                                Component.empty(),
                                changeToType
                        ))
                        .build(),
                        event -> {
                            Player player = ((Player) event.getWhoClicked());
                            player.sendMessage("TO ADD - changed the animal type to " + animalName);
                            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                            this.plot.changeType(animalType);
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
}
