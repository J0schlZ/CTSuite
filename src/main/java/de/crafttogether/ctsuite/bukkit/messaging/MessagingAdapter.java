package de.crafttogether.ctsuite.bukkit.messaging;

import de.crafttogether.ctsuite.bukkit.messaging.Packet;
import de.crafttogether.ctsuite.bukkit.messaging.MessagingService.Adapter;

public interface MessagingAdapter {
	public void sendTo(Packet packet, String serverName);
	public void sendProxy(Packet packet);
	public void sendServer(Packet packet);
	public void sendAll(Packet packet);
	public Adapter getName(Adapter adapterName);
}