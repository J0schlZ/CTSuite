package de.crafttogether.ctsuite.messaging;

import de.crafttogether.ctsuite.messaging.MessagingService.Adapter;

public interface MessagingAdapter {
	public void sendTo(Packet packet, String serverName);
	public void sendAll(Packet packet);
	public String getClientName();
	public Adapter getName();
}
