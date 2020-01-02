package de.crafttogether.ctsuite.bungee.messaging;

import de.crafttogether.ctsuite.bungee.messaging.Packet;
import de.crafttogether.ctsuite.bungee.messaging.MessagingService.Adapter;

public interface MessagingAdapter {
	public void sendTo(Packet packet, String serverName);
	public void sendAll(Packet packet);
	public Adapter getName(Adapter adapterName);
}