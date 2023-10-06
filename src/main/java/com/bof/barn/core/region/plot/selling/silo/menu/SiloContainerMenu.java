package com.bof.barn.core.region.plot.selling.silo.menu;

import com.bof.barn.core.gui.premade.menu.harvestable.HarvestablesContainerGUI;
import com.bof.barn.core.region.plot.harvestable.farm.CropType;
import com.bof.barn.core.region.plot.selling.silo.SiloPlot;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class SiloContainerMenu extends HarvestablesContainerGUI<CropType, SiloPlot> {

    public SiloContainerMenu(@NotNull SiloPlot plot) {
        super(plot, CropType.class, new SiloPlotMainMenu(plot), 6, 0, 0, 9, 5);
    }

    @Override
    public Component getSellMessage(@NotNull CropType type, float sellValue) {
        return Component.text("TO ADD - Sold crop for " + sellValue);
    }
}