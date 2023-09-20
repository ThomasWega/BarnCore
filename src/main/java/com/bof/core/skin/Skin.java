package com.bof.core.skin;

import lombok.Data;

import javax.annotation.Nullable;

/**
 * Stores the texture and signature data of given skin
 */
@Data
public final class Skin {
    @Nullable
    private final String texture;
    @Nullable
    private final String signature;
}
