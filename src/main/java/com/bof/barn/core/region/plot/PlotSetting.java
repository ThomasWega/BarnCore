package com.bof.barn.core.region.plot;

import lombok.Data;
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
    boolean toggled;

    /**
     * Constructs a new setting with the given setting name and toggle status.
     *
     * @param settingName The name of the plot setting.
     * @param toggled     The initial toggle status of the setting.
     */
    public PlotSetting(@NotNull String settingName, boolean toggled) {
        this.settingName = settingName;
        this.toggled = toggled;
        values.put(this.getClass(), settingName);
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
