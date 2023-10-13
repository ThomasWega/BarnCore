package com.bof.barn.core.region.plot.harvestable.farm.menu.upgrades;

import com.bof.barn.core.gui.premade.menu.PlotUpgradesMenu;
import com.bof.barn.core.region.plot.harvestable.farm.FarmPlot;
import com.bof.barn.core.region.plot.harvestable.farm.menu.FarmPlotMainMenu;
import org.jetbrains.annotations.NotNull;

public class FarmUpgradesMenu extends PlotUpgradesMenu<FarmPlot> {

    public FarmUpgradesMenu(@NotNull FarmPlot plot, boolean closeOnGoBack) {
        super(plot, new FarmPlotMainMenu(plot, closeOnGoBack));
    }
}
