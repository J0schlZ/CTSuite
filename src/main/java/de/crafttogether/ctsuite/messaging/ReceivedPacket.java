package de.crafttogether.ctsuite.messaging;

import org.json.JSONObject;

public class ReceivedPacket {
	private String packetId;
	private String sender;
	private JSONObject values;
	
	public ReceivedPacket(String packetId, String sender, JSONObject values) {
		this.packetId = packetId;
		this.sender = sender;
		this.values = values;
	}

	public String getId() {
		return packetId;
	}

	public String getSender() {
		return sender;
	}

	public JSONObject getValues() {
		return values;
	}

}
