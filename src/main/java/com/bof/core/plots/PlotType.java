package com.bof.core.plots;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlotType {
    FARM("farm"),
    ANIMAL("animal");

    private final String identifier;
}
