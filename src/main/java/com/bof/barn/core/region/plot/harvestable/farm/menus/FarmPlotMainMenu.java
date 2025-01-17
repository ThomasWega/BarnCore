package com.bof.barn.core.region.plot.harvestable.farm.menus;

import com.bof.barn.core.gui.premade.button.back.GoBackPane;
import com.bof.barn.core.gui.premade.button.plot.UpgradesButton;
import com.bof.barn.core.gui.premade.sound.SoundedGUIButton;
import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.item.SkullBuilder;
import com.bof.barn.core.region.plot.harvestable.farm.FarmPlot;
import com.bof.toolkit.skin.Skin;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FarmPlotMainMenu extends ChestGui {
    private final FarmPlot plot;
    private final StaticPane mainPane = new StaticPane(1, 1, 7, 2);
    private final boolean closeOnGoBack;

    public FarmPlotMainMenu(@NotNull FarmPlot plot, boolean closeOnGoBack) {
        super(3, ComponentHolder.of(Component.text("Farm Plot " + plot.getId())));
        this.plot = plot;
        this.closeOnGoBack = closeOnGoBack;
        this.initialize();
    }

    private void initialize() {
        this.addSections();

        if (this.closeOnGoBack) {
            this.addPane(new GoBackPane(4, 2, null));
        } else {
            this.addPane(new GoBackPane(4, 2, new FarmPlotsMenu(this.plot.getOwningRegion())));
        }
        this.addPane(mainPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private void addSections() {
        this.mainPane.addItem(this.getChangeCropsButton(), 0, 0);
        this.mainPane.addItem(new UpgradesButton(this.plot, event -> new FarmPlotUpgradesMenu(this.plot, closeOnGoBack).show(event.getWhoClicked())), 2, 0);
        this.mainPane.addItem(this.getBoostersButton(), 4, 0);
        this.mainPane.addItem(this.getHarvestButton(), 6, 0);
    }

    private GuiItem getChangeCropsButton() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#FF8378>Change Crops</color></b>");
        return new SoundedGUIButton(
                new ItemBuilder(this.plot.getCurrentlyHarvesting().getItem())
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to change the crops", NamedTextColor.DARK_GRAY)
                        ))
                        .build(), event -> new FarmChangeCropsMenu(this.plot, this.closeOnGoBack).show(event.getWhoClicked())
        );
    }

    private GuiItem getBoostersButton() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#eeff30>Boosters</color></b>");
        return new SoundedGUIButton(
                new SkullBuilder()
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to open boosters", NamedTextColor.DARK_GRAY)
                        ))
                        .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkyNzRhMmFjNTQxZTQwNGMwYWE4ODg3OWIwYzhiMTBmNTAyYmMyZDdlOWE2MWIzYjRiZjMzNjBiYzE1OTdhMiJ9fX0=", null))
                        .build(), event -> event.setCancelled(true)
        );
    }

    private GuiItem getHarvestButton() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#BE7DFF>Harvest Crops</color></b>");
        return new SoundedGUIButton(
                new SkullBuilder()
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to harvest crops", NamedTextColor.DARK_GRAY)
                        ))
                        .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Y5ZTdjZDdiMTRmNWJjNjk1Yzc1ZGE1MDI2YjA5ZGViMjA5MmNlNDczN2VjNmI1YThiMGEyN2YxZmVlZjk1NCJ9fX0=", null))
                        .build(),
                event -> {
                    Player player = ((Player) event.getWhoClicked());

                    int harvestedCount = this.plot.harvest(player);
                    if (harvestedCount > 0) {
                        player.sendMessage("TO ADD - Harvested " + harvestedCount + " crops");
                    }
                    player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                }
        );
    }
}
