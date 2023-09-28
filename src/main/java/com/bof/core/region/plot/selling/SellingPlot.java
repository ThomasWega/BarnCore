package com.bof.core.region.plot.selling;


import com.bof.core.region.plot.Plot;

public interface SellingPlot extends Plot {
    boolean isAutoSell();
    void setAutoSell(boolean autoSell);
}
