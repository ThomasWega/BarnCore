package com.bof.barn.core.region.plot.container.barn.menus;

import com.bof.barn.core.gui.premade.menu.plot.setting.PlotSettingsMenuGUI;
import com.bof.barn.core.region.plot.container.barn.BarnPlot;
import org.jetbrains.annotations.NotNull;

public class BarnPlotUpgradesMenu extends PlotSettingsMenuGUI<BarnPlot> {

    public BarnPlotUpgradesMenu(@NotNull BarnPlot plot) {
        super(plot, () -> new BarnPlotMainMenu(plot));
    }
}
