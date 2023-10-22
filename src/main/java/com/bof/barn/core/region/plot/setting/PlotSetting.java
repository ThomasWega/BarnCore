package com.bof.barn.core.region.plot.setting;

import com.bof.barn.core.Purchasable;
import com.bof.barn.core.item.ItemBuilder;
import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.event.setting.PlotSettingLevelIncreaseEvent;
import com.bof.barn.core.region.setting.ChanceSetting;
import com.bof.barn.core.region.setting.SettingState;
import com.bof.barn.core.region.setting.TimerSetting;
import com.bof.toolkit.utils.NumberUtils;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
     * A map that stores the setting instance for each subclass of PlotSetting.
     * This map is used to retrieve the setting name and other things for a specific subclass.
     */
    protected static Map<Class<? extends PlotSetting>, PlotSetting> values = new HashMap<>();
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
     * @param price        Base Price of the setting
     * @param initialState The initial toggle status of the setting.
     */
    public PlotSetting(@NotNull String settingName, @NotNull ItemStack item, float price, @NotNull SettingState initialState) {
        this.settingName = settingName;
        this.state = initialState;
        this.price = price;
        this.item = item;
        values.put(this.getClass(), this);
    }

    /**
     * Constructs a new setting with the given setting name and toggle status.
     *
     * @param settingName  The name of the plot setting.
     * @param item         Item used for this upgrade
     * @param price        Base Price of the setting
     * @param initialState The initial toggle status of the setting.
     * @param maxLevel     Max reachable level for this setting
     */
    public PlotSetting(@NotNull String settingName, @NotNull ItemStack item, float price, @NotNull SettingState initialState, int maxLevel) {
        this.settingName = settingName;
        this.state = initialState;
        this.price = price;
        this.item = item;
        this.maxLevel = maxLevel;
        values.put(this.getClass(), this);
    }

    /**
     * Get the name of the setting for a specific subclass of PlotSetting.
     *
     * @param clazz The subclass of PlotSetting for which to retrieve the setting name.
     * @return The setting name associated with the given subclass
     */
    public static @NotNull String getSettingName(Class<? extends PlotSetting> clazz) {
        return values.get(clazz).getSettingName();
    }

    /**
     * Get the ItemStack of the setting for a specific subclass of PlotSetting.
     *
     * @param clazz The subclass of PlotSetting for which to retrieve the setting name.
     * @return The ItemStack associated with the given subclass
     */
    public static @NotNull ItemStack getItem(Class<? extends PlotSetting> clazz) {
        return values.get(clazz).getItem();
    }

    /**
     * Creates an ItemBuilder with PlotSetting information for display.
     *
     * @param plot     The associated AbstractPlot.
     * @param setting  The PlotSetting to be displayed.
     * @param existing The existing ItemBuilder to extend.
     * @return An ItemBuilder with added setting details and instructions.
     * @throws NullPointerException if any input parameter is null.
     */
    public static @NotNull ItemBuilder getBuilderWithInfo(@NotNull AbstractPlot plot, @NotNull PlotSetting setting, @NotNull ItemBuilder existing) {
        if (setting instanceof ChanceSetting chanceSetting) {
            existing.appendLoreLine(Component.empty())
                    .appendLoreLine(Component.text("Chance: " + NumberUtils.roundBy(chanceSetting.getCurrentChance(), 2) + "%", NamedTextColor.WHITE));

            if (!setting.isAtMaxLevel()) {
                existing.appendLoreLine(Component.text("Next Chance: " + NumberUtils.roundBy(chanceSetting.getNextChance(), 2) + "%", NamedTextColor.WHITE));
            }
        }

        if (setting instanceof TimerSetting timerSetting) {
            existing.appendLoreLine(Component.empty())
                    .appendLoreLine(Component.text("Interval: " + NumberUtils.roundBy((float) timerSetting.getCurrentTickSpeed() / 20, 2) + "s", NamedTextColor.WHITE));

            if (!setting.isAtMaxLevel()) {
                existing.appendLoreLine(Component.text("Next Interval: " + NumberUtils.roundBy((float) timerSetting.getNextTickSpeed() / 20, 2) + "s", NamedTextColor.WHITE));
            }
        }

        existing.appendLoreLine(Component.empty())
                .appendLoreLine(Component.text("Level: " + setting.getCurrentLevel() + "/" + setting.getMaxLevel(), NamedTextColor.WHITE));

        if (!setting.isAtMaxLevel()) {
            existing.appendLoreLine(Component.text("Next level price: " + setting.getNextLevelPrice(), NamedTextColor.WHITE))
                    .appendLoreLine(Component.text("Your balance: " + plot.getOwningRegion().getFarmCoinsRounded(2) + "$", NamedTextColor.WHITE))
                    .appendLoreLine(Component.empty())
                    .appendLoreLine(Component.text("Shift-click to upgrade level", NamedTextColor.YELLOW));
        }

        return existing.appendLoreLine(Component.text("Click to change status", NamedTextColor.GREEN));
    }

    public boolean isToggled() {
        return this.getState().isToggled();
    }

    public boolean isUnlocked() {
        return this.getState().isUnlocked();
    }

    public boolean isLocked() {
        return this.getState().isLocked();
    }

    /**
     * @param unlocked Whether the setting should be unlocked
     * @return Whether the state was changed
     */
    public boolean setUnlocked(boolean unlocked) {
        if (unlocked && this.isLocked()) {
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
     *
     * @return Price of the next level
     */
    public float getNextLevelPrice() {
        if (this.getState().isLocked()) {
            return this.getPrice();
        }

        float priceMultiplier = 1.2f;
        return NumberUtils.roundBy(this.getPrice() * Math.pow(priceMultiplier, this.getCurrentLevel()), 2).floatValue();
    }

    /**
     * Tries to upgrade the level. Checks all requirements and max level
     *
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
     *
     * @param plot Plot that owns the setting
     */
    public abstract void upgradeAction(@NotNull AbstractPlot plot);
}
