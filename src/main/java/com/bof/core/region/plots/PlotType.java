package com.bof.core.region.plots;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlotType {
    FARM("farm"),
    ANIMAL("animal"),
    SILO("silo");

    private final String identifier;
}
