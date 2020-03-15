package de.crafttogether.ctsuite.bukkit.handlers;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.crafttogether.ctsuite.bukkit.CTSuite;
import de.crafttogether.ctsuite.messaging.MessagingService;
import de.crafttogether.ctsuite.util.CTPlayer;
import de.crafttogether.ctsuite.util.CTServer;
import de.crafttogether.ctsuite.util.CTWorld;

public class PlayerHandler {
	private CTSuite plugin;
	private MessagingService messaging;
	
	private ConcurrentHashMap<UUID, CTPlayer> playerMap;
	
	public PlayerHandler() {
		plugin = CTSuite.getInstance();
		messaging = plugin.getMessagingService();
		
		playerMap = new ConcurrentHashMap<UUID, CTPlayer>();
		
		// Add own players to playerMap
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
        	UUID uuid = player.getUniqueId();
			String playerName = player.getName();
			String serverName = messaging.getAdapter().getClientName();
			CTServer server = plugin.getServerHandler().getServer(serverName);
			CTWorld world = plugin.getWorldHandler().getWorld(player.getWorld().getName());
			
			if (!playerMap.containsKey(uuid))
				playerMap.put(uuid, new CTBukkitPlayer(uuid, playerName, server, world));
		}
		
		addPacketListeners();
	}
	
	private void addPacketListeners() {
		/*
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
				
				// Add new entries
				for (Object name : worldlist) {
					String worldName = (String) name;
					if (!worldMap.containsKey(worldName)) worldMap.put(worldName, new CTWorld(worldName, server));
				}
				
				// Remove old entries
				for (String worldName : worldMap.keySet()) {
					if (!worldlist.contains(serverName))
						worldMap.remove(worldName);
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

		messaging.on("socket-disconnect", new Callback() {
			@Override
			public void run(ReceivedPacket received) {
				String serverName = plugin.getMessagingService().getAdapter().getClientName();
				
				for (CTWorld world : worldMap.values()) {
					if (!world.getServer().getName().equalsIgnoreCase(serverName))
						worldMap.remove(world.getName());
				}
			}
		});
		
		messaging.on("player-list", new Callback() {
			@Override
			public void run(ReceivedPacket packet) {
				JSONArray jsonWorldList = packet.getValues().getJSONArray("worldList");
				List<String> worldList = new ArrayList<String>();
				
				// Add new entries
				for (Object item : jsonWorldList) {
					JSONObject worldObj = (JSONObject) item;
					String worldName = worldObj.getString("worldName");
					String serverName = worldObj.getString("serverName");
					
					worldList.add(worldName);
					CTServer server = plugin.getServerHandler().getServer(serverName);
					
					if (server == null) {
						plugin.getLogger().warning("Error: Server '" + serverName + "' not found.");
						return;
					}
					
					if (!worldMap.containsKey(worldName))
						worldMap.put(worldName, new CTWorld(worldName, server));
				}

				if (packet.getSender().equalsIgnoreCase("#proxy")) {
					// Remove old entries
					for (String worldName : worldMap.keySet()) {
						if (!worldList.contains(worldName))
							worldMap.remove(worldName);
					}
				}
			}
		});
		
		messaging.on("player-connection", new Callback() {
			@Override
			public void run(ReceivedPacket received) {
				String worldName = received.getValues().getString("worldName");
				String serverName = received.getValues().getString("serverName");
				CTServer server = plugin.getServerHandler().getServer(serverName);
				
				if (server == null) {
					plugin.getLogger().warning("Error: Server '" + serverName + "' not found.");
					return;
				}
				
				if (!worldMap.containsKey(worldName))
					worldMap.put(worldName, new CTWorld(worldName, server));
			}
		});
		
		messaging.on("player-disconnect", new Callback() {
			@Override
			public void run(ReceivedPacket received) {
				String worldName = received.getValues().getString("worldName");
				
				if (worldMap.containsKey(worldName))
					worldMap.remove(worldName);
			}
		});
		*/
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
