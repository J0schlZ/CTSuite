package de.crafttogether.ctsuite.bungee.handlers;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;

import de.crafttogether.ctsuite.bungee.CTSuite;
import de.crafttogether.ctsuite.messaging.bungee.Packet;
import de.crafttogether.ctsuite.messaging.ReceivedPacket;
import de.crafttogether.ctsuite.messaging.MessagingService;
import de.crafttogether.ctsuite.messaging.MessagingService.Callback;
import de.crafttogether.ctsuite.util.CTServer;

public class ServerHandler {
	private CTSuite plugin;
	private MessagingService messaging;
	
	private ConcurrentHashMap<String, CTServer> serverMap;
	
	public ServerHandler() {
		plugin	= CTSuite.getInstance();
		messaging = plugin.getMessagingService();
		
		serverMap = new ConcurrentHashMap<String, CTServer>();
		
		messaging.on("server-register", new Callback() {
			@Override
			public void run(ReceivedPacket received) {
				String serverName = received.getValues().getString("serverName");
				if (!serverMap.containsKey(serverName))	serverMap.put(serverName, new CTServer(serverName));

				plugin.getLogger().info("Server '" + serverName + "' connected");
				
				// Send server-list to connected server
				Packet packet = new Packet("server-list");
				packet.put("serverList", new JSONArray(serverMap.keySet()));
				packet.sendTo(serverName);
			}
		});
		
		messaging.on("server-disconnect", new Callback() {
			@Override
			public void run(ReceivedPacket received) {
				String serverName = received.getValues().getString("serverName");
				if (serverMap.containsKey(serverName)) serverMap.remove(serverName);
				
				plugin.getLogger().info("Server '" + serverName + "' disconnected");
			}
		});
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
