package de.crafttogether.ctsuite.messaging;

import de.crafttogether.ctsuite.messaging.bukkit.Packet;

public interface BukkitMessagingAdapter extends MessagingAdapter {
	public void sendTo(Packet packet, String serverName);
	public void sendAll(Packet packet);
	public void sendProxy(Packet packet);
	public void sendAllServers(Packet packet);
}