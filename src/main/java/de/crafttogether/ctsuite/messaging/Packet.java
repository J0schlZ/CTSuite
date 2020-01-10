package de.crafttogether.ctsuite.messaging;

import org.json.JSONObject;

public class Packet
{
    private String packetId;
    private String receiver;
    private MessagingService messaging;
    private MessagingAdapter adapter;
    private JSONObject values;
    
    public Packet(String packetId) {
        this.packetId = packetId;
        this.values = new JSONObject();
        this.messaging = MessagingService.getInstance();
        this.adapter = messaging.getAdapter();
    }
    
    public void put(String key, Object value) {
        this.values.put(key, value);
    }
    
    public void sendTo(String server) {
        this.receiver = server;
        adapter.sendTo(this, receiver);
    }
    
    public void sendAll() {
        this.receiver = "#server";
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