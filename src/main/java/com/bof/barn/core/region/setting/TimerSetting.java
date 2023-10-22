package com.bof.barn.core.region.setting;

import com.bof.barn.core.region.plot.setting.PlotSetting;

/**
 * Used to specify {@link com.bof.barn.core.region.plot.setting.PlotSetting} which has timer that runs
 */
public interface TimerSetting {
    long getCurrentTickSpeed();

    /**
     * @return value of the next tick speed
     */
    default long getNextTickSpeed() {
        if (this instanceof PlotSetting setting && setting.isAtMaxLevel()) {
            return this.getCurrentTickSpeed();
        }
        return this.getCurrentTickSpeed() - 20;
    }
}
