package de.misterY.server;

import java.util.Date;

import de.misterY.net.PROTOCOL;

public class Logger {
	public static void logMessage(String msg, User user, Session session, boolean incoming) {
		String output = "[" + new Date().toString() + "] " + (session != null ? ("Session " + session.getId() + ": ") : "");
		output += (incoming ? "Got message from \"" : "Sent message to \"") + user.getPlayer().getName() + "\": "
				+ msg.replace(PROTOCOL.SPLIT, "::");
		System.out.println(output);
	}
}
