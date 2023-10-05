package com.bof.barn.core.region.plot.harvestable.farm.menu;

import com.bof.barn.core.menu.premade.harvestable.settings.HarvestablePlotSettingMenu;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.settings.AutoStoreSetting;
import org.jetbrains.annotations.NotNull;

public class FarmAutoStoreMenu extends HarvestablePlotSettingMenu<AutoStoreSetting> {

    public FarmAutoStoreMenu(@NotNull BarnRegion region) {
        super(region, PlotType.FARM, AutoStoreSetting.class, new FarmSettingsMainMenu(region));
    }
}