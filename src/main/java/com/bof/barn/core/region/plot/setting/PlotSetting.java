package com.bof.barn.core.region.plot.setting;

import com.bof.barn.core.Purchasable;
import com.bof.barn.core.region.setting.SettingState;
import lombok.Data;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * The abstract base class for plot settings.
 * Plot settings represent various configuration options for plots in a region.
 * Subclasses of this class define specific settings and their behavior.
 */
@Data
public abstract class PlotSetting implements Purchasable {
    /**
     * A map that stores the setting name for each subclass of PlotSetting.
     * This map is used to retrieve the setting name for a specific subclass.
     */
    protected static Map<Class<? extends PlotSetting>, String> values = new HashMap<>();
    private final String settingName;
    private final ItemStack item;
    private final float price;
    private @NotNull SettingState state;

    /**
     * Constructs a new setting with the given setting name and toggle status.
     *
     * @param settingName The name of the plot setting.
     * @param item        Item used for this upgrade
     * @param price       Price of the setting
     * @param state     The initial toggle status of the setting.
     */
    public PlotSetting(@NotNull String settingName, @NotNull ItemStack item, float price, @NotNull SettingState state) {
        this.settingName = settingName;
        this.state = state;
        this.price = price;
        this.item = item;
        values.put(this.getClass(), settingName);
    }

    public boolean isToggled() {
        return this.getState().isToggled();
    }

    public boolean isUnlocked() {
        return this.getState().isUnlocked();
    }

    /**
     * @param unlocked Whether the setting should be unlocked
     * @return Whether the state was changed
     */
    public boolean setUnlocked(boolean unlocked) {
        if (unlocked && !this.isUnlocked()) {
            this.setState(SettingState.OFF);
            return true;
        } else if (!unlocked) {
            this.setState(SettingState.LOCKED);
            return true;
        }
        return false;
    }

    /**
     * Get the name of the setting for a specific subclass of PlotSetting.
     *
     * @param clazz The subclass of PlotSetting for which to retrieve the setting name.
     * @return The setting name associated with the given subclass, or null if not found.
     */
    public static String getSettingName(Class<? extends PlotSetting> clazz) {
        return values.get(clazz);
    }
}
