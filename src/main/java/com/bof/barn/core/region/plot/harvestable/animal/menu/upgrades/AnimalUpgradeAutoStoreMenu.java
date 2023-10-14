package com.bof.barn.core.region.plot.harvestable.animal.menu.upgrades;

import com.bof.barn.core.gui.premade.menu.plot.harvestable.settings.HarvestablePlotSettingGUI;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.setting.AutoStoreSetting;
import org.jetbrains.annotations.NotNull;

public class AnimalUpgradeAutoStoreMenu extends HarvestablePlotSettingGUI<AutoStoreSetting> {

    public AnimalUpgradeAutoStoreMenu(@NotNull BarnRegion region) {
        super(region, PlotType.ANIMAL, AutoStoreSetting.class, new AnimalUpgradesMainMenu(region));
    }
}