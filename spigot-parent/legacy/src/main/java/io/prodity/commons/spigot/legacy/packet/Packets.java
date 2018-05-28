package io.prodity.commons.spigot.legacy.packet;

import io.prodity.commons.spigot.legacy.lazy.LazyValue;
import io.prodity.commons.spigot.legacy.lazy.SimpleLazyValue;
import io.prodity.commons.spigot.legacy.version.Version;

public enum Packets {

    ;

    private static final LazyValue<PacketSenderProvider> provider = new SimpleLazyValue<>(
        () -> Version.getUtilitiesProvider(PacketSenderProvider.class, "packet.PacketSender"));

    public static PacketSenderProvider getProvider() {
        return Packets.provider.get();
    }

}
