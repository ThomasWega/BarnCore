package com.bof.core.utils;

import com.bof.core.region.plot.Plot;
import com.bof.toolkit.utils.ColorUtils;
import org.bukkit.Location;
import org.bukkit.block.Sign;
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
}
