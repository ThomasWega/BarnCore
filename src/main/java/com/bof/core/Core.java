package com.bof.core;

import com.bof.barn.world_generator.event.GridLoadedEvent;
import com.bof.core.region.plots.farm.handler.CropsTrampingHandler;
import com.bof.core.region.plots.farm.handler.PlayerFarmPlotHandler;
import com.bof.core.region.handler.PlayerRegionAssignHandler;
import com.bof.core.placeholders.papi.PAPIHook;
import com.bof.core.region.plots.PlotHoloManager;
import com.bof.core.region.plots.PlotManager;
import com.bof.core.region.RegionManager;
import com.bof.core.region.spawn.SpawnManager;
import com.bof.core.region.storage.RegionStorage;
import com.bof.toolkit.file.FileLoader;
import lombok.Getter;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
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

    @EventHandler(priority = EventPriority.LOWEST)
    private void onGridLoad(GridLoadedEvent event) {
        this.regionManager = new RegionManager(this);
        this.registerEventsAfterGridLoad();
        this.regionStorage.convertToRegions();
    }

    @Override
    public void onEnable() {
        LOGGER = getComponentLogger();
        this.loadFiles();
        this.registerEvents();
        this.registerPAPIPlaceholders();
    }

    @Override
    public void onDisable() {
    }

    private void registerEvents() {
        PluginManager p = Bukkit.getPluginManager();
        p.registerEvents(this, this);
    }

    private void registerEventsAfterGridLoad() {
        PluginManager p = Bukkit.getPluginManager();
        p.registerEvents(new PlotManager(), this);
        p.registerEvents(new PlotHoloManager(this), this);
        p.registerEvents(new SpawnManager(), this);
        p.registerEvents(new PlayerRegionAssignHandler(this), this);
        p.registerEvents(regionManager, this);
        p.registerEvents(new PlayerFarmPlotHandler(), this);
        p.registerEvents(new CropsTrampingHandler(), this);
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
