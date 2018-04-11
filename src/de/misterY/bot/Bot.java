package de.misterY.bot;

import java.util.Random;

import de.misterY.Map;
import de.misterY.MeansOfTransportation;
import de.misterY.PathFinder;
import de.misterY.Station;
import de.misterY.map.MapLoader;
import de.misterY.net.Client;
import de.misterY.net.PROTOCOL;

public class Bot extends Client {
	private AI Brain = new AI();
	private Station lastStation;
	private int targetID;
	private Random ran = new Random();
	private String myName;
	private Map map;
	private Station myStation;
	
	public Bot(String pServerIP, int pServerPort) {
		super(pServerIP, pServerPort);
		myName = "BOT"+ran.nextInt(9999);
		this.send(PROTOCOL.buildMessage(PROTOCOL.CS.LOGIN,myName));
		Brain.initialize(Brain.localPlayer.getCurrentStation(),Brain.localPlayer);
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
			if (msgParts[1] == myName) {
				myStation = map.getStationById(Integer.parseInt(msgParts[5]));
			}
			if(Boolean.parseBoolean(msgParts[6])) {
				Station currentStation = map.getStationById(Integer.parseInt(msgParts[5]));
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
			map = MapLoader.loadMap(msgParts[1]);
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
	
	@Override
	public void connectionLost() {
		
	}
	
	
	
	public void handleTurn() {
		Brain.doAnalysis();
		targetID = Brain.getTarget();
		if (targetID > 0) 
		{
			MoveToStation(map.getStationById(targetID));
		}
	}
	
	/**
	 * Executes the moves selected by the Analysis
	 * 
	 */
	
	
	private void MoveToStation(Station pStation) {
		this.send(PROTOCOL.buildMessage(PROTOCOL.CS.REQUEST_MOVEMENT, pStation.getId(),PathFinder.findPath(myStation, pStation).getFollowingStation(myStation).getId()));
	}

}
