package de.misterY.client;

import de.misterY.net.Client;

public class GameClient extends Client {

	public GameClient(String pServerIP, int pServerPort) {
		super(pServerIP, pServerPort);
	}

	@Override
	public void processMessage(String pMessage) {
		
	}

}
