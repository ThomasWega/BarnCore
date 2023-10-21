package com.bof.barn.core.region.plot;

import com.bof.barn.core.Core;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.event.setting.PlotSettingToggleEvent;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalPlot;
import com.bof.barn.core.region.plot.harvestable.farm.FarmPlot;
import com.bof.barn.core.region.plot.container.barn.BarnPlot;
import com.bof.barn.core.region.plot.container.silo.SiloPlot;
import com.bof.barn.core.region.plot.setting.PlotSetting;
import com.bof.barn.core.utils.BoxUtils;
import com.bof.toolkit.utils.ColorUtils;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import lombok.Data;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Data
public abstract class AbstractPlot {
    /**
     * @return Instance of the plugin
     */
    private final @NotNull Core plugin;
    /**
     * Holds all the settings for the given plot and their values.
     * The setting can be retrieved by its class
     *
     * @return Setting instance that contains additional info and values
     */
    private final @NotNull Map<Class<? extends PlotSetting>, PlotSetting> settings = new HashMap<>();
    private final @NotNull PlotType type;
    /**
     * @return Region that owns this plot
     */
    private final @NotNull BarnRegion owningRegion;
    private final @NotNull BoundingBox box;
    /**
     * @return All blocks the {@link BoundingBox} contains
     */
    private final Set<Block> boxBlocks;
    private final int id;
    private Hologram hologram;

    public AbstractPlot(@NotNull Core plugin, @NotNull PlotType type, @NotNull BarnRegion owningRegion, @NotNull BoundingBox box, int id) {
        this.plugin = plugin;
        this.type = type;
        this.id = id;
        this.owningRegion = owningRegion;
        this.box = box;
        this.boxBlocks = BoxUtils.getBlocksInBox(box, true);
    }

    /**
     * @param type         Type of the plot
     * @param owningRegion Region that owns the plot
     * @param box          Box the plot is in
     * @param id           ID of the plot
     * @return new Plot instance depending on the type
     */
    public static AbstractPlot newPlot(@NotNull Core plugin, @NotNull PlotType type, @NotNull BarnRegion owningRegion, @NotNull BoundingBox box, int id) {
        AbstractPlot plot = null;
        switch (type) {
            case FARM -> plot = new FarmPlot(plugin, owningRegion, box, id);
            case SILO -> plot = new SiloPlot(plugin, owningRegion, box, id);
            case ANIMAL -> plot = new AnimalPlot(plugin, owningRegion, box, id);
            case BARN -> plot = new BarnPlot(plugin, owningRegion, box, id);
        }
        return plot;
    }

    /**
     * @return All settings that are toggled true
     */
    public @NotNull List<? extends PlotSetting> getToggledSettings() {
        return this.getSettings().values().stream()
                .filter(PlotSetting::isToggled)
                .toList();
    }

    /**
     * @return All settings that are locked
     * @see #getUnlockedSettings()
     */
    public @NotNull List<? extends PlotSetting> getLockedSettings() {
        return this.getSettings().values().stream()
                .filter(plotSetting -> !plotSetting.isUnlocked())
                .toList();
    }

    /**
     * @return All settings that are unlocked
     * @see #getLockedSettings()
     */
    public @NotNull List<? extends PlotSetting> getUnlockedSettings() {
        return this.getSettings().values().stream()
                .filter(PlotSetting::isUnlocked)
                .toList();
    }

    /**
     * Check if the given plot has the setting mapped
     *
     * @param settingClazz The specific Setting class
     * @return whether the plot even has this setting
     */
    public boolean hasSetting(@NotNull Class<? extends PlotSetting> settingClazz) {
        return this.getSettings().containsKey(settingClazz);
    }

    /**
     * Get the instance of the setting
     *
     * @param settingClazz Class of the settings
     * @param <T>          Which exact setting to get
     * @return Instance of the setting
     */
    public <T extends PlotSetting> T getSetting(@NotNull Class<T> settingClazz) {
        //noinspection unchecked
        return (T) this.getSettings().get(settingClazz);
    }

    /**
     * @param settingClazz Class of the specific setting
     * @return the toggle boolean of the setting
     */
    public boolean getSettingToggle(@NotNull Class<? extends PlotSetting> settingClazz) {
        return this.getSetting(settingClazz).isToggled();
    }

    /**
     * Switches the toggle boolean for the given setting (e.g. from true to false)
     *
     * @param settingClazz Class of the specific setting
     * @return New value of the toggle boolean
     * @see #setSetting(Class, boolean)
     */
    public boolean switchSettingToggle(@NotNull Class<? extends PlotSetting> settingClazz) {
        boolean newValue = !this.getSettingToggle(settingClazz);
        this.setSetting(settingClazz, newValue);
        return newValue;
    }

    /**
     * Check for the given setting
     *
     * @param settingClazz The specific Setting class
     * @return whether the setting is true
     * @see #isSetting(Class, boolean)
     */
    public boolean isSetting(@NotNull Class<? extends PlotSetting> settingClazz) {
        return this.isSetting(settingClazz, true);
    }

    /**
     * Check for the given setting and value
     *
     * @param settingClazz The specific Setting class
     * @param value        Value to check against
     * @return whether the setting is true
     * @see #isSetting(Class)
     */
    public boolean isSetting(@NotNull Class<? extends PlotSetting> settingClazz, boolean value) {
        return this.getSettings().get(settingClazz).isToggled() == value;
    }

    /**
     * Sets the setting to true
     *
     * @param settingClazz Class of the setting to set a value for
     * @see #setSetting(Class, boolean)
     */
    public void setSetting(@NotNull Class<? extends PlotSetting> settingClazz) {
        this.setSetting(settingClazz, true);
    }

    /**
     * Sets the setting to the given value, updates the hologram and calls the {@link PlotSettingToggleEvent}
     *
     * @param settingClazz Class of the setting to set a value for
     * @param value        Value to set the setting to
     * @see #setSetting(Class)
     */
    public void setSetting(@NotNull Class<? extends PlotSetting> settingClazz, boolean value) {
        PlotSetting setting = this.getSettings().get(settingClazz);
        setting.setToggled(value);
        this.updateHologram();
        Bukkit.getPluginManager().callEvent(new PlotSettingToggleEvent(this, setting));
    }

    /**
     * Determines what should happen when the plot's hologram is clicked
     */
    public abstract Consumer<PlayerHologramInteractEvent> getHologramAction();


    /**
     * @return Display name that should be used in items and holograms
     * @see #getLore()
     */
    public abstract Component getDisplayName();

    /**
     * @return Lore that should be used in items and holograms
     * @see #getDisplayName()
     */
    public abstract List<Component> getLore();

    /**
     * Handles updating every line of the hologram including updating the placeholders
     */
    public abstract void updateHologram();

    /**
     * Parses placeholders in a collection of strings and converts them into a list of
     * components. Placeholders are applied individually for each online player in the
     * owning region, and the results are collected into components.
     * <p></p>
     * If no online players are present, the method will return the original strings
     * without parsing placeholders.
     *
     * @param strings A collection of strings containing placeholders to parse and convert.
     * @return A list of components generated from the parsed and converted strings.
     * @throws NullPointerException if the input collection or any of its elements are null.
     */
    public @NotNull List<Component> parsePlaceholdersAndConvertToComponent(@NotNull Collection<String> strings) {
        Set<Player> onlinePlayers = this.getOwningRegion().getAllOnlinePlayers();

        if (onlinePlayers.isEmpty()) {
            // No players are online, return the original strings without parsing placeholders
            return strings.stream()
                    .map(ColorUtils::convertLegacyToMiniMessage)
                    .map(MiniMessage.miniMessage()::deserialize)
                    .collect(Collectors.toList());
        }

        return strings.stream()
                .map(s -> {
                    // Assuming you want to apply placeholders for each player
                    List<String> parsedPlaceholders = onlinePlayers.stream()
                            .map(player -> PlaceholderAPI.setPlaceholders(player, s))
                            .toList();

                    return parsedPlaceholders.stream()
                            .map(ColorUtils::colorLegacyAndMiniMessage)
                            .collect(Collectors.toList()); // Collect the components for each string
                })
                .flatMap(List::stream) // Flatten the List<List<Component>> to List<Component>
                .collect(Collectors.toList());
    }
}