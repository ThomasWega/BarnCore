package com.bof.barn.core.region.plot;

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
public abstract class PlotSetting {
    private final String settingName;
    private final ItemStack item;
    boolean toggled;

    /**
     * Constructs a new setting with the given setting name and toggle status.
     *
     * @param settingName The name of the plot setting.
     * @param item Item used for this upgrade
     * @param toggled     The initial toggle status of the setting.
     */
    public PlotSetting(@NotNull String settingName, @NotNull ItemStack item, boolean toggled) {
        this.settingName = settingName;
        this.toggled = toggled;
        this.item = item;
        values.put(this.getClass(), settingName);
    }

    /**
     * Switch the toggle to opposite boolean
     * @return new boolean
     */
    public boolean switchToggle() {
        this.toggled = !toggled;
        return toggled;
    }

    /**
     * A map that stores the setting name for each subclass of PlotSetting.
     * This map is used to retrieve the setting name for a specific subclass.
     */
    protected static Map<Class<? extends PlotSetting>, String> values = new HashMap<>();

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
