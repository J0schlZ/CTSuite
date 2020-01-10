package de.crafttogether.ctsuite.messaging;

public interface BukkitMessagingAdapter extends MessagingAdapter {
	void sendProxy(Packet packet);
	void sendAllServers(Packet packet);
}