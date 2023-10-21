package com.bof.barn.core.region.plot.harvestable.animal.menus;

import com.bof.barn.core.gui.premade.menu.plot.setting.PlotSettingsGUI;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalPlot;
import org.jetbrains.annotations.NotNull;

public class AnimalPlotUpgradesMenu extends PlotSettingsGUI<AnimalPlot> {

    public AnimalPlotUpgradesMenu(@NotNull AnimalPlot plot, boolean closeOnGoBack) {
        super(plot, () -> new AnimalPlotMainMenu(plot, closeOnGoBack));
    }
}
