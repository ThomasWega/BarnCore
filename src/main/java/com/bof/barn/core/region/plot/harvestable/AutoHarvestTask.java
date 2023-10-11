package com.bof.barn.core.region.plot.harvestable;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AutoHarvestTask<T extends HarvestablePlot<?>> implements Runnable {
    private final T plot;

    @Override
    public void run() {
        if (!plot.isHarvestablePresent()) return;

        // TODO finish
    }
}
