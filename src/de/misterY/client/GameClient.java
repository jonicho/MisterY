package de.misterY.client;

import de.misterY.net.Client;
import de.misterY.net.PROTOCOL;

public class GameClient extends Client {
	
	private Runnable updateRunnable;
	private String[] playerList;
	private String mapString;

	public GameClient(String serverIP, int serverPort) {
		super(serverIP, serverPort);
	}

	@Override
	public void processMessage(String message) {
		System.out.println(message);
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
			mapString = msgParts[1];
			break;
		case PROTOCOL.SC.TURN:
			
			break;
		case PROTOCOL.SC.PLAYER_LEFT:
			
			break;

		default:
			break;
		}
		if (updateRunnable != null) {
			updateRunnable.run();
		}
	}
	
	public void setUpdateRunnable(Runnable updateRunnable) {
		this.updateRunnable = updateRunnable;
	}
	
	public String getMapString() {
		return mapString;
	}
}
