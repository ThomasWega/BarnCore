package com.bof.barn.core.placeholders.papi;

import com.bof.barn.core.player.GamePlayer;
import com.bof.barn.core.region.BarnRegion;
import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.barn.core.region.plot.harvestable.setting.AutoStoreSetting;
import com.bof.barn.core.region.plot.container.barn.BarnPlot;
import com.bof.barn.core.region.plot.container.settings.AutoSellSetting;
import com.bof.barn.core.region.plot.container.silo.SiloPlot;
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
            case "farmcoins_rounded":
                return String.valueOf(region.getFarmCoinsRounded(2));
        }

        if (params.startsWith("plot_farm_setting_autostore_")) {
            int plotId = Integer.parseInt(params.substring(28));
            Optional<AbstractPlot> optPlot = region.getPlot(PlotType.FARM, plotId);
            if (optPlot.isPresent())
                return String.valueOf(optPlot.get().isSetting(AutoStoreSetting.class));
        }

        if (params.startsWith("plot_animal_setting_autostore_")) {
            int plotId = Integer.parseInt(params.substring(30));
            Optional<AbstractPlot> optPlot = region.getPlot(PlotType.ANIMAL, plotId);
            if (optPlot.isPresent())
                return String.valueOf(optPlot.get().isSetting(AutoStoreSetting.class));
        }

        if (params.startsWith("plot_farm_setting_colored_status_autostore_")) {
            int plotId = Integer.parseInt(params.substring(43));
            Optional<AbstractPlot> optPlot = region.getPlot(PlotType.FARM, plotId);
            if (optPlot.isPresent()) {
                return optPlot.get().isSetting(AutoStoreSetting.class) ? "§aON" : "§cOFF";
            }
        }

        if (params.startsWith("plot_animal_setting_colored_status_autostore_")) {
            int plotId = Integer.parseInt(params.substring(45));
            Optional<AbstractPlot> optPlot = region.getPlot(PlotType.ANIMAL, plotId);
            if (optPlot.isPresent()) {
                return optPlot.get().isSetting(AutoStoreSetting.class) ? "§aON" : "§cOFF";
            }
        }

        if (params.startsWith("plot_silo_capacity_")) {
            int plotId = Integer.parseInt(params.substring(19));
            Optional<AbstractPlot> optPlot = region.getPlot(PlotType.SILO, plotId);
            if (optPlot.isPresent())
                return String.valueOf(((SiloPlot) optPlot.get()).getCapacity());
        }

        if (params.startsWith("plot_barn_capacity_")) {
            int plotId = Integer.parseInt(params.substring(19));
            Optional<AbstractPlot> optPlot = region.getPlot(PlotType.BARN, plotId);
            if (optPlot.isPresent())
                return String.valueOf(((BarnPlot) optPlot.get()).getCapacity());
        }

        if (params.startsWith("plot_silo_filled_")) {
            int plotId = Integer.parseInt(params.substring(17));
            Optional<AbstractPlot> optPlot = region.getPlot(PlotType.SILO, plotId);
            if (optPlot.isPresent())
                return String.valueOf(((SiloPlot) optPlot.get()).getFilledAmount());
        }

        if (params.startsWith("plot_barn_filled_")) {
            int plotId = Integer.parseInt(params.substring(17));
            Optional<AbstractPlot> optPlot = region.getPlot(PlotType.BARN, plotId);
            if (optPlot.isPresent())
                return String.valueOf(((BarnPlot) optPlot.get()).getFilledAmount());
        }

        if (params.startsWith("plot_silo_percentage_filled_")) {
            int plotId = Integer.parseInt(params.substring(28));
            Optional<AbstractPlot> optPlot = region.getPlot(PlotType.SILO, plotId);
            if (optPlot.isPresent())
                return String.valueOf(((SiloPlot) optPlot.get()).getFilledPercentageRounded(2));
        }

        if (params.startsWith("plot_barn_percentage_filled_")) {
            int plotId = Integer.parseInt(params.substring(28));
            Optional<AbstractPlot> optPlot = region.getPlot(PlotType.BARN, plotId);
            if (optPlot.isPresent())
                return String.valueOf(((BarnPlot) optPlot.get()).getFilledPercentageRounded(2));
        }

        if (params.startsWith("plot_silo_setting_autosell_")) {
            int plotId = Integer.parseInt(params.substring(27));
            Optional<AbstractPlot> optPlot = region.getPlot(PlotType.SILO, plotId);
            if (optPlot.isPresent())
                return String.valueOf((optPlot.get()).isSetting(AutoSellSetting.class));
        }

        if (params.startsWith("plot_silo_setting_colored_status_autosell_")) {
            int plotId = Integer.parseInt(params.substring(42));
            Optional<AbstractPlot> optPlot = region.getPlot(PlotType.SILO, plotId);
            if (optPlot.isPresent())
                return optPlot.get().isSetting(AutoSellSetting.class) ? "§aON" : "§cOFF";
        }

        if (params.startsWith("plot_barn_setting_autosell_")) {
            int plotId = Integer.parseInt(params.substring(27));
            Optional<AbstractPlot> optPlot = region.getPlot(PlotType.BARN, plotId);
            if (optPlot.isPresent())
                return String.valueOf(optPlot.get().isSetting(AutoSellSetting.class));
        }

        if (params.startsWith("plot_barn_setting_colored_status_autosell_")) {
            int plotId = Integer.parseInt(params.substring(42));
            Optional<AbstractPlot> optPlot = region.getPlot(PlotType.BARN, plotId);
            if (optPlot.isPresent())
                return optPlot.get().isSetting(AutoSellSetting.class) ? "§aON" : "§cOFF";
        }

        return null;
    }
}
