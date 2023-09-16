package com.bof.core;

import com.bof.core.handler.GridLoadedHandler;
import com.bof.core.handler.PlayerJoinHandler;
import com.bof.core.region.RegionManager;
import lombok.Getter;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Core extends JavaPlugin {
    public static ComponentLogger LOGGER;

    @Getter
    private final RegionManager regionManager = new RegionManager();

    @Override
    public void onEnable() {
        LOGGER = getComponentLogger();
        this.registerEvents();
    }

    @Override
    public void onDisable() {
    }

    private void registerEvents() {
        PluginManager p = Bukkit.getPluginManager();
        p.registerEvents(new GridLoadedHandler(), this);
        p.registerEvents(new PlayerJoinHandler(regionManager), this);
    }
}
