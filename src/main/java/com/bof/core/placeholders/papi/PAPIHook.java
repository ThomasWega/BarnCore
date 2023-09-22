package com.bof.core.placeholders.papi;

import com.bof.core.player.GamePlayer;
import com.bof.core.region.BarnRegion;
import com.bof.core.region.plots.Plot;
import com.bof.core.region.plots.PlotType;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PAPIHook extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "barn";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Wega";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer offPlayer, @NotNull String params) {
        Player player = offPlayer.getPlayer();
        assert player != null;
        GamePlayer gamePlayer = GamePlayer.get(player);
        Optional<BarnRegion> optRegion = gamePlayer.getBarnRegion();
        if (optRegion.isEmpty()) return null;
        BarnRegion region = optRegion.get();

        switch (params) {
            case "inv":
                return String.valueOf(region.getCropsInventory().size());
            case "inv_size":
                return String.valueOf(region.getCropsInventorySize());
        }

        if (params.startsWith("plot_farm_autoharvest")) {
            int plotId = Integer.parseInt(params.substring(22));
            Optional<Plot> optPlot = region.getPlot(PlotType.FARM, plotId);
            if (optPlot.isPresent())
                return String.valueOf(optPlot.get().isAutoHarvest());
        }

        if (params.startsWith("plot_farm_colored_status_autoharvest")) {
            int plotId = Integer.parseInt(params.substring(37));
            Optional<Plot> optPlot = region.getPlot(PlotType.FARM, plotId);
            if (optPlot.isPresent()) {
                return optPlot.get().isAutoHarvest() ? "§aON" : "§cOFF";
            }
        }

        return null;
    }
}
