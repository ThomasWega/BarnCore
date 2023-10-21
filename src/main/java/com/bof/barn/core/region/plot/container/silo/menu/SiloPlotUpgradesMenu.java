package com.bof.barn.core.region.plot.container.silo.menu;

import com.bof.barn.core.gui.premade.menu.plot.upgrades.PlotUpgradesMenuGUI;
import com.bof.barn.core.region.plot.container.silo.SiloPlot;
import org.jetbrains.annotations.NotNull;

public class SiloPlotUpgradesMenu extends PlotUpgradesMenuGUI<SiloPlot> {

    public SiloPlotUpgradesMenu(@NotNull SiloPlot plot) {
        super(plot, () -> new SiloPlotMainMenu(plot));
    }
}
