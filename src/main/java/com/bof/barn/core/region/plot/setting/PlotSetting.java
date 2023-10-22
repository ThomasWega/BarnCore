package com.bof.barn.core.region.plot.setting;

import com.bof.barn.core.Purchasable;
import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.event.setting.PlotSettingLevelIncreaseEvent;
import com.bof.barn.core.region.setting.SettingState;
import com.bof.toolkit.utils.NumberUtils;
import lombok.Data;
import org.bukkit.Bukkit;
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
    private int currentLevel = 1;
    private int maxLevel = 1;

    /**
     * Constructs a new setting with the given setting name and toggle status.
     *
     * @param settingName  The name of the plot setting.
     * @param item         Item used for this upgrade
     * @param price    Base Price of the setting
     * @param initialState The initial toggle status of the setting.
     */
    public PlotSetting(@NotNull String settingName, @NotNull ItemStack item, float price, @NotNull SettingState initialState) {
        this.settingName = settingName;
        this.state = initialState;
        this.price = price;
        this.item = item;
        values.put(this.getClass(), settingName);
    }

    /**
     * Constructs a new setting with the given setting name and toggle status.
     *
     * @param settingName  The name of the plot setting.
     * @param item         Item used for this upgrade
     * @param price    Base Price of the setting
     * @param initialState The initial toggle status of the setting.
     * @param maxLevel     Max reachable level for this setting
     */
    public PlotSetting(@NotNull String settingName, @NotNull ItemStack item, float price, @NotNull SettingState initialState, int maxLevel) {
        this.settingName = settingName;
        this.state = initialState;
        this.price = price;
        this.item = item;
        this.maxLevel = maxLevel;
        values.put(this.getClass(), settingName);
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


    public boolean isAtMaxLevel() {
        return this.currentLevel >= maxLevel;
    }

    /**
     * Calculates the price of the next level by using modifier
     * @return Price of the next level
     */
    public float getNextLevelPrice() {
        float priceMultiplier = 1.2f;

        return NumberUtils.roundBy(this.getPrice() * Math.pow(priceMultiplier, this.getCurrentLevel()), 2).floatValue();
    }

    /**
     * Tries to upgrade the level. Checks all requirements and max level
     * @param plot Plot that owns the setting
     * @return Whether the operation succeeded
     */
    public boolean upgradeLevel(@NotNull AbstractPlot plot) {
        if (this.isAtMaxLevel()) return false;

        currentLevel++;
        this.upgradeAction(plot);
        Bukkit.getPluginManager().callEvent(new PlotSettingLevelIncreaseEvent(plot, this));
        return true;
    }

    /**
     * What to do when {@link #upgradeLevel(AbstractPlot)} succeeds
     * @param plot Plot that owns the setting
     */
    public abstract void upgradeAction(@NotNull AbstractPlot plot);
}
