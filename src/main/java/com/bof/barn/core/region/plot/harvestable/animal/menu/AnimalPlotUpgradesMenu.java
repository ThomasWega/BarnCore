package com.bof.barn.core.region.plot.harvestable.animal.menu;

import com.bof.barn.core.gui.premade.menu.plot.upgrades.PlotUpgradesMenuGUI;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalPlot;
import org.jetbrains.annotations.NotNull;

public class AnimalPlotUpgradesMenu extends PlotUpgradesMenuGUI<AnimalPlot> {

    public AnimalPlotUpgradesMenu(@NotNull AnimalPlot plot, boolean closeOnGoBack) {
        super(plot, () -> new AnimalPlotMainMenu(plot, closeOnGoBack));
    }
}
