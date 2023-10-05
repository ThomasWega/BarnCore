package com.bof.barn.core.region.plot.harvestable.animal.menu;

import com.bof.barn.core.menu.premade.SelectPlotMenu;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.menu.RegionMainMenu;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.HarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalPlot;
import com.github.stefvanschie.inventoryframework.adventuresupport.StringHolder;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import org.jetbrains.annotations.NotNull;

public class AnimalPlotsMenu extends SelectPlotMenu {

    public AnimalPlotsMenu(@NotNull BarnRegion region) {
        super(region, PlotType.ANIMAL, new RegionMainMenu(region), 4, 1, 1, 7, 2, StringHolder.of("Animal plots"));
    }

    @Override
    public Gui getActivePlotMenu(@NotNull HarvestablePlot<?> plot) {
        return new AnimalPlotMainMenu(((AnimalPlot) plot), false);
    }
}