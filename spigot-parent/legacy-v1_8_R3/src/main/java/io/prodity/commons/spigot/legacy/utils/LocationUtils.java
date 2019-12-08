package io.prodity.commons.spigot.legacy.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@UtilityClass
public class LocationUtils {

    public static String toString(Location location, boolean yawpitch) {
        String a = ",";
        return location.getWorld().getName() + a + location.getX() + a + location.getY() + a + location.getZ() + (yawpitch ? a + location
            .getYaw() + a + location.getPitch() : "");
    }

    public static Location fromString(String path, boolean yawpitch) {
        String[] split = path.split(",");
        World world = Bukkit.getWorld(split[0]);
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);

        if (!yawpitch) {
            return new Location(world, x, y, z);
        } else {
            float yaw = Float.parseFloat(split[4]);
            float pitch = Float.parseFloat(split[5]);
            return new Location(world, x, y, z, yaw, pitch);
        }

    }

}
