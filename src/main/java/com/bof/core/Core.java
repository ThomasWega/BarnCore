package com.bof.core;

import com.bof.barn.world_generator.events.GridLoadedEvent;
import com.bof.core.handler.PlayerJoinHandler;
import com.bof.core.region.RegionManager;
import com.bof.core.region.storage.RegionsStorage;
import lombok.Getter;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Core extends JavaPlugin implements Listener {
    public static ComponentLogger LOGGER;

    @Getter
    private final RegionManager regionManager = new RegionManager();

    @EventHandler(priority = EventPriority.LOWEST)
    private void onGridLoad(GridLoadedEvent event) {
        RegionsStorage.convertToRegions();
        this.registerEventsAfterGridLoad();
    }

    @Override
    public void onEnable() {
        LOGGER = getComponentLogger();
        this.registerEvents();
    }

    @Override
    public void onDisable() {
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    private void registerEventsAfterGridLoad() {
        PluginManager p = Bukkit.getPluginManager();
        p.registerEvents(new PlayerJoinHandler(regionManager), this);
    }
}
