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
	private CTServer server = null;
	private CTWorld world = null;

	public CTLocation(CTServer server, CTWorld world, double x, double y, double z) {
		this.server = server;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public CTLocation(CTServer server, CTWorld world, double x, double y, double z, float pitch, float yaw) {
		this.server = server;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	
	public CTServer getServer() {
		return server;
	}
	
	public CTWorld getWorld() {
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
	
	public void setServer(CTServer server) {
		this.server = server;
	}
	
	public void setWorld(CTWorld world) {
		this.world = world;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void setZ(double z) {
		this.z = z;
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	
	public Location toBukkitLocation() {
		return CTLocation.toBukkitLocation(this);
	}
	
	public static CTLocation fromBukkitLocation(Location loc, CTServer server) {
		return new CTLocation(server, CTWorld.fromBukkitWorld(loc.getWorld(), server), loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
	}
	
	public static Location toBukkitLocation(CTLocation loc) {
		World world = CTSuite.getInstance().getServer().getWorld(loc.getWorld().getName());
		
		if (world != null)
			new Location(world, loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
		
		//TODO: UnkownWorldExcpetion?
		
		return null;
	}
	
}
