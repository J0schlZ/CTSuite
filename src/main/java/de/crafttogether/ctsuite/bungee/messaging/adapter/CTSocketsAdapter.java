package de.crafttogether.ctsuite.bungee.messaging.adapter;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.event.EventHandler;
import de.crafttogether.ctsockets.bungee.CTSockets;
import de.crafttogether.ctsockets.bungee.events.MessageReceivedEvent;
import de.crafttogether.ctsockets.bungee.events.ServerConnectedEvent;
import de.crafttogether.ctsockets.bungee.events.ServerDisconnectedEvent;
import de.crafttogether.ctsuite.bungee.messaging.MessagingService;
import de.crafttogether.ctsuite.bungee.messaging.MessagingService.Adapter;
import de.crafttogether.ctsuite.bungee.messaging.MessagingService.Callback;
import de.crafttogether.ctsuite.bungee.messaging.Packet;
import de.crafttogether.ctsuite.bungee.messaging.ReceivedPacket;
import de.crafttogether.ctsuite.bungee.messaging.MessagingAdapter;
import de.crafttogether.ctsuite.bungee.CTSuite;

public class CTSocketsAdapter implements MessagingAdapter, Listener
{	
    private CTSockets ctSockets;
    private MessagingService messaging;
    
    public CTSocketsAdapter(MessagingService messaging) {
    	CTSuite plugin = CTSuite.getInstance();
    	ProxyServer proxy = plugin.getProxy();
    	PluginManager pm = proxy.getPluginManager();
    	
        this.ctSockets = CTSockets.getInstance();
		this.messaging = messaging;
		
		ctSockets = (CTSockets) proxy.getPluginManager().getPlugin("CTSockets");
    	
        if (ctSockets == null || !(ctSockets instanceof CTSockets)) {
        	plugin.getLogger().warning("Couln't find CTSockets");
        	pm.unregisterListeners(plugin);
        	pm.unregisterCommands(plugin);
        	plugin.onDisable();
            return;
        }
		
        pm.registerListener(plugin, this);
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
	public void sendAll(Packet packet) {
		ctSockets.sendToAllServers(buildPacket(packet).toString());		
	}

	@Override
	public Adapter getName(Adapter adapterName) {
		return Adapter.CTSOCKETS;		
	}
}