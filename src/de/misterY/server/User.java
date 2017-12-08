package de.misterY.server;

import de.misterY.Player;

public class User {
	private String ip;
	private int port;
	private Player player;
	private boolean isInSession;

	public User(String ip, int port, String name) {
		this.ip = ip;
		this.port = port;
		player = new Player(name);
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public Player getPlayer() {
		return player;
	}

	public void setInSession(boolean isInSession) {
		this.isInSession = isInSession;
	}

	public boolean isInSession() {
		return isInSession;
	}
}
