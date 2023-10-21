package com.bof.barn.core.region.plot.harvestable.farm.menus;

import com.bof.barn.core.gui.premade.menu.plot.setting.PlotSettingsMenuGUI;
import com.bof.barn.core.region.plot.harvestable.farm.FarmPlot;
import org.jetbrains.annotations.NotNull;

public class FarmPlotUpgradesMenu extends PlotSettingsMenuGUI<FarmPlot> {

    public FarmPlotUpgradesMenu(@NotNull FarmPlot plot, boolean closeOnGoBack) {
        super(plot, () -> new FarmPlotMainMenu(plot, closeOnGoBack));
    }
}
