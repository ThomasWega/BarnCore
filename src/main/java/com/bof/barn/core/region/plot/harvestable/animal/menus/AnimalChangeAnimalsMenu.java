package com.bof.barn.core.region.plot.harvestable.animal.menus;

import com.bof.barn.core.gui.premade.menu.plot.harvestable.ChangeHarvestablesGUI;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalPlot;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalType;
import com.github.stefvanschie.inventoryframework.adventuresupport.StringHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang.WordUtils;
import org.jetbrains.annotations.NotNull;

public class AnimalChangeAnimalsMenu extends ChangeHarvestablesGUI<AnimalType, AnimalPlot> {

    public AnimalChangeAnimalsMenu(@NotNull AnimalPlot plot, boolean closeMainPlotMenuOnGoBack) {
        super(AnimalType.class, plot, new AnimalPlotMainMenu(plot, closeMainPlotMenuOnGoBack), StringHolder.of("Select Animal type"));
    }

    @Override
    public Component getChangeToTypeText(@NotNull AnimalType type) {
        return MiniMessage.miniMessage().deserialize("<white>Change animals to " + WordUtils.capitalizeFully(type.name()) + "</white>");
    }

    @Override
    public Component getChangedToTypeMessage(@NotNull AnimalType type) {
        return Component.text("TO ADD - changed the animals type to " + WordUtils.capitalizeFully(type.name()));
    }

    @Override
    public Component getCurrentlyHarvestingText(@NotNull AnimalType Type) {
        return MiniMessage.miniMessage().deserialize("<dark_gray>Currently breeding</dark_gray>");
    }
}