package de.misterY.bot;

import java.util.Random;

import de.misterY.Map;
import de.misterY.MeansOfTransportation;
import de.misterY.PathFinder;
import de.misterY.Player;
import de.misterY.Station;
import de.misterY.map.MapLoader;
import de.misterY.net.Client;
import de.misterY.net.PROTOCOL;

public class Bot extends Client {
	private AI brain = new AI();
	private Station lastStation;
	private int targetID;
	private Random ran = new Random();
	private String myName;
	private Map map;
	private Station myStation;

	public Bot(String pServerIP, int pServerPort, String name) {
		super(pServerIP, pServerPort);
		myName = name;
		send(PROTOCOL.buildMessage(PROTOCOL.CS.LOGIN, myName));
		send(PROTOCOL.CS.READY);
		Player player = new Player(myName);
		brain.initialize(null, player);
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
			// ignore the chat
			break;
		case PROTOCOL.SC.INFO_UPDATE:
			if (msgParts[1].equals(myName)) {
				myStation = map.getStationById(Integer.parseInt(msgParts[5]));
				brain.localPlayer.setCurrentStation(myStation);
			}
			if (Boolean.parseBoolean(msgParts[6])) {
				Station currentStation = map.getStationById(Integer.parseInt(msgParts[5]));
				if (currentStation != null && lastStation != currentStation) {
					lastStation = currentStation;
				}
			}
			break;
		case PROTOCOL.SC.USED_TICKETS:
			MeansOfTransportation[] pTickets = new MeansOfTransportation[msgParts.length - 1];
			for (int i = 1; i < msgParts.length; i++) {
				pTickets[i - 1] = MeansOfTransportation.valueOf(msgParts[i]);
			}
			brain.updateData(lastStation, pTickets);
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
		brain.doAnalysis();
		brain.MoveExecute();
		targetID = brain.getTarget();
		if (targetID > 0) {
			MoveToStation(map.getStationById(targetID));
		}
		if (targetID == -1 || targetID == -5) {
			MoveToStation(PathFinder.findPathToNearestStation(myStation.getLinks().get((int) (myStation.getLinks().size() * Math.random())).getStation(), MeansOfTransportation.Taxi).getLastStation());
		}
	}

	/**
	 * Executes the moves selected by the Analysis
	 * 
	 */

	private void MoveToStation(Station pStation) {
		this.send(PROTOCOL.buildMessage(PROTOCOL.CS.REQUEST_MOVEMENT, pStation.getId(),
				PathFinder.findPath(myStation, pStation).getFollowingStation(myStation).getId()));
	}

}
