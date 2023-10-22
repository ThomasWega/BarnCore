package com.bof.barn.core.player;

import com.bof.barn.core.Core;
import com.bof.barn.core.region.BarnRegion;
import lombok.Data;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Handles the data and other info about the player
 */
@Data
public class GamePlayer {
    private static final Map<Player, GamePlayer> cachedPlayers = new HashMap<>();
    private final Core plugin;
    private final Player bukkitPlayer;

    public GamePlayer(@NotNull Core plugin, @NotNull Player bukkitPlayer) {
        this.plugin = plugin;
        this.bukkitPlayer = bukkitPlayer;

        if (!cachedPlayers.containsKey(bukkitPlayer)) {
            cachedPlayers.put(bukkitPlayer, this);
        }
    }

    /**
     * Cache the player with his data
     *
     * @param plugin Plugin instance
     * @param player Bukkit Player to cache
     */
    public static void cache(@NotNull Core plugin, @NotNull Player player) {
        cachedPlayers.put(player, new GamePlayer(plugin, player));
    }

    /**
     * Get the cached player instance
     *
     * @param player Bukkit Player to get for
     * @return Instance for the player or null
     */
    public static GamePlayer get(@NotNull Player player) {
        return cachedPlayers.get(player);
    }

    /**
     * Remove the player from the {@link #cachedPlayers}
     *
     * @param player Bukkit Player to uncache
     */
    public static void unCache(@NotNull Player player) {
        cachedPlayers.remove(player);
    }

    /**
     * @return Region the player is owner or member of
     */
    public @NotNull Optional<BarnRegion> getBarnRegion() {
        return this.plugin.getRegionManager().getRegionOf(bukkitPlayer);
    }
}
