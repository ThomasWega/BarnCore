package com.bof.barn.core.utils;

import com.bof.barn.core.region.plot.Plot;
import com.bof.toolkit.utils.ColorUtils;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PlotUtils {

    private PlotUtils() {
    }

    /**
     * Gets the signs in the {@link org.bukkit.util.BoundingBox} and
     * checks for holo text
     *
     * @param plot Plot to check for signs in
     * @return Optional with location of the sign, or empty if no sign with holo is present
     */
    public static Optional<Location> identifyHologram(@NotNull Plot plot) {
        return BoxUtils.getBlocksInBox(plot.getBox(), "SIGN").stream()
                .map(block -> ((Sign) block.getState()))
                .filter(sign -> ColorUtils.stripColor(sign.getSide(Side.FRONT).line(0)).equals("holo"))
                .map(sign -> sign.getBlock().getLocation())
                .findFirst();
    }
}
