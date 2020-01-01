package de.crafttogether.ctsuite.bukkit.messaging;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.crafttogether.ctsockets.bukkit.CTSockets;
import de.crafttogether.ctsockets.bukkit.events.ServerConnectedEvent;
import de.crafttogether.ctsockets.bukkit.events.ServerDisconnectedEvent;
import de.crafttogether.ctsockets.bukkit.events.MessageReceivedEvent;

public class CTSocketListener implements Listener
{
    private CTSockets plugin;
    
    public CTSocketListener() {
        this.plugin = CTSockets.getInstance();
    }
    
    @EventHandler
    public void onServerConnected(ServerConnectedEvent ev) {
    	plugin.getLogger().info("[CTSocketsTest]: `ServerConnectedEvent` " + ev.getServerName());
    }
    
    @EventHandler
    public void onServerDisconnected(ServerDisconnectedEvent ev) {
    	plugin.getLogger().info("[CTSocketsTest]: `ServerDisconnectedEvent` " + ev.getServerName());
    }
    
    @EventHandler
    public void onMessageReceived(MessageReceivedEvent ev) {
    	plugin.getLogger().info("[CTSocketsTest]: `MessageReceivedEvent` '" + ev.getSender() + "' -> '" + ev.getMessage() + "'");
    }

}