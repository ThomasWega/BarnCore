package com.bof.barn.core.region.setting;

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

    default boolean shouldRun() {
        return random.nextFloat() <= this.getCurrentChance();
    }
}
