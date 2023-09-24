package com.bof.core.region.plot;


public interface HarvestablePlot<T extends HarvestableType> extends Plot {
    boolean isAutoStore();
    void setAutoStore(boolean autoStore);
    T getCurrentlyHarvesting();
}
