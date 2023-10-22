package com.bof.barn.core.region.setting;

import com.bof.barn.core.region.plot.setting.PlotSetting;

import java.util.Random;

/**
 * Used to specify {@link com.bof.barn.core.region.plot.setting.PlotSetting} which has a chance to occur
 */
public interface ChanceSetting {
    Random random = new Random();

    /**
     * @return 0 - 1.0 chance
     */
    float getCurrentChance();

    /**
     * @return 0 - 1.0 value of the next chance
     */
    default float getNextChance() {
        if (this instanceof PlotSetting setting && setting.isAtMaxLevel()) {
            return this.getCurrentChance();
        }
        return this.getCurrentChance() + 0.1f;
    }

    default boolean shouldRun() {
        return random.nextFloat() <= this.getCurrentChance();
    }
}
