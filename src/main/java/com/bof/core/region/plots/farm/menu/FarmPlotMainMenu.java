package com.bof.core.region.plots.farm.menu;

import com.bof.core.item.ItemBuilder;
import com.bof.core.item.SkullBuilder;
import com.bof.core.menu.premade.back.GoBackPane;
import com.bof.core.region.plots.farm.FarmPlot;
import com.bof.core.skin.Skin;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FarmPlotMainMenu extends ChestGui {
    private final FarmPlot plot;
    private final OutlinePane mainPane = new OutlinePane(1, 1, 7, 2);

    public FarmPlotMainMenu(@NotNull FarmPlot plot) {
        super(4, ComponentHolder.of(Component.text("Farm Plot " + plot.getId())));
        this.plot = plot;
        this.initialize();
    }

    private void initialize() {
        this.mainPane.setGap(2);

        this.addSections();

        this.addPane(new GoBackPane(4, 3, new CropsMainMenu(this.plot.getOwningRegion())));
        this.addPane(mainPane);

        this.setOnGlobalClick(event -> event.setCancelled(true));
    }

    private void addSections() {
        this.mainPane.addItem(getChangeCropsItem());
        this.mainPane.addItem(getUpgradesItem());
        this.mainPane.addItem(getBoostersItem());
        this.mainPane.addItem(getHarvestItem());
        this.mainPane.addItem(getAutoHarvestItem());
    }

    private GuiItem getChangeCropsItem() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#FF8378>Change Crops</color></b>");
        return new GuiItem(
                new ItemBuilder(this.plot.getCurrentCrop().getItemMaterial())
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to change the crops", NamedTextColor.DARK_GRAY)
                        ))
                        .build(), event -> new FarmChangeCropsMenu(this.plot).show(event.getWhoClicked())
        );
    }

    private GuiItem getUpgradesItem() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#4FFFD3>Boosters</color></b>");
        return new GuiItem(
                new SkullBuilder()
                        .displayName(name)
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to open boosters", NamedTextColor.DARK_GRAY)
                        ))
                        .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmI0OWEyY2I5MDczNzk5MzIwMWZlNzJhMWYxYWI3NWM1YzkzYzI4ZjA0N2Y2ODVmZmFkNWFiMjBjN2IwY2FmMCJ9fX0=", null))
                        .build(), event -> event.setCancelled(true)
        );
    }

    private GuiItem getBoostersItem() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#eeff30>Boosters</color></b>");
        return new GuiItem(
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

    private GuiItem getHarvestItem() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#BE7DFF>Harvest Crops</color></b>");
        return new GuiItem(
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
                    plot.harvestCrops(player);
                    new FarmPlotMainMenu(this.plot).show(player);
                }
        );
    }

    private GuiItem getAutoHarvestItem() {
        Component name = MiniMessage.miniMessage().deserialize("<b><color:#2b84ff>Auto Harvest</color></b>");
        if (plot.isAutoHarvest()) {
            return new GuiItem(
                    new SkullBuilder()
                            .displayName(name)
                            .lore(List.of(
                                    Component.empty(),
                                    Component.text("Click to disable", NamedTextColor.RED)
                            ))
                            .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTVhMGIwN2UzNmVhZmRlY2YwNTljOGNiMTM0YTdiZjBhMTY3ZjkwMDk2NmYxMDk5MjUyZDkwMzI3NjQ2MWNjZSJ9fX0=", null))
                            .addEnchantment(Enchantment.MENDING, 1)
                            .hideFlags()
                            .build(),
                    event -> {
                        Player player = ((Player) event.getWhoClicked());
                        plot.setAutoHarvest(player, false);
                        player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                        plot.updateHologram();
                    }
            );
        } else {
            return new GuiItem(
                    new SkullBuilder()
                            .displayName(name)
                            .lore(List.of(
                                    Component.empty(),
                                    Component.text("Click to enable", NamedTextColor.GREEN)
                            ))
                            .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTVhMGIwN2UzNmVhZmRlY2YwNTljOGNiMTM0YTdiZjBhMTY3ZjkwMDk2NmYxMDk5MjUyZDkwMzI3NjQ2MWNjZSJ9fX0=", null))
                            .build(),
                    event -> {
                        Player player = ((Player) event.getWhoClicked());
                        plot.setAutoHarvest(player, true);
                        player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                        plot.updateHologram();
                    }
            );
        }
    }
}
