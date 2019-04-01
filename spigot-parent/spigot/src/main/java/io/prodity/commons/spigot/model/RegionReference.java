package io.prodity.commons.spigot.model;

import java.util.Objects;
import java.util.StringJoiner;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class RegionReference {

    public static RegionReference of(Location min, Location max) {
        if (!min.getWorld().getName().equals(max.getWorld().getName())) {
            throw new IllegalArgumentException("Worlds of min and max are not the same!");
        }
        return new RegionReference(min.getWorld().getName(), min.getBlockX(), max.getBlockX(),
                min.getBlockY(), max.getBlockY(), min.getBlockZ(), max.getBlockZ());
    }

    private final String world;
    private final int minX;
    private final int maxX;
    private final int minY;
    private final int maxY;
    private final int minZ;
    private final int maxZ;

    public RegionReference(String world, int minX, int maxX, int minY,
            int maxY, int minZ, int maxZ) {
        this.world = world;
        this.maxX = maxX;
        this.minX = minX;
        this.minY = minY;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.minZ = minZ;
    }

    public String getWorld() {
        return this.world;
    }

    public int getMinX() {
        return this.minX;
    }

    public int getMaxX() {
        return this.maxX;
    }

    public int getMinY() {
        return this.minY;
    }

    public int getMaxY() {
        return this.maxY;
    }

    public int getMinZ() {
        return this.minZ;
    }

    public int getMaxZ() {
        return this.maxZ;
    }

    public BlockReference getMin() {
        return new BlockReference(this.world, this.minX, this.minY, this.minZ);
    }

    public BlockReference getMax() {
        return new BlockReference(this.world, this.maxX, this.maxY, this.maxZ);
    }

    public Location dereferenceMin() {
        return new Location(Bukkit.getWorld(this.world), this.minX, this.minY, this.minZ);
    }

    public Location dereferenceMax() {
        return new Location(Bukkit.getWorld(this.world), this.maxX, this.maxY, this.maxZ);
    }

    public boolean containsXYZ(int x, int y, int z) {
        return x >= this.minX && x <= this.maxX && z >= this.minZ && z <= this.maxZ
                && y >= this.minY && y <= this.maxY;
    }

    public boolean containsXZ(int x, int z) {
        return x >= this.minX && x <= this.maxX && z >= this.minZ && z <= this.maxZ;
    }

    public boolean intersects(RegionReference that) {
        return that.minX <= this.maxX && that.maxX >= this.minX && that.minY <= this.maxY
                && that.maxY >= this.minY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final RegionReference that = (RegionReference) o;
        return this.minX == that.minX &&
                this.maxX == that.maxX &&
                this.minY == that.minY &&
                this.maxY == that.maxY &&
                this.minZ == that.minZ &&
                this.maxZ == that.maxZ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.minX, this.maxX, this.minY, this.maxY, this.minZ, this.maxZ);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RegionReference.class.getSimpleName() + "[", "]")
                .add("X: " + this.minX + "->" + this.maxX)
                .add("Y: " + this.minY + "->" + this.maxY)
                .add("Z: " + this.minZ + "->" + this.maxZ)
                .toString();
    }

}
