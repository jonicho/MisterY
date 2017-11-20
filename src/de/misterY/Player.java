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
	public Player(String name, boolean mrY) {
		this.name = name;
		this.mrY = mrY;
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
	 * player.
	 */
	private void chooseRandomStartStation() {

	}

	/**
	 * Use a ticket
	 * 
	 * @param type
	 *            the type of ticket
	 * @return true if there are still tickets, false if there are not
	 */
	public boolean useTicket(MeansOfTransportation type) {
		return false;
	}

	public void setCurrentStation(Station currentStation) {
		this.currentStation = currentStation;
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

}
