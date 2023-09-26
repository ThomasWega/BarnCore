package com.bof.core.utils;

import com.bof.core.region.plot.PlotType;
import com.bof.toolkit.utils.ColorUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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

public class BoxUtils {

    private BoxUtils() {
    }

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

    public static Block getCenterBlock(@NotNull BoundingBox box) {
        Vector center = box.getCenter();
        return Bukkit.getWorld("world").getBlockAt(center.getBlockX(), center.getBlockY(), center.getBlockZ());
    }

    public static Location getCenterLocation(@NotNull BoundingBox box) {
        return getCenterBlock(box).getLocation();
    }

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

    public static Optional<Location> identifySpawn(@NotNull BoundingBox box) {
        return getBlocksInBox(box, "SIGN").stream()
                .map(block -> ((Sign) block.getState()))
                .filter(sign -> ColorUtils.stripColor(sign.getSide(Side.FRONT).line(0)).equals("spawn"))
                .map(BlockState::getLocation)
                .findFirst();
    }

    public static Set<Block> getBlocksInBox(@NotNull BoundingBox box, boolean includeAir) {
        World world = Bukkit.getWorld("world");
        assert world != null;
        Set<Block> blocks = new HashSet<>();

        Vector min = box.getMin();
        Vector max = box.getMax();

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (!includeAir && block.getType() == Material.AIR) continue;

                    blocks.add(block);
                }
            }
        }
        return blocks;
    }

    public static Set<Block> getBlocksInBox(@NotNull BoundingBox box, @NotNull Material material) {
        return getBlocksInBox(box, false).stream()
                .filter(block -> block.getType() == material)
                .collect(Collectors.toSet());
    }

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
        World world = Bukkit.getWorld("world");
        assert world != null;

        Vector min = box.getMin();
        Vector max = box.getMax();

        double randomX = min.getX() + Math.random() * (max.getX() - min.getX());
        double randomY = min.getY() + Math.random() * (max.getY() - min.getY());
        double randomZ = min.getZ() + Math.random() * (max.getZ() - min.getZ());

        return new Location(world, randomX, randomY, randomZ);
    }
}
