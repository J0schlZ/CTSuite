package de.crafttogether.ctsuite.bukkit.handlers;

import java.util.UUID;

import de.crafttogether.ctsuite.util.CTLocation;
import de.crafttogether.ctsuite.util.CTPlayer;
import de.crafttogether.ctsuite.util.CTServer;
import de.crafttogether.ctsuite.util.CTWorld;

public class CTBukkitPlayer extends CTPlayer {
private CTWorld world;
	
	public CTBukkitPlayer(UUID uuid, String playerName, CTServer server, CTWorld world) {
		super(uuid, playerName, server, world);
		// TODO Auto-generated constructor stub
	}

	public void setWorld(CTWorld world) {
		
	}
	
	@Override
	public void teleport(CTLocation loc) {
		// TODO Auto-generated method stub	
	}
}
