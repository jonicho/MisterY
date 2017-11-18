package de.misterY.server;

import de.misterY.Player;

public class User {
	private String ip;
	private int port;
	private Player player;
	
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
}
