package io.prodity.commons.spigot.model;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationReference {

	public static LocationReference of(Location location) {
		return new LocationReference(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	private final String world;
	private final double x;
	private final double y;
	private final double z;

	public LocationReference(String world, double x, double y, double z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
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
		return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z);
	}

}
