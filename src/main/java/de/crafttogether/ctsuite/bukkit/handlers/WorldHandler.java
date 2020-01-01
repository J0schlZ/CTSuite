package de.crafttogether.ctsuite.bukkit.handlers;

import java.util.Collection;
import java.util.HashMap;

import de.crafttogether.ctsuite.bukkit.CTSuite;
import de.crafttogether.ctsuite.util.CTWorld;

public class WorldHandler {
	private CTSuite plugin;
	
	private HashMap<String, CTWorld> worldMap;
	
	public WorldHandler() {
		plugin = CTSuite.getInstance();
		
		//TODO: Get worlds from database
		//TODO: Process and provide packets to register/notify loaded/unloaded worlds
	}
	
	public Collection<CTWorld> getWorlds() {
		return worldMap.values();
	}
	
	public CTWorld getWorld(String name) {
		if (worldMap.containsKey(name))
			return worldMap.get(name);
		return null;
	}
}