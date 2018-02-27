package de.misterY.bot;

import de.misterY.MapLoader;
import de.misterY.MeansOfTransportation;
import de.misterY.Station;
import de.misterY.net.Client;
import de.misterY.net.PROTOCOL;

public class Bot extends Client {
	private AI Brain = new AI();
	private Station lastStation;
	
	public Bot(String pServerIP, int pServerPort) {
		super(pServerIP, pServerPort);

	}

	@Override
	public void processMessage(String message) {
		System.out.println(message);
		String[] msgParts = message.split(PROTOCOL.SPLIT);

		switch (msgParts[0]) {
		case PROTOCOL.SC.ERROR:
			return;
		case PROTOCOL.SC.OK:
			break;
		case PROTOCOL.SC.CHAT_UPDATE:
			//ignore the chat
			break;
		case PROTOCOL.SC.INFO_UPDATE:
			if(Boolean.parseBoolean(msgParts[6])) {
				Station currentStation = Station.class.cast(msgParts[5]);
				if (lastStation != null && lastStation != currentStation) {
					lastStation = currentStation;
				}
			}
			break;
		case PROTOCOL.SC.USED_TICKETS:
			MeansOfTransportation pTicket = MeansOfTransportation.valueOf(msgParts[1]);
			Brain.updateData(lastStation, pTicket);
			break;
		case PROTOCOL.SC.MAP:
			break;
		case PROTOCOL.SC.TURN:
			handleTurn();
			break;
		case PROTOCOL.SC.PLAYER_LEFT:
			break;
		case PROTOCOL.SC.WIN:
			break;

		default:
			break;
		}
	}
	
	public void handleTurn() {
		
	}

}
