package io.prodity.commons.spigot.model;

import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class BlockReference {

    public static BlockReference of(Block block) {
        return BlockReference.of(block.getLocation());
    }

    public static BlockReference of(Location location) {
        return new BlockReference(location.getWorld().getName(), location.getBlockX(),
                location.getBlockY(), location.getBlockZ());
    }

    private final String world;
    private final int x;
    private final int y;
    private final int z;

    public BlockReference(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getWorld() {
        return this.world;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public Block dereference() {
        return this.dereferenceLocation().getBlock();
    }

    public Location dereferenceLocation() {
        return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BlockReference that = (BlockReference) o;
        return this.x == that.x &&
                this.y == that.y &&
                this.z == that.z &&
                Objects.equals(this.world, that.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.world, this.x, this.y, this.z);
    }

}
