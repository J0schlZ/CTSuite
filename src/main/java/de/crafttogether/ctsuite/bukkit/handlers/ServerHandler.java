package de.crafttogether.ctsuite.bukkit.handlers;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import de.crafttogether.ctsuite.bukkit.CTSuite;
import de.crafttogether.ctsuite.messaging.MessagingService;
import de.crafttogether.ctsuite.messaging.MessagingService.Callback;
import de.crafttogether.ctsuite.messaging.ReceivedPacket;
import de.crafttogether.ctsuite.util.CTServer;

public class ServerHandler {
	private CTSuite plugin;
	private MessagingService messaging;
	
	private ConcurrentHashMap<String, CTServer> serverMap;
	
	public ServerHandler() {
		plugin	= CTSuite.getInstance();
		messaging = plugin.getMessagingService();
		
		serverMap = new ConcurrentHashMap<String, CTServer>();
		
		// Add self to serverMap
		String serverName = messaging.getAdapter().getClientName();
		serverMap.put(serverName, new CTServer(serverName));
		
		// Register depending Packets
		addPacketListeners();
	}
	
	private void addPacketListeners() {
		messaging.on("server-register", new Callback() {
			@Override
			public void run(ReceivedPacket received) {
				String serverName = received.getValues().getString("serverName");
				if (!serverMap.containsKey(serverName))	serverMap.put(serverName, new CTServer(serverName));
			}
		});
		
		messaging.on("server-disconnect", new Callback() {
			@Override
			public void run(ReceivedPacket received) {
				String serverName = received.getValues().getString("serverName");
				if (serverMap.containsKey(serverName)) serverMap.remove(serverName);
			}
		});
		
		messaging.on("socket-disconnect", new Callback() {
			@Override
			public void run(ReceivedPacket received) {
				String serverName = plugin.getMessagingService().getAdapter().getClientName();
				
				for (CTServer server : serverMap.values()) {
					if (!server.getName().equalsIgnoreCase(serverName))
						serverMap.remove(server.getName());
				}
			}
		});
		
		messaging.on("server-list", new Callback() {
			@Override
			public void run(ReceivedPacket received) {
				List<Object> serverList = received.getValues().getJSONArray("serverList").toList();
				
				// Add new entries
				for (Object name : serverList) {
					String serverName = (String) name;
					if (!serverMap.containsKey(serverName)) serverMap.put(serverName, new CTServer(serverName));
				}
				
				if (received.getSender().equalsIgnoreCase("#proxy")) {
					// Remove old entries
					for (String serverName : serverMap.keySet()) {
						if (!serverList.contains(serverName))
							serverMap.remove(serverName);
					}
				}
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
