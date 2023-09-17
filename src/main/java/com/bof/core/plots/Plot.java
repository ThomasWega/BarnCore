package com.bof.core.plots;

import org.bukkit.util.BoundingBox;

public interface Plot {
    BoundingBox getBox();
    PlotType getType();
}
