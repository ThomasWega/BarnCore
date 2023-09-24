package com.bof.core.utils;

import com.bof.core.region.plot.Plot;
import com.bof.toolkit.utils.ColorUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.block.sign.Side;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PlotUtils {

    public static Optional<Location> identifyHologram(@NotNull Plot plot) {
        return BoxUtils.getBlocksInBox(plot.getBox(), "SIGN").stream()
                .map(block -> ((Sign) block.getState()))
                .filter(sign -> ColorUtils.stripColor(sign.getSide(Side.FRONT).line(0)).equals("holo"))
                .map(sign -> sign.getBlock().getLocation())
                .findFirst();
    }

    public static void handleSignsRemoval(@NotNull Plot plot) {
        BoxUtils.getBlocksInBox(plot.getBox(), "SIGN").forEach(block -> {
            if (block.getState() instanceof Sign sign) {
                sign.getBlock().getRelative(BlockFace.DOWN).setType(Material.FARMLAND);
                Block under = block.getRelative(BlockFace.DOWN);
                Farmland farmland = ((Farmland) block.getRelative(BlockFace.DOWN).getBlockData());
                farmland.setMoisture(farmland.getMaximumMoisture());
                under.setBlockData(farmland);
            }
        });
    }
}
