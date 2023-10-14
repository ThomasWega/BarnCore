package com.bof.barn.core.region.plot.setting;

import com.bof.barn.core.Purchasable;
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
    boolean toggled;
    boolean unlocked = false;

    /**
     * Constructs a new setting with the given setting name and toggle status.
     *
     * @param settingName The name of the plot setting.
     * @param item        Item used for this upgrade
     * @param price       Price of the setting
     * @param toggled     The initial toggle status of the setting.
     */
    public PlotSetting(@NotNull String settingName, @NotNull ItemStack item, float price, boolean toggled) {
        this.settingName = settingName;
        this.toggled = toggled;
        this.price = price;
        this.item = item;
        values.put(this.getClass(), settingName);
    }

    /**
     * Constructs a new setting with the given setting name and toggle status.
     *
     * @param settingName The name of the plot setting.
     * @param item        Item used for this upgrade
     * @param price       Price of the setting
     * @param unlocked    Whether the setting is unlocked
     * @param toggled     The initial toggle status of the setting.
     */
    public PlotSetting(@NotNull String settingName, @NotNull ItemStack item, float price, boolean unlocked, boolean toggled) {
        this.settingName = settingName;
        this.toggled = toggled;
        this.unlocked = unlocked;
        this.price = price;
        this.item = item;
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
}
