package de.misterY.bot;

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
	private String myName;
	private Map map;
	private Station myStation;
	private boolean started;

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
			handleInfoUpdate(msgParts);
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
			handleTurn(msgParts);
			break;
		case PROTOCOL.SC.PLAYER_LEFT:
			if (started)
				close();
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

	private void handleTurn(String[] msgParts) {
		started = true;
		if (!msgParts[1].equals(myName)) {
			return;
		}
		brain.doAnalysis();
		brain.moveExecute();
		targetID = brain.getTarget();
		if (targetID > 0) {
			moveToStation(map.getStationById(targetID));
		}
		if (targetID == -1 || targetID == -5) {
			Station[] possibleStations = PathFinder.findPossibleStations(myStation);
			moveToStation(possibleStations[(int) (Math.random() * possibleStations.length)]);
		}
	}

	private void handleInfoUpdate(String[] msgParts) {
		String name = msgParts[1];
		int taxiTickets = Integer.parseInt(msgParts[2]);
		int busTickets = Integer.parseInt(msgParts[3]);
		int undergroundTickets = Integer.parseInt(msgParts[4]);
		int currentStationId = Integer.parseInt(msgParts[5]);
		boolean isMrY = Boolean.parseBoolean(msgParts[6]);

		if (name.equals(myName)) {
			myStation = map.getStationById(currentStationId);
			brain.getLocalPlayer().setCurrentStation(myStation);
		}
		if (isMrY) {
			Station currentStation = map.getStationById(currentStationId);
			Player mryHandle = new Player(name);
			mryHandle.setTaxiTickets(taxiTickets);
			mryHandle.setBusTickets(busTickets);
			mryHandle.setUndergroundTickets(undergroundTickets);
			mryHandle.setCurrentStation(currentStation);
			brain.setMryHandle(mryHandle);
			if (currentStation != null && lastStation != currentStation) {
				lastStation = currentStation;
			}
		}
	}

	/**
	 * Executes the moves selected by the Analysis
	 * 
	 */

	private void moveToStation(Station pStation) {
		try {
			this.send(PROTOCOL.buildMessage(PROTOCOL.CS.REQUEST_MOVEMENT, pStation.getId(),
					PathFinder.getPossibleMeansOfTransportation(myStation, pStation)[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
