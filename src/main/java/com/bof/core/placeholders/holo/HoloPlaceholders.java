package com.bof.core.placeholders.holo;

import com.github.unldenis.hologram.placeholder.Placeholders;
import lombok.Getter;

public class HoloPlaceholders {
    @Getter
    private static final Placeholders placeholders = new Placeholders(Placeholders.STRING | Placeholders.PAPI);
}
