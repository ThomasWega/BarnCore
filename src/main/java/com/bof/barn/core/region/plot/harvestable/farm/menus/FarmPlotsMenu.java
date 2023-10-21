package com.bof.barn.core.region.plot.harvestable.farm.menus;

import com.bof.barn.core.gui.premade.menu.plot.SelectPlotGUI;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.menus.RegionMainMenu;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.AbstractHarvestablePlot;
import com.bof.barn.core.region.plot.harvestable.farm.FarmPlot;
import com.github.stefvanschie.inventoryframework.adventuresupport.StringHolder;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import org.jetbrains.annotations.NotNull;

public class FarmPlotsMenu extends SelectPlotGUI {

    public FarmPlotsMenu(@NotNull BarnRegion region) {
        super(region, PlotType.FARM, new RegionMainMenu(region), 4, 1, 1, 7, 2, StringHolder.of("Farm plots"));
    }

    @Override
    public Gui getActivePlotMenu(@NotNull AbstractHarvestablePlot<?> plot) {
        return new FarmPlotMainMenu(((FarmPlot) plot), false);
    }
}