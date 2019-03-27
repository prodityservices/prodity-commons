package io.prodity.commons.spigot.model;

import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationReference {

    public static LocationReference of(Location location) {
        return new LocationReference(location.getWorld().getName(), location.getX(),
                location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    private final String world;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public LocationReference(String world, double x, double y, double z) {
        this(world, x, y, z, 0, 0);
    }

    public LocationReference(String world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public String getWorld() {
        return this.world;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public Location dereference() {
        return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z, this.yaw,
                this.pitch);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final LocationReference that = (LocationReference) o;
        return Double.compare(that.x, this.x) == 0 &&
                Double.compare(that.y, this.y) == 0 &&
                Double.compare(that.z, this.z) == 0 &&
                Float.compare(that.yaw, this.yaw) == 0 &&
                Float.compare(that.pitch, this.pitch) == 0 &&
                Objects.equals(this.world, that.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.world, this.x, this.y, this.z, this.yaw, this.pitch);
    }

}
