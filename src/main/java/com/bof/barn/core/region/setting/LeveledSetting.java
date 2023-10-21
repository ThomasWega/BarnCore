package com.bof.barn.core.region.setting;

import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.toolkit.utils.NumberUtils;

/**
 * Used to specify {@link com.bof.barn.core.region.plot.setting.PlotSetting} which has levels (tiers)
 */
public interface LeveledSetting {
    int getCurrentLevel();

    int getMaxLevel();

    float getBasePrice();

    /**
     * Increases the level by one, while updating all the necessary stuff
     *
     * @param plot Plot that issued the ugrade
     * @return whether the upgrade succeeded
     */
    boolean upgradeLevel(AbstractPlot plot);

    /**
     * @return Whether the setting is currently at it's max level
     */
    default boolean isAtMaxLevel() {
        return this.getCurrentLevel() == this.getMaxLevel();
    }

    default float getNextLevelPrice() {
        float priceMultiplier = 1.2f;

        return NumberUtils.roundBy(this.getBasePrice() * Math.pow(priceMultiplier, this.getCurrentLevel()), 2).floatValue();
    }
}
