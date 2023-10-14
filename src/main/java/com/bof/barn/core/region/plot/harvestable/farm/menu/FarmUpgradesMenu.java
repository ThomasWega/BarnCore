package com.bof.barn.core.region.plot.harvestable.farm.menu;

import com.bof.barn.core.gui.premade.menu.plot.upgrades.PlotUpgradesMenuGUI;
import com.bof.barn.core.region.plot.harvestable.farm.FarmPlot;
import org.jetbrains.annotations.NotNull;

public class FarmUpgradesMenu extends PlotUpgradesMenuGUI<FarmPlot> {

    public FarmUpgradesMenu(@NotNull FarmPlot plot, boolean closeOnGoBack) {
        super(plot, new FarmPlotMainMenu(plot, closeOnGoBack));
    }
}
