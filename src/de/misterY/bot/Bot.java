package de.misterY.bot;

import java.util.Random;

import de.misterY.MapLoader;
import de.misterY.MeansOfTransportation;
import de.misterY.Station;
import de.misterY.net.Client;
import de.misterY.net.PROTOCOL;
import de.misterY.pathfinding.PathFinder;

public class Bot extends Client {
	private AI Brain = new AI();
	private PathFinder pFinder = new PathFinder();
	private Station lastStation;
	private int moveState;
	private Random ran = new Random();
	private String myName;
	
	public Bot(String pServerIP, int pServerPort) {
		super(pServerIP, pServerPort);
		myName = "BOT"+ran.nextInt(9999);
		this.send(PROTOCOL.buildMessage(PROTOCOL.CS.LOGIN,myName));
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
		Brain.doAnalysis();
		moveState = Brain.getMoveState();
		MoveExecute();
	}
	
	/**
	 * Executes the moves selected by the Analysis
	 * 
	 */
	public void MoveExecute() {
		switch (moveState) {
		case 0: // Nothing usefull todo, just go in a random direction
			break;
		case 1: // Go to Definitive Resolved Position
			break;
		case 2: // Go to Definitive Predcted Position
			break;
		case 4: // Go to the Next Trainstation to occupy it
			break;
		case 5: // We are chasing MRY & are one turn behind him, pick a link that matches his
				// ticket to maybe get him
			break;
		}
	}
	
	private void MoveToStation(Station pStation) {
		this.send(PROTOCOL.buildMessage(PROTOCOL.CS.REQUEST_MOVEMENT, pStation.getId()));
	}

}
