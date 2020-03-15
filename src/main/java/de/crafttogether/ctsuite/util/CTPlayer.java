package de.crafttogether.ctsuite.util;

import java.util.UUID;

public abstract class CTPlayer {
	UUID uuid;
	private String name;
	private String nickName;
	private String displayName;
	
	private CTServer server;
	private CTWorld world;

	public CTPlayer(UUID uuid, String playerName, CTServer server, CTWorld world) {
		this.uuid = uuid;
		this.name = playerName;
		this.nickName = playerName;
		this.displayName = playerName;
		this.server = server;
		this.world = world;
	}
	
	public UUID getUUID() {
		return uuid;
	}

	public String getName() {
		return name;
	}
	
	public String nickName() {
		return nickName;
	}
	
	public String displayName() {
		return displayName;
	}
	
	public CTServer getServer() {
		return server;
	}

	public CTWorld getWorld() {
		return world;
	}
	
	public abstract void teleport(CTLocation loc);
}
