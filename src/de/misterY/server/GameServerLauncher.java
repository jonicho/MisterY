package de.misterY.server;

import de.misterY.net.PROTOCOL;

public class GameServerLauncher {
	public static void main(String[] args) {
		new GameServer(PROTOCOL.PORT);
	}
}
