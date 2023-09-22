package com.bof.core.player;

import com.bof.core.Core;
import com.bof.core.region.BarnRegion;
import lombok.Data;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
public class GamePlayer {
    private static final Map<Player, GamePlayer> cachedPlayers = new HashMap<>();
    private final Core plugin;
    private final Player bukkitPlayer;
    private final Optional<BarnRegion> barnRegion;

    public GamePlayer(@NotNull Core plugin, @NotNull Player bukkitPlayer) {
        this.plugin = plugin;
        this.bukkitPlayer = bukkitPlayer;
        this.barnRegion = plugin.getRegionManager().getRegionOf(bukkitPlayer);

        if (!cachedPlayers.containsKey(bukkitPlayer)) {
            cachedPlayers.put(bukkitPlayer, this);
        }

    }

    public static void cache(@NotNull Core plugin, @NotNull Player player) {
        cachedPlayers.put(player, new GamePlayer(plugin, player));
    }

    public static GamePlayer get(@NotNull Player player) {
        return cachedPlayers.get(player);
    }

    public static void unCache(@NotNull Player player) {
        cachedPlayers.remove(player);
    }
}
