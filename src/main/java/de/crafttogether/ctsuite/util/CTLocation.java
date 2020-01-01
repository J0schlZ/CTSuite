package de.crafttogether.ctsuite.util;

import org.bukkit.Location;
import org.bukkit.World;

import de.crafttogether.ctsuite.bukkit.CTSuite;

public class CTLocation {
	private double x = 0;
	private double y = 0;
	private double z = 0;
	private float pitch = 0;
	private float yaw = 0;
	private String server = null;
	private String world = null;

	public CTLocation(String server, String world, double x, double y, double z) {
		this.server = server;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public CTLocation(String server, String world, double x, double y, double z, float pitch, float yaw) {
		this.server = server;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	
	public String getServer() {
		return server;
	}
	
	public String getWorld() {
		return world;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public Location toBukkitLocation() {
		return CTLocation.toBukkitLocation(this);
	}
	
	public static CTLocation fromBukkitLocation(Location loc, String serverName) {
		return new CTLocation(serverName, loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
	}
	
	public static Location toBukkitLocation(CTLocation loc) {
		World world = CTSuite.getInstance().getServer().getWorld(loc.getWorld());
		
		if (world != null)
			new Location(world, loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
		
		//TODO: UnkownWorldExcpetion?
		
		return null;
	}
	
}
