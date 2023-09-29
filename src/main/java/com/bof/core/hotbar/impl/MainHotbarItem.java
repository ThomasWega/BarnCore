package com.bof.core.hotbar.impl;

import com.bof.core.Core;
import com.bof.core.hotbar.HotbarItem;
import com.bof.core.item.SkullBuilder;
import com.bof.core.player.GamePlayer;
import com.bof.core.region.menu.RegionMainMenu;
import com.bof.core.skin.Skin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainHotbarItem extends HotbarItem {
    public MainHotbarItem(@NotNull Core core) {
        super(core, new SkullBuilder()
                        .displayName(Component.text("Barn Menu", NamedTextColor.RED))
                        .lore(List.of(
                                Component.empty(),
                                Component.text("Click to open", NamedTextColor.DARK_GRAY)
                        ))
                        .skin(new Skin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGE5OTUzY2U5NDVkYTZlMmZiODAwMzBlNzU0ZmE4ZTA5MmM0ZTllY2QwNTQ4ZjkzOGMzNDk3YTgwOGU0MWE0OCJ9fX0=", null))
                        .hideFlags()
                        .build(),
                event -> {
                    Player player = event.getPlayer();
                    GamePlayer gamePlayer = GamePlayer.get(player);
                    gamePlayer.getBarnRegion().ifPresent(region -> new RegionMainMenu(region).show(player));
                }
        );
    }
}
