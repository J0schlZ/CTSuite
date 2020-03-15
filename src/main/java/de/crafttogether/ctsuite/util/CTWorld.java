package de.crafttogether.ctsuite.util;

import org.bukkit.World;

public class CTWorld {
	private String worldName;
	private CTServer server;
	
	public CTWorld(String worldName, CTServer server) {
		this.worldName = worldName;
		this.server = server;
	}
	
	public String getName() {
		return worldName;
	}
	
	public CTServer getServer() {
		return server;
	}
}

