package com.bof.barn.core.region.plot.selling.barn.menu;

import com.bof.barn.core.gui.premade.menu.plot.upgrades.PlotUpgradesMenuGUI;
import com.bof.barn.core.region.plot.selling.barn.BarnPlot;
import org.jetbrains.annotations.NotNull;

public class BarnPlotUpgradesMenu extends PlotUpgradesMenuGUI<BarnPlot> {

    public BarnPlotUpgradesMenu(@NotNull BarnPlot plot) {
        super(plot, () -> new BarnPlotMainMenu(plot));
    }
}
