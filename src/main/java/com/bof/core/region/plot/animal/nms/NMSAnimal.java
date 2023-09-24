package com.bof.core.region.plot.animal.nms;

import com.bof.core.Core;
import com.bof.core.region.plot.animal.AnimalType;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class NMSAnimal {
    private final Location location;
    private final AnimalType type;

    public NMSAnimal(@NotNull Location location, @NotNull AnimalType type) {
        this.location = location;
        this.type = type;
        this.spawnEntity();
    }

    private void spawnEntity() {
        int entityId = 10000;

        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);

        packet.getIntegers().write(0, entityId);
        packet.getUUIDs().write(0, UUID.randomUUID());
        packet.getIntegers().write(1, this.type.getEntityTypeID());

        packet.getDoubles()
                .write(0, this.location.getX())
                .write(1, this.location.getY())
                .write(2, this.location.getZ());

        // Pitch and yaw, remove this code section if you want to default to 0
        packet.getBytes()
                .write(4, (byte) (this.location.getYaw() * 256.0F / 360.0F)) // yaw
                .write(5, (byte) (this.location.getPitch() * 256.0F / 360.0F)) // pitch
                .write(5, (byte) (this.location.getYaw() * 256.0F / 360.0F)); // head yaw

        // Send the packet to all players
        try {
            for(Player player : Bukkit.getOnlinePlayers()) {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            }
        } catch (InvocationTargetException e) {
            Core.LOGGER.error("Failed to spawn NMS Entity of type " + type.name(), e);
        }
    }
}
