package de.misterY;

public class Player {
	private String name;
	private Station currentStation;
	private Map map;
	private int taxiTickets;
	private int busTickets;
	private int undergroundTickets;

	private boolean mrY;

	/**
	 * Constructs a new player.
	 * 
	 * @param name
	 *            The name.
	 * @param mrY
	 *            Whether the player is mrY.
	 */
	public Player(String name) {
		this.name = name;
	}

	/**
	 * Called when the game starts
	 * 
	 * @param map
	 *            The map.
	 * @param taxiTickets
	 *            The initial taxi tickets.
	 * @param busTickets
	 *            The initial bus tickets.
	 * @param undergroundTickets
	 *            The initial underground tickets.
	 */
	public void start(Map map, int taxiTickets, int busTickets, int undergroundTickets) {
		this.map = map;
		chooseRandomStartStation();
	}

	/**
	 * Chooses a random station to start on which is neither mister y nor another
	 * player and makes it to the current station.
	 */
	private void chooseRandomStartStation() {
		currentStation = map.getStationById((int) Math.round(map.getStationCount() * Math.random()));
	}

	/**
	 * Use a ticket
	 * 
	 * @param type
	 *            the type of ticket
	 * @return true if use succeeded, false otherwise (there were not enough tickets)
	 */
	public boolean useTicket(MeansOfTransportation type) {
		switch (type) {
		case Taxi:
			if (taxiTickets > 0) {
				taxiTickets--;
				return true;
			}
			break;
		case Bus:
			if (busTickets > 0) {
				busTickets--;
				return true;
			}
			break;
		case Underground:
			if (undergroundTickets > 0) {
				undergroundTickets--;
				return true;
			}
			break;

		default:
			throw new IllegalStateException("Unknown mean of transportaion: \"" + type + "\"");
		}
		return false;
	}

	public void setCurrentStation(Station currentStation) {
		this.currentStation = currentStation;
	}

	public void setMrY(boolean mrY) {
		this.mrY = mrY;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public String getName() {
		return name;
	}

	public Station getCurrentStation() {
		return currentStation;
	}

	public int getBusTickets() {
		return busTickets;
	}

	public int getTaxiTickets() {
		return taxiTickets;
	}

	public int getUndergroundTickets() {
		return undergroundTickets;
	}

	public Map getMap() {
		return map;
	}

	public boolean isMrY() {
		return mrY;
	}

}
