package de.crafttogether.ctsuite.messaging;

import de.crafttogether.ctsuite.messaging.MessagingService.Adapter;

public abstract interface MessagingAdapter {
	public String getClientName();
	public Adapter getName();
}
