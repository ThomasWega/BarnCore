package com.bof.core.region.plot.harvestable.farm.menu;

import com.bof.core.item.ItemBuilder;
import com.bof.core.menu.premade.back.GoBackPane;
import com.bof.core.region.plot.harvestable.farm.CropType;
import com.bof.core.region.plot.harvestable.farm.FarmPlot;
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

public class FarmChangeCropsMenu extends ChestGui {
    private final FarmPlot plot;
    private final OutlinePane mainPane = new OutlinePane(1, 1, 7, 1);
    private final boolean closeFarmPlotMenuOnGoBack;

    public FarmChangeCropsMenu(@NotNull FarmPlot plot, boolean closeFarmPlotMenuOnGoBack) {
        super(3, ComponentHolder.of(Component.text("Crops on Plot " + plot.getId())));
        this.plot = plot;
        this.closeFarmPlotMenuOnGoBack = closeFarmPlotMenuOnGoBack;
        this.initialize();
    }

    private void initialize() {
        this.mainPane.setPriority(Pane.Priority.NORMAL);

        this.addCropsItems();
        this.addSpaceItem();

        this.addPane(new GoBackPane(4, 2, new FarmPlotMainMenu(this.plot, this.closeFarmPlotMenuOnGoBack)));
        this.addPane(mainPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private void addCropsItems() {
        Arrays.stream(CropType.values()).forEach(cropsType -> {
            String cropName = StringUtils.capitalize(cropsType.name().toLowerCase());
            Component currentlyPlanted = MiniMessage.miniMessage().deserialize("<dark_gray>Currently planted</dark_gray>");
            Component changeToType = MiniMessage.miniMessage().deserialize("<white>Change crops to " + cropName + "</white>");

            if (cropsType == plot.getCurrentlyHarvesting()) {
                this.mainPane.addItem(new GuiItem(new ItemBuilder(cropsType.getItemMaterial())
                        .displayName(cropsType.getDisplayName()
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
                this.mainPane.addItem(new GuiItem(new ItemBuilder(cropsType.getItemMaterial())
                        .displayName(cropsType.getDisplayName()
                                .decorate(TextDecoration.BOLD))
                        .lore(List.of(
                                Component.empty(),
                                changeToType
                        ))
                        .build(),
                        event -> {
                            Player player = ((Player) event.getWhoClicked());
                            player.sendMessage("TO ADD - changed the crops type to " + cropName);
                            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                            this.plot.changeType(cropsType);
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
