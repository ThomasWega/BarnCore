package com.bof.barn.core.region.plot.harvestable.farm.menu;

import com.bof.barn.core.menu.premade.harvestable.ChangeHarvestablesMenu;
import com.bof.barn.core.region.plot.harvestable.farm.CropType;
import com.bof.barn.core.region.plot.harvestable.farm.FarmPlot;
import com.github.stefvanschie.inventoryframework.adventuresupport.StringHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang.WordUtils;
import org.jetbrains.annotations.NotNull;

public class FarmChangeCropsMenu extends ChangeHarvestablesMenu<CropType, FarmPlot> {

    public FarmChangeCropsMenu(@NotNull FarmPlot plot, boolean closeMainPlotMenuOnGoBack) {
        super(CropType.class, plot, new FarmPlotMainMenu(plot, closeMainPlotMenuOnGoBack), StringHolder.of("Select Crops type"));
    }

    @Override
    public Component getChangeToTypeText(@NotNull CropType type) {
        return MiniMessage.miniMessage().deserialize("<white>Change crops to " + WordUtils.capitalizeFully(type.name()) + "</white>");
    }

    @Override
    public Component getChangedToTypeMessage(@NotNull CropType type) {
        return Component.text("TO ADD - changed the crops type to " + WordUtils.capitalizeFully(type.name()));
    }

    @Override
    public Component getCurrentlyHarvestingText(@NotNull CropType Type) {
        return MiniMessage.miniMessage().deserialize("<dark_gray>Currently planted</dark_gray>");
    }
}