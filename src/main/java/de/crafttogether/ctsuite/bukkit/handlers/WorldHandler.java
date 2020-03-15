package de.crafttogether.ctsuite.bukkit.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import de.crafttogether.ctsuite.bukkit.CTSuite;
import de.crafttogether.ctsuite.messaging.MessagingService;
import de.crafttogether.ctsuite.messaging.MessagingService.Callback;
import de.crafttogether.ctsuite.messaging.ReceivedPacket;
import de.crafttogether.ctsuite.messaging.bukkit.Packet;
import de.crafttogether.ctsuite.util.CTServer;
import de.crafttogether.ctsuite.util.CTWorld;

public class WorldHandler implements Listener {
	private CTSuite plugin;
	private MessagingService messaging;
	
	private ConcurrentHashMap<String, CTWorld> worldMap;
	
	public WorldHandler() {
		plugin = CTSuite.getInstance();
		messaging = plugin.getMessagingService();
		
		worldMap = new ConcurrentHashMap<String, CTWorld>();
		
		/*MultiverseCore multiverse = CTSuite.getInstance().getMultiverseCore();
        if (multiverse != null) {
        	multiverse = multiverse.getCore();
            final Collection<MultiverseWorld> MVWorlds = (Collection<MultiverseWorld>)multiverse.getMVWorldManager().getMVWorlds();
            for (final MultiverseWorld world : MVWorlds) {
            	String worldName = world.getName();
    			String serverName = messaging.getAdapter().getClientName();
    			CTServer server = plugin.getServerHandler().getServer(serverName);
    			
    			if (!worldMap.containsKey(worldName))
    				worldMap.put(worldName, new CTWorld(worldName, server));
            }
        }*/
		
		// Add own worlds to worldMap
		for (World world : Bukkit.getServer().getWorlds()) {
        	String worldName = world.getName();
			String serverName = messaging.getAdapter().getClientName();
			CTServer server = plugin.getServerHandler().getServer(serverName);
			
			if (!worldMap.containsKey(worldName))
				worldMap.put(worldName, new CTWorld(worldName, server));
		}
		
		// Register Bukkit Events
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
		// Register depending Packets
		addPacketListeners();
	}

	@EventHandler (priority = EventPriority.MONITOR)
	public void onWorldLoad(WorldLoadEvent e) {
		plugin.getLogger().info("World '" + e.getWorld().getName() + "' loaded");
		Packet packet = new Packet("world-loaded");
		packet.put("worldName", e.getWorld().getName());
		packet.sendAll();
	}

	@EventHandler (priority = EventPriority.MONITOR)
	public void onWorldUnload(WorldUnloadEvent e) {
		plugin.getLogger().info("World '" + e.getWorld().getName() + "' unloaded");
		Packet packet = new Packet("world-unloaded");
		packet.put("worldName", e.getWorld().getName());
		packet.sendAll();
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
				
				// Add new entries
				for (Object obj : worldlist) {
					CTWorld world = (CTWorld) obj;
					if (!worldMap.containsKey(world.getName())) worldMap.put(world.getName(), world);
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
		
		messaging.on("world-list", new Callback() {
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