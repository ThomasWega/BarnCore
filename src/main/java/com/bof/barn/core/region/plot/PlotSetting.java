package com.bof.barn.core.region.plot;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class PlotSetting {
    private final String settingName;
    boolean toggled;

    public PlotSetting(@NotNull String settingName, boolean toggled) {
        this.settingName = settingName;
        this.toggled = toggled;
        values.put(this.getClass(), settingName);
    }

    protected static Map<Class<? extends PlotSetting>, String> values = new HashMap<>();

    public static String getSettingName(Class<? extends PlotSetting> clazz) {
        return values.get(clazz);
    }
}
