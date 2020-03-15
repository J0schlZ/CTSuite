package de.crafttogether.ctsuite.bungee.handlers;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import de.crafttogether.ctsuite.bungee.CTSuite;
import de.crafttogether.ctsuite.messaging.bungee.Packet;
import de.crafttogether.ctsuite.messaging.ReceivedPacket;
import de.crafttogether.ctsuite.messaging.MessagingService;
import de.crafttogether.ctsuite.messaging.MessagingService.Callback;
import de.crafttogether.ctsuite.util.CTServer;
import de.crafttogether.ctsuite.util.CTWorld;

public class WorldHandler {
	private CTSuite plugin;
	private MessagingService messaging;
	
	private ConcurrentHashMap<String, CTWorld> worldMap;
	
	public WorldHandler() {
		plugin = CTSuite.getInstance();
		messaging = CTSuite.getInstance().getMessagingService();
		
		worldMap = new ConcurrentHashMap<String, CTWorld>();
		
		// Register depending Packets
		addPacketListeners();
	}
	
	private void addPacketListeners() {
		messaging.on("server-register", new Callback() {
			@Override
			public void run(ReceivedPacket received) {				
				List<Object> worldlist = received.getValues().getJSONArray("worldList").toList();

				System.out.println(worldlist.toString());
				
				String serverName = received.getSender();
				CTServer server = plugin.getServerHandler().getServer(serverName);
				
				if (server == null) {
					plugin.getLogger().warning("Error: Server '" + serverName + "' not found.");
					return;
				}
				
				// Send bungee's world-list (with server-names) to connected server
				JSONArray worldList = new JSONArray();
				
				for (CTWorld world : worldMap.values()) {
					JSONObject worldObj = new JSONObject();
					worldObj.put("worldName", world.getName());
					worldObj.put("serverName", world.getServer().getName());
					worldList.put(worldObj);
				}
				
				Packet packet = new Packet("world-list");
				packet.put("worldList", worldList);
				packet.sendTo(serverName);
				
				// Add new entries
				for (Object name : worldlist) {
					String worldName = (String) name;
					if (!worldMap.containsKey(worldName)) worldMap.put(worldName, new CTWorld(worldName, server));
				}
			
				plugin.getLogger().info("Server '" + received.getSender() + "' registered");
			}
		});
		
		messaging.on("server-disconnect", new Callback() {
			@Override
			public void run(ReceivedPacket received) {
				String serverName = received.getValues().getString("serverName");
				
				for (CTWorld world : worldMap.values()) {
					if (world.getServer().getName().equalsIgnoreCase(serverName))
						worldMap.remove(world.getName());
				}
			}
		});
		
		messaging.on("world-loaded", new Callback() {
			@Override
			public void run(ReceivedPacket received) {
				String worldName = received.getValues().getString("worldName");
				String serverName = received.getSender();
				CTServer server = plugin.getServerHandler().getServer(serverName);
				
				if (server == null) {
					plugin.getLogger().warning("Error: Server '" + serverName + "' not found.");
					return;
				}
				
				if (!worldMap.containsKey(worldName))
					worldMap.put(worldName, new CTWorld(worldName, server));
			}
		});
		
		messaging.on("world-unloaded", new Callback() {
			@Override
			public void run(ReceivedPacket received) {
				String worldName = received.getValues().getString("worldName");
				
				if (worldMap.containsKey(worldName))
					worldMap.remove(worldName);
			}
		});
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