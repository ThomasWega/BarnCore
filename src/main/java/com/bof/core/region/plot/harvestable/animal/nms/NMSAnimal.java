package com.bof.core.region.plot.harvestable.animal.nms;

import com.bof.core.Core;
import com.bof.core.region.plot.harvestable.animal.AnimalPlot;
import com.bof.core.region.plot.harvestable.animal.AnimalType;
import com.bof.core.utils.BoxUtils;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.UUID;

public class NMSAnimal {

    private NMSAnimal() {
    }

    public static int spawnNew(@NotNull AnimalPlot plot, @NotNull AnimalType type) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);

        int entityId = Entity.nextEntityId();
        packet.getUUIDs().write(0, UUID.randomUUID());
        packet.getIntegers().write(0, entityId);

        packet.getEntityTypeModifier().write(0, type.getEntityType());

        Location location = BoxUtils.getRandomLocation(plot.getBox());
        packet.getDoubles()
                .write(0, location.getX())
                .write(1, location.getY())
                .write(2, location.getZ());

        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            }
        } catch (InvocationTargetException e) {
            Core.LOGGER.error("Failed to spawn NMS Entity of type " + type.name(), e);
        }

        return entityId;
    }

    public static void remove(int... entityIds) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        packet.getModifier().write(0, new IntArrayList(entityIds));
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            }
        } catch (InvocationTargetException e) {
            Core.LOGGER.error("Failed to remove NMS Entities (" + Arrays.toString(entityIds) + ")", e);
        }
    }
}
