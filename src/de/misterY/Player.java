package de.misterY;

import de.misterY.net.PROTOCOL;
import de.misterY.pathfinding.PathFinder;

public class Player {
	private String name;
	private Station currentStation;
	private Map map;
	private boolean isReady;
	private int taxiTickets;
	private int busTickets;
	private int undergroundTickets;
	private boolean isTurn;

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
		this.taxiTickets = taxiTickets;
		this.busTickets = busTickets;
		this.undergroundTickets = undergroundTickets;
		chooseRandomStartStation();
	}

	/**
	 * Chooses a random station to start on which is neither mister y nor another
	 * player and makes it to the current station.
	 */
	private void chooseRandomStartStation() {
		currentStation = map.getRandomStartStation();
	}

	/**
	 * Moves to the given station with the given means of transportation if
	 * possible.
	 * 
	 * @param station
	 *            The station to move to.
	 * @param type
	 * @return
	 */
	public boolean moveTo(Station end, MeansOfTransportation type) {
		if (!validateMovement(end, type)) {
			return false;
		}
		if (!useTicket(type)) {
			return false;
		}
		currentStation = end;
		return true;
	}

	/**
	 * Validates this player's movement by checking whether a movement from the
	 * specified starting point to the specified end point is possible, also
	 * considering if this player has enough tickets.
	 * 
	 * @param end
	 *            The end station
	 * @param type
	 *            The mean of transportation
	 * @return True if movement is valid, false otherwise
	 */
	public boolean validateMovement(Station end, MeansOfTransportation type) {
		return currentStation.isMeansOfTransportation(type) && hasEnoughTickets(type)
				&& PathFinder.isReachable(currentStation, end, type);
	}

	/**
	 * Adds one ticket of the given means of transportation.
	 * 
	 * @param type
	 */
	public void addTicket(MeansOfTransportation type) {
		switch (type) {
		case Taxi:
			taxiTickets++;
			break;
		case Bus:
			busTickets++;
			break;
		case Underground:
			undergroundTickets++;
			break;

		default:
			break;
		}
	}

	/**
	 * Use a ticket
	 * 
	 * @param type
	 *            the type of ticket
	 * @return true if use succeeded, false otherwise (there were not enough
	 *         tickets)
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

	/**
	 * Checks whether this player has enough tickets for the given means of
	 * transport.
	 * 
	 * @param type
	 *            The means of transport.
	 * @return Whether this player has enough tickets.
	 */
	public boolean hasEnoughTickets(MeansOfTransportation type) {
		switch (type) {
		case Taxi:
			if (taxiTickets > 0) {
				return true;
			}
			break;
		case Bus:
			if (busTickets > 0) {
				return true;
			}
			break;
		case Underground:
			if (undergroundTickets > 0) {
				return true;
			}
			break;

		default:
			throw new IllegalStateException("Unknown mean of transportaion: \"" + type + "\"");
		}
		return false;
	}

	/**
	 * Return all the ticket information of this player.</br>
	 * Returns the following separated by {@code PROTOCOL.SPLIT}: user name, taxi
	 * tickets, bus tickets, underground tickets, current station id, whether this
	 * player is mrY
	 * 
	 * @return the info string
	 */
	public String getInfoString() {
		return PROTOCOL.buildMessage(name, getTaxiTickets(), getBusTickets(), getUndergroundTickets(),
				currentStation.getId(), mrY);
	}

	public void setCurrentStation(Station currentStation) {
		this.currentStation = currentStation;
	}

	public void setMrY(boolean mrY) {
		this.mrY = mrY;
	}

	public void setBusTickets(int busTickets) {
		this.busTickets = busTickets;
	}

	public void setTaxiTickets(int taxiTickets) {
		this.taxiTickets = taxiTickets;
	}

	public void setUndergroundTickets(int undergroundTickets) {
		this.undergroundTickets = undergroundTickets;
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

	public boolean isTurn() {
		return isTurn;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	public void setTurn(boolean isTurn) {
		this.isTurn = isTurn;
	}

	public boolean isReady() {
		return this.isReady;
	}

}
