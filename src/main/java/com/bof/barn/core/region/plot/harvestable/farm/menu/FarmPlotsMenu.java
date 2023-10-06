package com.bof.barn.core.region.plot.harvestable.farm.menu;

import com.bof.barn.core.gui.premade.menu.SelectPlotGUI;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.menu.RegionMainMenu;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.HarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.farm.FarmPlot;
import com.github.stefvanschie.inventoryframework.adventuresupport.StringHolder;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import org.jetbrains.annotations.NotNull;

public class FarmPlotsMenu extends SelectPlotGUI {

    public FarmPlotsMenu(@NotNull BarnRegion region) {
        super(region, PlotType.FARM, new RegionMainMenu(region), 4, 1, 1, 7, 2, StringHolder.of("Farm plots"));
    }

    @Override
    public Gui getActivePlotMenu(@NotNull HarvestablePlot<?> plot) {
        return new FarmPlotMainMenu(((FarmPlot) plot), false);
    }
}