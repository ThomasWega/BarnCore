package com.bof.barn.core.utils;

import com.bof.barn.core.region.plot.AbstractPlot;
import com.bof.barn.core.region.plot.PlotType;
import com.bof.toolkit.utils.ColorUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.block.sign.Side;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static com.bof.barn.core.Core.WORLD;

/**
 * Various utilities for {@link BoundingBox} combined with {@link AbstractPlot}
 */
public class BoxUtils {

    private BoxUtils() {
    }

    /**
     * Checks for 2 signs with the same id in the given {@link BoundingBox} with the same {@link PlotType#getIdentifier()}.
     * Creates a new {@link BoundingBox} with the 2 signs location and removes the signs
     *
     * @param type Type of plot to try to identify
     * @param box  Box to check in
     * @return Map with plot id and box
     */
    public static Map<String, BoundingBox> identifyPlots(@NotNull PlotType type, @NotNull BoundingBox box) {
        // get the signs of the specified PlotType only
        Set<Sign> signsInBox = getBlocksInBox(box, "SIGN").stream()
                .map(block -> ((Sign) block.getState()))
                .filter(sign -> ColorUtils.stripColor(sign.getSide(Side.FRONT).line(0)).equals(type.getIdentifier()))
                .collect(Collectors.toSet());

        final Map<String, MutablePair<Block, Block>> plotLocations = new HashMap<>();

        // get the ids
        signsInBox.forEach(sign -> {
            String id = ColorUtils.stripColor(sign.getSide(Side.FRONT).line(1));
            MutablePair<Block, Block> pair = plotLocations.getOrDefault(id, new MutablePair<>(null, null));
            if (pair.getLeft() == null) {
                pair.setLeft(sign.getBlock());
            } else if (pair.getRight() == null) {
                pair.setRight(sign.getBlock());
            }
            plotLocations.put(id, pair);
        });

        removeSignsForPlot(type, signsInBox);

        return plotLocations.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            MutablePair<Block, Block> pair = entry.getValue();
                            return BoundingBox.of(pair.getLeft(), pair.getRight());
                        }
                ));
    }

    /**
     * @param box Box to get the Center block for
     * @return Center block of the given box
     */
    public static Block getCenterBlock(@NotNull BoundingBox box) {
        Vector center = box.getCenter();
        return WORLD.getBlockAt(center.getBlockX(), center.getBlockY(), center.getBlockZ());
    }

    /**
     * @param box Box to get the Center location for
     * @return Center location of the given box
     */
    public static Location getCenterLocation(@NotNull BoundingBox box) {
        return getCenterBlock(box).getLocation();
    }

    /**
     * Removes the given signs. If type is {@link PlotType#FARM}, it sets the block below to max moisturised {@link Material#FARMLAND}
     *
     * @param type  Type of the plot
     * @param signs Signs to remove
     */
    private static void removeSignsForPlot(PlotType type, Set<Sign> signs) {
        signs.forEach(sign -> {
            Block block = sign.getBlock();
            block.setType(Material.AIR);

            if (type == PlotType.FARM) {
                Block under = block.getRelative(BlockFace.DOWN);
                under.setType(Material.FARMLAND);
                Farmland farmland = ((Farmland) under.getBlockData());
                farmland.setMoisture(farmland.getMaximumMoisture());
                under.setBlockData(farmland);
            }
        });
    }

    /**
     * Identify sign with spawn tag
     *
     * @param box Box to check in
     * @return Optional with location of the sign, or empty if no sign was found
     */
    public static Optional<Location> identifySpawn(@NotNull BoundingBox box) {
        return getBlocksInBox(box, "SIGN").stream()
                .map(block -> ((Sign) block.getState()))
                .filter(sign -> ColorUtils.stripColor(sign.getSide(Side.FRONT).line(0)).equals("spawn"))
                .map(BlockState::getLocation)
                .findFirst();
    }

    /**
     * Get all blocks that are present in the {@link BoundingBox}
     *
     * @param box        Box to check in
     * @param includeAir Whether to include blocks with type {@link Material#AIR}
     * @return Set of blocks present in the box
     */
    public static Set<Block> getBlocksInBox(@NotNull BoundingBox box, boolean includeAir) {
        Set<Block> blocks = new HashSet<>();

        Vector min = box.getMin();
        Vector max = box.getMax();

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    Block block = WORLD.getBlockAt(x, y, z);
                    if (!includeAir && block.getType() == Material.AIR) continue;

                    blocks.add(block);
                }
            }
        }
        return blocks;
    }

    /**
     * Get all blocks that are present in the {@link BoundingBox} with the specific {@link Material}
     *
     * @param box      Box to check in
     * @param material Only blocks with this material will be returned
     * @return Set of blocks present in the box
     */
    public static Set<Block> getBlocksInBox(@NotNull BoundingBox box, @NotNull Material material) {
        return getBlocksInBox(box, false).stream()
                .filter(block -> block.getType() == material)
                .collect(Collectors.toSet());
    }

    /**
     * Get all blocks that are present in the {@link BoundingBox} with type {@link Material#name()} containing the given {@link String}
     *
     * @param box               Box to check in
     * @param matContainsString Only blocks which contain this String in Material type name will be returned
     * @return Set of blocks present in the box
     */
    public static Set<Block> getBlocksInBox(@NotNull BoundingBox box, @NotNull String matContainsString) {
        return getBlocksInBox(box, false).stream()
                .filter(block -> block.getType().name().contains(matContainsString))
                .collect(Collectors.toSet());
    }

    /**
     * Get a random location inside the given BoundingBox.
     *
     * @param box The BoundingBox to generate a random location inside.
     * @return A random Location within the specified BoundingBox.
     */
    public static Location getRandomLocation(@NotNull BoundingBox box) {
        Vector min = box.getMin();
        Vector max = box.getMax();

        double randomX = min.getX() + Math.random() * (max.getX() - min.getX());
        double randomY = min.getY() + Math.random() * (max.getY() - min.getY());
        double randomZ = min.getZ() + Math.random() * (max.getZ() - min.getZ());

        return new Location(Bukkit.getWorld("world"), randomX, randomY, randomZ);
    }
}
