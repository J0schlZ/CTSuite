package de.crafttogether.ctsuite.messaging.bukkit;

import org.json.JSONObject;

import de.crafttogether.ctsuite.messaging.BukkitMessagingAdapter;
import de.crafttogether.ctsuite.messaging.MessagingService;

public class Packet
{
    private String packetId;
    private String receiver;
    private MessagingService messaging;
    private BukkitMessagingAdapter adapter;
    private JSONObject values;
    
    public Packet(String packetId) {
        this.packetId = packetId;
        this.values = new JSONObject();
        this.messaging = MessagingService.getInstance();
        this.adapter = (BukkitMessagingAdapter) messaging.getAdapter();
    }
    
    public void put(String key, Object value) {
        this.values.put(key, value);
}
    
    public void sendTo(String server) {
        this.receiver = server;
        adapter.sendTo(this, receiver);
    }
    
    public void sendProxy() {
    	this.receiver = "#proxy";
    	adapter.sendProxy(this);
    }
    
    public void sendAllServers() {
    	this.receiver = "#server";
    	adapter.sendAllServers(this);
    }
    
    public void sendAll() {
        this.receiver = "#all";
    	adapter.sendAll(this);
    }
    
    public String getId() {
        return this.packetId;
    }
    
    public String getReciever() {
        return this.receiver;
    }
    
    public JSONObject getValues() {
        return this.values;
    }
}