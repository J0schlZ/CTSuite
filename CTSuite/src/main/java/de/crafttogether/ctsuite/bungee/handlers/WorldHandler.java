package de.crafttogether.ctsuite.bungee.handlers;

import java.util.Collection;
import java.util.HashMap;

import de.crafttogether.ctsuite.bungee.CTSuite;
import de.crafttogether.ctsuite.util.CTWorld;

public class WorldHandler {
	private CTSuite plugin;
	
	private HashMap<String, CTWorld> worldMap;
	
	public WorldHandler() {
		plugin = CTSuite.getInstance();
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