package com.bof.barn.core.region.plot.harvestable.farm.menus;

import com.bof.barn.core.gui.premade.menu.plot.setting.PlotSettingsGUI;
import com.bof.barn.core.region.plot.harvestable.farm.FarmPlot;
import org.jetbrains.annotations.NotNull;

public class FarmPlotUpgradesMenu extends PlotSettingsGUI<FarmPlot> {

    public FarmPlotUpgradesMenu(@NotNull FarmPlot plot, boolean closeOnGoBack) {
        super(plot, () -> new FarmPlotMainMenu(plot, closeOnGoBack));
    }
}
