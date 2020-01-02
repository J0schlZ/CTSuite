package de.crafttogether.ctsuite.bukkit.messaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import de.crafttogether.ctsuite.bukkit.messaging.MessagingAdapter;
import de.crafttogether.ctsuite.bukkit.messaging.adapter.CTSocketsAdapter;

public class MessagingService {
	private static MessagingService messagingService;
	
	public interface Callback {
		void run(ReceivedPacket packet);
	}
	
	public enum Adapter {
		CTSOCKETS;
	}
	
	private Map<String, List<Callback>> eventMap;
	
	private MessagingAdapter adapter;
	
	public MessagingService(Adapter adapterName) {
		messagingService = this;
		
		this.eventMap = new HashMap<String, List<Callback>>();
		
		switch(adapterName) {
			case CTSOCKETS: adapter = new CTSocketsAdapter(this); break;
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