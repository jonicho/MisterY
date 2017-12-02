package de.misterY.client;

import de.misterY.net.Client;
import de.misterY.net.PROTOCOL;

public class GameClient extends Client {

	public GameClient(String serverIP, int serverPort) {
		super(serverIP, serverPort);
	}

	@Override
	public void processMessage(String message) {
		String[] msgParts = message.split(PROTOCOL.SPLIT);
		
		switch (msgParts[0]) {
		case PROTOCOL.SC.ERROR:
			
			break;
		case PROTOCOL.SC.POSITION_UPDATE:
			
			break;
		case PROTOCOL.SC.OK:
			
			break;
		case PROTOCOL.SC.CHAT_UPDATE:
			
			break;
		case PROTOCOL.SC.INFO_UPDATE:
			
			break;
		case PROTOCOL.SC.MAP:
			
			break;
		case PROTOCOL.SC.TURN:
			
			break;
		case PROTOCOL.SC.PLAYER_LEFT:
			
			break;

		default:
			break;
		}
	}

}
