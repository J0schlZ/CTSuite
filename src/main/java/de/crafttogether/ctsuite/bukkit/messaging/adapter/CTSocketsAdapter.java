package de.crafttogether.ctsuite.bukkit.messaging.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.json.JSONObject;

import de.crafttogether.ctsockets.bukkit.CTSockets;
import de.crafttogether.ctsockets.bukkit.events.MessageReceivedEvent;
import de.crafttogether.ctsockets.bukkit.events.ServerConnectedEvent;
import de.crafttogether.ctsockets.bukkit.events.ServerDisconnectedEvent;
import de.crafttogether.ctsuite.bukkit.messaging.MessagingService;
import de.crafttogether.ctsuite.bukkit.messaging.MessagingService.Adapter;
import de.crafttogether.ctsuite.bukkit.messaging.MessagingService.Callback;
import de.crafttogether.ctsuite.bukkit.messaging.Packet;
import de.crafttogether.ctsuite.bukkit.messaging.ReceivedPacket;
import de.crafttogether.ctsuite.bukkit.messaging.MessagingAdapter;
import de.crafttogether.ctsuite.bukkit.CTSuite;

public class CTSocketsAdapter implements MessagingAdapter, Listener
{	
    private CTSockets ctSockets;
    private MessagingService messaging;
    
    public CTSocketsAdapter(MessagingService messaging) {
    	CTSuite plugin = CTSuite.getInstance();
    	
        this.ctSockets = CTSockets.getInstance();
		this.messaging = messaging;
		
		ctSockets = (CTSockets) Bukkit.getServer().getPluginManager().getPlugin("CTSockets");
    	
        if (ctSockets == null || !(ctSockets instanceof CTSockets)) {
        	plugin.getLogger().warning("Couln't find CTSockets");
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
    public void onServerConnected(ServerConnectedEvent ev) {
		Map<String, List<Callback>> eventMap = messaging.getEvents();
		JSONObject values = new JSONObject();
		values.put("serverName", ev.getServerName());
		
		String packetId = "server-connection";
		if (eventMap.containsKey(packetId)) {
			List<Callback> events = eventMap.get(packetId);
			for (Callback event : events)
				event.run(new ReceivedPacket(packetId, "proxy", values));
		}
    }
    
    @EventHandler
    public void onServerDisconnected(ServerDisconnectedEvent ev) {
		Map<String, List<Callback>> eventMap = messaging.getEvents();
		JSONObject values = new JSONObject();
		values.put("serverName", ev.getServerName());
		
		String packetId = "server-disconnect";
		if (eventMap.containsKey(packetId)) {
			List<Callback> events = eventMap.get(packetId);
			for (Callback event : events)
				event.run(new ReceivedPacket(packetId, "proxy", values));
		}
    }
    
    @EventHandler
    public void onMessageReceived(MessageReceivedEvent ev) {
    	JSONObject message = null;
    	String packetId = null;
    	JSONObject values = null;
    	
    	try {
    		message = new JSONObject(ev.getMessage());
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		return;
    	}
    	
    	// Filter packets
    	if (!message.has("packetId") || !(message.get("packetId") instanceof String)
    	||  !message.has("values") || !(message.get("values") instanceof JSONObject)) {
    		CTSuite.getInstance().getLogger().warning("Invalid packet received!");
    		CTSuite.getInstance().getLogger().warning(ev.getMessage());
    		return;
    	}

    	packetId = message.getString("packetId");
    	values = message.getJSONObject("values");
    	
		Map<String, List<Callback>> eventMap = messaging.getEvents();
		
		if (eventMap.containsKey(packetId)) {
			List<Callback> events = eventMap.get(packetId);
			for (Callback event : events)
				event.run(new ReceivedPacket(packetId, ev.getSender(), values));
		}
    }

    private JSONObject buildPacket(Packet packet) {
    	JSONObject newPacket = new JSONObject();
    	JSONObject values = packet.getValues();
    	
		newPacket.put("packetId", packet.getId());
		newPacket.put("values", values);
		return newPacket;
    }
    
	@Override
	public void sendTo(Packet packet, String serverName) {
		ctSockets.sendToServer(serverName, buildPacket(packet).toString());
	}

	@Override
	public void sendProxy(Packet packet) {
		ctSockets.sendToProxy(buildPacket(packet).toString());
	}

	@Override
	public void sendServer(Packet packet) {
		ctSockets.sendToAllServers(buildPacket(packet).toString());		
	}

	@Override
	public void sendAll(Packet packet) {
		ctSockets.sendToAll(buildPacket(packet).toString());		
	}

	@Override
	public Adapter getName(Adapter adapterName) {
		return Adapter.CTSOCKETS;		
	}
}