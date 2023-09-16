package com.bof.core.handler;

import com.bof.barn.world_generator.events.GridLoadedEvent;
import com.bof.core.region.storage.RegionsStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class GridLoadedHandler implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    private void onGridLoad(GridLoadedEvent event) {
        RegionsStorage.convertToRegions();
    }
}
