package de.crafttogether.ctsuite.bungee.messaging;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import de.crafttogether.ctsockets.bungee.CTSockets;
import de.crafttogether.ctsockets.bungee.events.ServerConnectedEvent;
import de.crafttogether.ctsockets.bungee.events.ServerDisconnectedEvent;
import de.crafttogether.ctsockets.bungee.events.MessageForwardedEvent;
import de.crafttogether.ctsockets.bungee.events.MessageReceivedEvent;

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
    	plugin.getLogger().info("[CTSocketsTest]: `MessageReceivedEvent` (" + ev.getSender() + ") -> '" + ev.getMessage() + "'");
    }
    
    @EventHandler
    public void onMessageForwarded(MessageForwardedEvent ev) {
    	plugin.getLogger().info("[CTSocketsTest]: `MessageForwardedEvent` (" + ev.getSender() + ") -> (" + ev.getTarget() + ") -> '" + ev.getMessage() + "'");
    }

}