package de.misterY.client;

import java.util.ArrayList;

import de.misterY.Map;
import de.misterY.MapLoader;
import de.misterY.MeansOfTransportation;
import de.misterY.Player;
import de.misterY.net.Client;
import de.misterY.net.PROTOCOL;

public class GameClient extends Client {

	private Runnable updateRunnable;
	private Runnable errorRunnable;
	private Runnable chatRunnable;
	private ArrayList<Player> players = new ArrayList<Player>();
	private MeansOfTransportation[] ticketsUsedByMisterY = new MeansOfTransportation[0];
	private Map map;
	private ChatHandler chatHandler = new ChatHandler();
	private int errorCode;
	private boolean started;
	private int round = 1;
	private Player winner = null;
	private boolean finished;

	public GameClient() {
		super(PROTOCOL.IP, PROTOCOL.PORT);
	}

	@Override
	public void processMessage(String message) {
		System.out.println(message);
		String[] msgParts = message.split(PROTOCOL.SPLIT);

		switch (msgParts[0]) {
		case PROTOCOL.SC.ERROR:
			if (errorRunnable != null) {
				errorCode = Integer.parseInt(msgParts[1]);
				errorRunnable.run();
			}
			return;
		case PROTOCOL.SC.OK:

			break;
		case PROTOCOL.SC.CHAT_UPDATE:
			handleChatUpdate(msgParts);
			break;
		case PROTOCOL.SC.INFO_UPDATE:
			handleInfoUpdate(msgParts);
			break;
		case PROTOCOL.SC.USED_TICKETS:
			handleUsedTickets(msgParts);
			break;
		case PROTOCOL.SC.MAP:
			map = MapLoader.loadMap(msgParts[1]);
			break;
		case PROTOCOL.SC.TURN:
			handleTurn(msgParts);
			break;
		case PROTOCOL.SC.PLAYER_LEFT:
			break;
		case PROTOCOL.SC.WIN:
			handleWin(msgParts);
			break;

		default:
			break;
		}
		if (updateRunnable != null && !finished) {
			updateRunnable.run();
			if (winner != null) {
				finished = true;
			}
		}
	}

	private void handleWin(String[] msgParts) {
		winner = getPlayerByName(msgParts[1]);
	}

	private void handleInfoUpdate(String[] msgParts) {
		String name = msgParts[1];
		int taxiTickets = Integer.parseInt(msgParts[2]);
		int busTickets = Integer.parseInt(msgParts[3]);
		int undergroundTickets = Integer.parseInt(msgParts[4]);
		int currentStationId = Integer.parseInt(msgParts[5]);
		boolean isMrY = Boolean.parseBoolean(msgParts[6]);

		Player player = getPlayerByName(name);
		if (player == null) {
			player = new Player(name);
			player.setMap(map);
			players.add(player);
		}
		player.setTaxiTickets(taxiTickets);
		player.setBusTickets(busTickets);
		player.setUndergroundTickets(undergroundTickets);
		player.setCurrentStation(map.getStationById(currentStationId));
		player.setMrY(isMrY);
	}
	
	private void handleUsedTickets(String[] msgParts) {
		ticketsUsedByMisterY = new MeansOfTransportation[msgParts.length - 1];
		for (int i = 0; i < msgParts.length - 1; i++) {
			ticketsUsedByMisterY[i] = MeansOfTransportation.valueOf(msgParts[i + 1]);
		}
	}
	
	private void handleChatUpdate(String[] msgParts) {
		chatHandler.addMessage(getPlayerByName(msgParts[1]), msgParts[2]);
		if (chatRunnable != null) {
			chatRunnable.run();
		}
	}

	private void handleTurn(String[] msgParts) {
		started = true;
		String name = msgParts[1];
		Player player = getPlayerByName(name);
		if (player == null) {
			return;
		}
		for (Player p : players) {
			p.setTurn(false);
		}
		player.setTurn(true);
		round = Integer.parseInt(msgParts[2]);
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	/**
	 * Returns the player with the given name.<br>
	 * Returns null if there is no player with the given name.
	 * 
	 * @param name
	 * @return
	 */
	public Player getPlayerByName(String name) {
		for (Player player : players) {
			if (player.getName().equals(name)) {
				return player;
			}
		}
		return null;
	}

	/**
	 * Returns the current player.<br>
	 * Returns null if there is no current player (which should not happen).
	 * 
	 * @return
	 */
	public Player getCurrentPlayer() {
		for (Player player : players) {
			if (player.isTurn()) {
				return player;
			}
		}
		return null;
	}
	
	public int getRound() {
		return round;
	}
	
	public MeansOfTransportation[] getTicketsUsedByMisterY() {
		return ticketsUsedByMisterY;
	}

	public int getErrorCode() {
		return errorCode;
	}
	
	public ChatHandler getChatHandler() {
		return chatHandler;
	}

	public void setUpdateRunnable(Runnable updateRunnable) {
		this.updateRunnable = updateRunnable;
	}

	public void setErrorRunnable(Runnable errorRunnable) {
		this.errorRunnable = errorRunnable;
	}
	
	public void setChatRunnable(Runnable chatRunnable) {
		this.chatRunnable = chatRunnable;
	}

	public Map getMap() {
		return map;
	}

	public boolean isStarted() {
		return started;
	}
	
	public Player getWinner() {
		return winner;
	}
	
	public boolean isFinished() {
		return finished;
	}
}
