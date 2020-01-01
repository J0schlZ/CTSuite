package de.crafttogether.ctsuite.bukkit.handlers;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import de.crafttogether.ctsuite.bukkit.CTSuite;
import de.crafttogether.ctsuite.util.CTPlayer;

public class PlayerHandler {
	private CTSuite plugin;
	
	private HashMap<UUID, CTPlayer> playerMap;
	
	public PlayerHandler() {
		plugin = CTSuite.getInstance();
		
		//TODO: Get players from database
		//TODO: Process and provide packets to register/notify joined/leaved players
	}
	
	public Collection<CTPlayer> getPlayers() {
		return playerMap.values();
	}
	
	public CTPlayer getPlayer(UUID uuid) {
		if (playerMap.containsKey(uuid))
			return playerMap.get(uuid);
		return null;
	}
}
