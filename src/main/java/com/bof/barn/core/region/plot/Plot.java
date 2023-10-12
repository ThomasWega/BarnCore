package com.bof.barn.core.region.plot;

import com.bof.barn.core.Core;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.harvestable.animal.AnimalPlot;
import com.bof.barn.core.region.plot.harvestable.farm.FarmPlot;
import com.bof.barn.core.region.plot.selling.barn.BarnPlot;
import com.bof.barn.core.region.plot.selling.silo.SiloPlot;
import com.bof.toolkit.utils.ColorUtils;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public interface Plot {

    /**
     * @param type         Type of the plot
     * @param owningRegion Region that owns the plot
     * @param box          Box the plot is in
     * @param id           ID of the plot
     * @return new Plot instance depending on the type
     */
    static Plot newPlot(@NotNull Core plugin, @NotNull PlotType type, @NotNull BarnRegion owningRegion, @NotNull BoundingBox box, int id) {
        Plot plot = null;
        switch (type) {
            case FARM -> plot = new FarmPlot(plugin, owningRegion, box, id);
            case SILO -> plot = new SiloPlot(plugin, owningRegion, box, id);
            case ANIMAL -> plot = new AnimalPlot(plugin, owningRegion, box, id);
            case BARN -> plot = new BarnPlot(plugin, owningRegion, box, id);
        }
        return plot;
    }

    /**
     * @return Instance of the plugin
     */
    @NotNull Core getPlugin();

    /**
     * Holds all the settings for the given plot and their values.
     * The setting can be retrieved by its class
     *
     * @return Setting instance that contains additional info and values
     */
    @NotNull Map<Class<? extends PlotSetting>, PlotSetting> getSettings();


    /**
     * Check if the given plot has the setting mapped
     *
     * @param settingClazz The specific Setting class
     * @return whether the plot even has this setting
     */
    default boolean hasSetting(@NotNull Class<? extends PlotSetting> settingClazz) {
        return this.getSettings().containsKey(settingClazz);
    }

    default <T extends PlotSetting> T getSetting(@NotNull Class<T> settingClazz) {
        //noinspection unchecked
        return (T) this.getSettings().get(settingClazz);
    }

    default boolean getSettingToggle(@NotNull Class<? extends PlotSetting> settingClazz) {
        return this.getSetting(settingClazz).isToggled();
    }

    /**
     * Check for the given setting
     *
     * @param settingClazz The specific Setting class
     * @return whether the setting is true
     * @see #isSetting(Class, boolean)
     */
    default boolean isSetting(@NotNull Class<? extends PlotSetting> settingClazz) {
        return this.isSetting(settingClazz, true);
    }

    /**
     * Check for the given setting and value
     *
     * @param settingClazz The specific Setting class
     * @param value Value to check against
     * @return whether the setting is true
     * @see #isSetting(Class)
     */
    default boolean isSetting(@NotNull Class<? extends PlotSetting> settingClazz, boolean value) {
        return this.getSettings().get(settingClazz).isToggled() == value;
    }

    /**
     * Sets the setting to true
     *
     * @param settingClazz Class of the setting to set a value for
     * @see #setSetting(Class, boolean)
     */
    default void setSetting(@NotNull Class<? extends PlotSetting> settingClazz) {
        this.setSetting(settingClazz, true);
    }

    /**
     * Sets the setting to the given value
     *
     * @param settingClazz Class of the setting to set a value for
     * @param value Value to set the setting to
     * @see #setSetting(Class)
     */
    default void setSetting(@NotNull Class<? extends PlotSetting> settingClazz, boolean value) {
        this.getSettings().get(settingClazz).setToggled(value);
        this.updateHologram();
    }

    PlotType getType();

    Hologram getHologram();

    void setHologram(@NotNull Hologram holo);

    int getId();

    /**
     * Determines what should happen when the plot's hologram is clicked
     */
    Consumer<PlayerHologramInteractEvent> getHologramAction();


    /**
     * @return Display name that should be used in items and holograms
     * @see #getLore()
     */
    Component getDisplayName();

    /**
     * @return Lore that should be used in items and holograms
     * @see #getDisplayName()
     */
    List<Component> getLore();

    /**
     * @return Region that owns this plot
     */
    BarnRegion getOwningRegion();

    BoundingBox getBox();

    /**
     * @return All blocks the {@link BoundingBox} contains
     */
    Set<Block> getBoxBlocks();

    void updateHologram();

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
    default @NotNull List<Component> parsePlaceholdersAndConvertToComponent(@NotNull Collection<String> strings) {
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
                            .map(ColorUtils::convertLegacyToMiniMessage)
                            .map(MiniMessage.miniMessage()::deserialize)
                            .collect(Collectors.toList()); // Collect the components for each string
                })
                .flatMap(List::stream) // Flatten the List<List<Component>> to List<Component>
                .collect(Collectors.toList());
    }
}
