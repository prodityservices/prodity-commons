package io.prodity.commons.spigot.model;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationReference {

	public static LocationReference of(Location location) {
		return new LocationReference(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	private final String world;
	private final int x;
	private final int y;
	private final int z;

	public LocationReference(String world, int x, int y, int z) {
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

	public Location dereference() {
		return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z);
	}

}
