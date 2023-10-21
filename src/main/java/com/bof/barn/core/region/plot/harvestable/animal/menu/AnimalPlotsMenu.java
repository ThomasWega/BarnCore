package com.bof.barn.core.region.plot.harvestable.animal.menu;

import com.bof.barn.core.gui.premade.menu.plot.SelectPlotGUI;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.menu.RegionMainMenu;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.AbstractHarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalPlot;
import com.github.stefvanschie.inventoryframework.adventuresupport.StringHolder;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import org.jetbrains.annotations.NotNull;

public class AnimalPlotsMenu extends SelectPlotGUI {

    public AnimalPlotsMenu(@NotNull BarnRegion region) {
        super(region, PlotType.ANIMAL, new RegionMainMenu(region), 4, 1, 1, 7, 2, StringHolder.of("Animal plots"));
    }

    @Override
    public Gui getActivePlotMenu(@NotNull AbstractHarvestablePlot<?> plot) {
        return new AnimalPlotMainMenu(((AnimalPlot) plot), false);
    }
}