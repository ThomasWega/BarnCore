package com.bof.barn.core.region.plot.harvestable.animal.menu;

import com.bof.barn.core.gui.premade.menu.harvestable.settings.HarvestablePlotSettingGUI;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.settings.AutoStoreSetting;
import org.jetbrains.annotations.NotNull;

public class AnimalAutoStoreMenu extends HarvestablePlotSettingGUI<AutoStoreSetting> {

    public AnimalAutoStoreMenu(@NotNull BarnRegion region) {
        super(region, PlotType.ANIMAL, AutoStoreSetting.class, new AnimalSettingsMainMenu(region));
    }
}