package com.bof.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class BoxUtils {

    private BoxUtils() {
    }

    public static Set<Block> getBlocksInBox(@NotNull BoundingBox box) {
        World world = Bukkit.getWorld("world");
        assert world != null;
        Set<Block> blocks = new HashSet<>();

        // Iterate through the blocks in the bounding box
        for (int x = (int) box.getMinX(); x <= (int) box.getMaxX(); x++) {
            for (int y = (int) box.getMinY(); y <= (int) box.getMaxY(); y++) {
                for (int z = (int) box.getMinZ(); z <= (int) box.getMaxZ(); z++) {
                    blocks.add(world.getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }
}
