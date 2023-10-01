package com.bof.barn.core.placeholders.papi;

import com.bof.barn.core.player.GamePlayer;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.Plot;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.HarvestablePlot;
import com.bof.barn.core.region.plot.selling.silo.SiloPlot;
import com.bof.barn.core.region.plot.selling.barn.BarnPlot;
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
            case "silo_inv":
                return String.valueOf(region.getCropsInventory().size());
            case "silo_inv_size":
                return String.valueOf(region.getCropsInventoryCapacity());
            case "barn_inv":
                return String.valueOf(region.getAnimalInventory().size());
            case "barn_inv_size":
                return String.valueOf(region.getAnimalInventoryCapacity());
            case "silos_filled":
                return String.valueOf(region.getAllSilosFilledAmount());
            case "silos_capacity":
                return String.valueOf(region.getAllSilosCapacityAmount());
            case "barns_filled":
                return String.valueOf(region.getAllBarnsFilledAmount());
            case "barns_capacity":
                return String.valueOf(region.getAllBarnsCapacityAmount());
            case "farmcoins":
                return String.valueOf(region.getFarmCoins());
        }

        if (params.startsWith("plot_farm_autostore_")) {
            int plotId = Integer.parseInt(params.substring(20));
            Optional<Plot> optPlot = region.getPlot(PlotType.FARM, plotId);
            if (optPlot.isPresent())
                return String.valueOf(((HarvestablePlot<?>) optPlot.get()).isAutoStore());
        }

        if (params.startsWith("plot_animal_autostore_")) {
            int plotId = Integer.parseInt(params.substring(22));
            Optional<Plot> optPlot = region.getPlot(PlotType.ANIMAL, plotId);
            if (optPlot.isPresent())
                return String.valueOf(((HarvestablePlot<?>) optPlot.get()).isAutoStore());
        }

        if (params.startsWith("plot_farm_colored_status_autostore_")) {
            int plotId = Integer.parseInt(params.substring(35));
            Optional<Plot> optPlot = region.getPlot(PlotType.FARM, plotId);
            if (optPlot.isPresent()) {
                return ((HarvestablePlot<?>) optPlot.get()).isAutoStore() ? "§aON" : "§cOFF";
            }
        }

        if (params.startsWith("plot_animal_colored_status_autostore_")) {
            int plotId = Integer.parseInt(params.substring(37));
            Optional<Plot> optPlot = region.getPlot(PlotType.ANIMAL, plotId);
            if (optPlot.isPresent()) {
                return ((HarvestablePlot<?>) optPlot.get()).isAutoStore() ? "§aON" : "§cOFF";
            }
        }

        if (params.startsWith("plot_silo_capacity_")) {
            int plotId = Integer.parseInt(params.substring(19));
            Optional<Plot> optPlot = region.getPlot(PlotType.SILO, plotId);
            if (optPlot.isPresent())
                return String.valueOf(((SiloPlot) optPlot.get()).getCapacity());
        }

        if (params.startsWith("plot_barn_capacity_")) {
            int plotId = Integer.parseInt(params.substring(19));
            Optional<Plot> optPlot = region.getPlot(PlotType.BARN, plotId);
            if (optPlot.isPresent())
                return String.valueOf(((BarnPlot) optPlot.get()).getCapacity());
        }

        if (params.startsWith("plot_silo_filled_")) {
            int plotId = Integer.parseInt(params.substring(17));
            Optional<Plot> optPlot = region.getPlot(PlotType.SILO, plotId);
            if (optPlot.isPresent())
                return String.valueOf(((SiloPlot) optPlot.get()).getFilledAmount());
        }

        if (params.startsWith("plot_barn_filled_")) {
            int plotId = Integer.parseInt(params.substring(17));
            Optional<Plot> optPlot = region.getPlot(PlotType.BARN, plotId);
            if (optPlot.isPresent())
                return String.valueOf(((BarnPlot) optPlot.get()).getFilledAmount());
        }

        if (params.startsWith("plot_silo_percentage_filled_")) {
            int plotId = Integer.parseInt(params.substring(28));
            Optional<Plot> optPlot = region.getPlot(PlotType.SILO, plotId);
            if (optPlot.isPresent())
                return String.valueOf(((SiloPlot) optPlot.get()).getFilledPercentageRounded(2));
        }

        if (params.startsWith("plot_barn_percentage_filled_")) {
            int plotId = Integer.parseInt(params.substring(28));
            Optional<Plot> optPlot = region.getPlot(PlotType.BARN, plotId);
            if (optPlot.isPresent())
                return String.valueOf(((BarnPlot) optPlot.get()).getFilledPercentageRounded(2));
        }

        if (params.startsWith("plot_silo_autosell_")) {
            int plotId = Integer.parseInt(params.substring(19));
            Optional<Plot> optPlot = region.getPlot(PlotType.SILO, plotId);
            if (optPlot.isPresent())
                return String.valueOf(((SiloPlot) optPlot.get()).isAutoSell());
        }

        if (params.startsWith("plot_silo_colored_status_autosell_")) {
            int plotId = Integer.parseInt(params.substring(34));
            Optional<Plot> optPlot = region.getPlot(PlotType.SILO, plotId);
            if (optPlot.isPresent())
                return ((SiloPlot) optPlot.get()).isAutoSell() ? "§aON" : "§cOFF";
        }

        if (params.startsWith("plot_barn_autosell_")) {
            int plotId = Integer.parseInt(params.substring(19));
            Optional<Plot> optPlot = region.getPlot(PlotType.BARN, plotId);
            if (optPlot.isPresent())
                return String.valueOf(((BarnPlot) optPlot.get()).isAutoSell());
        }

        if (params.startsWith("plot_barn_colored_status_autosell_")) {
            int plotId = Integer.parseInt(params.substring(34));
            Optional<Plot> optPlot = region.getPlot(PlotType.BARN, plotId);
            if (optPlot.isPresent())
                return ((BarnPlot) optPlot.get()).isAutoSell() ? "§aON" : "§cOFF";
        }

        return null;
    }
}
