package com.bof.barn.core;

import com.bof.barn.core.enviromental.EnvironmentalHandler;
import com.bof.barn.core.hotbar.PlayerHotbarHandler;
import com.bof.barn.core.region.handler.PlayerRegionAssignHandler;
import com.bof.barn.core.region.plot.PlotHoloManager;
import com.bof.barn.core.region.plot.PlotManager;
import com.bof.barn.core.region.plot.setting.PlotSettingsManager;
import com.bof.barn.core.region.plot.task.PlotTasksManager;
import com.bof.barn.core.region.spawn.SpawnManager;
import com.bof.barn.core.region.storage.RegionStorage;
import com.bof.barn.world_generator.WorldGenerator;
import com.bof.barn.world_generator.event.GridLoadedEvent;
import com.bof.barn.core.placeholders.papi.PAPIHook;
import com.bof.barn.core.region.RegionManager;
import com.bof.barn.core.region.plot.harvestable.animal.handler.PlayerKillAnimalHandler;
import com.bof.barn.core.region.plot.harvestable.farm.handler.CropsTrampingHandler;
import com.bof.barn.core.region.plot.harvestable.farm.handler.PlayerFarmPlotHandler;
import com.bof.toolkit.file.FileLoader;
import lombok.Getter;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

@Getter
public final class Core extends JavaPlugin implements Listener {
    public static ComponentLogger LOGGER;
    private final RegionStorage regionStorage = new RegionStorage();
    private RegionManager regionManager;
    public static World WORLD;

    //TODO add info about upgrades ON/OFF to lore of Upgrades button
    //TODO add comments

    //TODO test island members

    @EventHandler(priority = EventPriority.LOWEST)
    private void onGridLoad(GridLoadedEvent event) {
        WORLD = WorldGenerator.WORLD;
        this.regionManager = new RegionManager(this);
        this.registerEventsAfterGridLoad();
        this.regionStorage.convertToRegions();
        this.onFullEnable();
    }

    @Override
    public void onEnable() {
        LOGGER = getComponentLogger();
        this.loadFiles();
        this.registerEvents();
    }

    public void onFullEnable() {
        this.registerHandlers();
        this.registerPAPIPlaceholders();
    }

    @Override
    public void onDisable() {
    }

    private void registerHandlers() {
        new EnvironmentalHandler(this);
    }

    private void registerEvents() {
        PluginManager p = Bukkit.getPluginManager();
        p.registerEvents(this, this);
    }

    private void registerEventsAfterGridLoad() {
        PluginManager p = Bukkit.getPluginManager();
        p.registerEvents(new PlotManager(this), this);
        p.registerEvents(new PlotHoloManager(this), this);
        p.registerEvents(new PlotSettingsManager(), this);
        p.registerEvents(new PlotTasksManager(this), this);
        p.registerEvents(new SpawnManager(), this);
        p.registerEvents(new PlayerRegionAssignHandler(this), this);
        p.registerEvents(regionManager, this);
        p.registerEvents(new PlayerFarmPlotHandler(), this);
        p.registerEvents(new CropsTrampingHandler(), this);
        p.registerEvents(new PlayerKillAnimalHandler(), this);
        p.registerEvents(new PlayerHotbarHandler(this), this);
    }

    private void loadFiles() {
        try {
            FileLoader.loadAllFiles(getClass().getClassLoader(), getDataFolder(),
                    "config.yml"
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to load one or more config files", e);
        }
    }

    private void registerPAPIPlaceholders() {
        new PAPIHook().register();
    }
}
