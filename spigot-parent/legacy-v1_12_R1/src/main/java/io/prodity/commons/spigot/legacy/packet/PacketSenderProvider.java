package io.prodity.commons.spigot.legacy.packet;

import io.prodity.commons.spigot.legacy.version.VersionProvider;
import java.util.Collection;
import org.bukkit.entity.Player;

public interface PacketSenderProvider extends VersionProvider {

    void sendPacket(Player player, Object packet) throws IllegalArgumentException;

    void sendPacketArray(Player player, Object... packets) throws IllegalArgumentException;

    void sendPacketCollection(Player player, Collection<Object> packets) throws IllegalArgumentException;

}
