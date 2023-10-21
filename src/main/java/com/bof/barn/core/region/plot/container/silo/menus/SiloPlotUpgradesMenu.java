package com.bof.barn.core.region.plot.container.silo.menus;

import com.bof.barn.core.gui.premade.menu.plot.setting.PlotSettingsMenuGUI;
import com.bof.barn.core.region.plot.container.silo.SiloPlot;
import org.jetbrains.annotations.NotNull;

public class SiloPlotUpgradesMenu extends PlotSettingsMenuGUI<SiloPlot> {

    public SiloPlotUpgradesMenu(@NotNull SiloPlot plot) {
        super(plot, () -> new SiloPlotMainMenu(plot));
    }
}