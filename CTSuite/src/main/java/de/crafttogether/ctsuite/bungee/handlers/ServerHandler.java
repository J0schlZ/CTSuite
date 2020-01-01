package de.crafttogether.ctsuite.bungee.handlers;

import java.util.Collection;
import java.util.HashMap;

import de.crafttogether.ctsuite.bungee.CTSuite;
import de.crafttogether.ctsuite.util.CTServer;

public class ServerHandler {
	private CTSuite plugin;
	
	private HashMap<String, CTServer> serverMap;
	
	public ServerHandler() {
		plugin = CTSuite.getInstance();
	}
	
	public Collection<CTServer> getServer() {
		return serverMap.values();
	}
	
	public CTServer getServer(String name) {
		if (serverMap.containsKey(name))
			return serverMap.get(name);
		return null;
	}
}
