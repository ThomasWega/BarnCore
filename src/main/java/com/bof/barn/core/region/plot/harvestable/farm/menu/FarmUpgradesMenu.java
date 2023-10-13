package com.bof.barn.core.region.plot.harvestable.farm.menu;

import com.bof.barn.core.gui.premade.menu.PlotUpgradesGUI;
import com.bof.barn.core.region.plot.harvestable.farm.FarmPlot;
import org.jetbrains.annotations.NotNull;

public class FarmUpgradesMenu extends PlotUpgradesGUI<FarmPlot> {

    public FarmUpgradesMenu(@NotNull FarmPlot plot, boolean closeOnGoBack) {
        super(plot, new FarmPlotMainMenu(plot, closeOnGoBack));
    }
}
