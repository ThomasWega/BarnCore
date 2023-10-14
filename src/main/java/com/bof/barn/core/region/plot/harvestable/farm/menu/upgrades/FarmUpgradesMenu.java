package com.bof.barn.core.region.plot.harvestable.farm.menu.upgrades;

import com.bof.barn.core.gui.premade.menu.harvestable.settings.HarvestablePlotSettingGUI;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.setting.AutoStoreSetting;
import org.jetbrains.annotations.NotNull;

public class FarmUpgradesMenu extends HarvestablePlotSettingGUI<AutoStoreSetting> {

    public FarmUpgradesMenu(@NotNull BarnRegion region) {
        super(region, PlotType.FARM, AutoStoreSetting.class, new FarmPlotUpgradesMenu(region));
    }


}