package com.bof.core.region.plot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum PlotType {
    FARM("farm"),
    ANIMAL("animal"),
    SILO("silo"),
    BARN("barn");

    private final String identifier;

    public static Optional<PlotType> getByIdentifier(@NotNull String identifier) {
        return Arrays.stream(PlotType.values())
                .filter(plotType -> plotType.identifier.equalsIgnoreCase(identifier))
                .findAny();
    }
}
