package com.bof.barn.core.region.setting;

import com.bof.barn.core.region.plot.Plot;
import com.bof.toolkit.utils.NumberUtils;

public interface LeveledSetting {
    int getCurrentLevel();
    int getMaxLevel();
    float getBasePrice();
    boolean upgradeLevel(Plot plot);
    default boolean isAtMaxLevel() {
        return this.getCurrentLevel() == this.getMaxLevel();
    }

    default float getNextLevelPrice() {
        float priceMultiplier = 1.2f;

        return NumberUtils.roundBy(this.getBasePrice() * Math.pow(priceMultiplier, this.getCurrentLevel()), 2).floatValue();
    }
}
