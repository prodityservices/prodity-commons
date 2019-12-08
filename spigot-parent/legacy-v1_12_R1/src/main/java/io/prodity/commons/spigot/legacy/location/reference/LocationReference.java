package io.prodity.commons.spigot.legacy.location.reference;

import io.prodity.commons.spigot.legacy.chunk.reference.ChunkReference;
import io.prodity.commons.spigot.legacy.world.reference.WorldReference;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public interface LocationReference extends ChunkReference {

    Location getBukkitLocation();

    @Override
    WorldReference<?> getWorldReference();

    default Block getBukkitBlock() {
        return this.getBukkitLocation().getBlock();
    }

    default Vector getBukkitLocatonAsVector() {
        return this.getBukkitLocation().toVector();
    }

}