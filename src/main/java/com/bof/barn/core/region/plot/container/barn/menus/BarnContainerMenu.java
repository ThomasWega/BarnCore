package com.bof.barn.core.region.plot.container.barn.menus;

import com.bof.barn.core.gui.premade.menu.plot.harvestable.HarvestablesContainerGUI;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalType;
import com.bof.barn.core.region.plot.container.barn.BarnPlot;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class BarnContainerMenu extends HarvestablesContainerGUI<AnimalType, BarnPlot> {

    public BarnContainerMenu(@NotNull BarnPlot plot) {
        super(plot, AnimalType.class, new BarnPlotMainMenu(plot), 6, 0, 0, 9, 5);
    }

    @Override
    public Component getSellMessage(@NotNull AnimalType type, float sellValue) {
        return Component.text("TO ADD - Sold animal for " + sellValue);
    }
}