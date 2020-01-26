package de.crafttogether.ctsuite.messaging;

import de.crafttogether.ctsuite.messaging.bungee.Packet;

public interface BungeeMessagingAdapter extends MessagingAdapter {
	public void sendTo(Packet packet, String serverName);
	public void sendAll(Packet packet);
}