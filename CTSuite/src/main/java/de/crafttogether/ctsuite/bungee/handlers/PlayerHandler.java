package de.crafttogether.ctsuite.bungee.handlers;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import de.crafttogether.ctsuite.bungee.CTSuite;
import de.crafttogether.ctsuite.util.CTPlayer;

public class PlayerHandler {
	private CTSuite plugin;
	
	private HashMap<UUID, CTPlayer> playerMap;
	
	public PlayerHandler() {
		plugin = CTSuite.getInstance();
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
