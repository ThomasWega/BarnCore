package com.bof.barn.core.region.setting;

import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.setting.PlotSetting;
import org.jetbrains.annotations.NotNull;

public class SettingManager {

    /**
     * Tries to purchase the setting for the given plot
     *
     * @param plot    Plot to purchase the setting for
     * @param setting Setting to purchase
     * @return Whether the purchase succeeded
     */
    public boolean purchaseSetting(@NotNull AbstractPlot plot, @NotNull PlotSetting setting) {
        if (plot.getOwningRegion().hasEnoughCoins(setting.getNextLevelPrice())) {
            setting.upgradeLevel(plot);
            plot.getOwningRegion().removeFarmCoins(setting.getNextLevelPrice());
            return true;
        }

        return false;
    }

    /**
     * Tries to unlock the setting for the given plot
     *
     * @param plot    Plot to unlock the setting for
     * @param setting Setting to unlock
     * @return Whether the purchase succeeded
     */
    public boolean unlockSetting(@NotNull AbstractPlot plot, @NotNull PlotSetting setting) {
        float price = setting.getPrice();
        if (plot.getOwningRegion().hasEnoughCoins(price)) {
            if (setting.setUnlocked(true)) {
                plot.getOwningRegion().removeFarmCoins(price);
            }
            return true;
        }
        return false;
    }
}
