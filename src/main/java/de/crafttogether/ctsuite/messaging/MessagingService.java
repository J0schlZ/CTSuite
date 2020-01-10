package de.crafttogether.ctsuite.messaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.crafttogether.ctsuite.util.PluginEnvironment;

public class MessagingService {
	private static MessagingService messagingService;
	
	public interface Callback {
		public void run(ReceivedPacket packet);
	}
	
	public enum Adapter {
		CTSOCKETS;
	}
	
	private Map<String, List<Callback>> eventMap;
	
	private MessagingAdapter adapter;
	
	public MessagingService(Adapter adapterName, PluginEnvironment environment) {
		messagingService = this;
		
		this.eventMap = new HashMap<String, List<Callback>>();
		
		switch (environment) {
			case BUNGEE:
				switch(adapterName) {
					case CTSOCKETS: adapter = new de.crafttogether.ctsuite.messaging.bungee.adapter.CTSocketsAdapter(this); break;
				}
			break;
			
			case BUKKIT:
				switch(adapterName) {
					case CTSOCKETS: adapter = new de.crafttogether.ctsuite.messaging.bukkit.adapter.CTSocketsAdapter(this); break;
				}
			break;
		}
	}
	
	public void on(String eventName, Callback cb) {
		if (eventMap.containsKey(eventName))
			eventMap.get(eventName).add(cb);
		else
			eventMap.put(eventName, new ArrayList<>(Arrays.asList(cb)));
	}
	
	public Map<String, List<Callback>> getEvents() {
		return eventMap;
	}

	public static MessagingService getInstance() {
		return messagingService;
	}

	public MessagingAdapter getAdapter() {
		return adapter;
	}
}