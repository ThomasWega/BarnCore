package com.bof.core.region.plot.harvestable;


import com.bof.core.region.plot.Plot;

public interface HarvestablePlot<T extends HarvestableType> extends Plot {
    boolean isAutoStore();
    void setAutoStore(boolean autoStore);
    T getCurrentlyHarvesting();
}
