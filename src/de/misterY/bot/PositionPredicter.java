package de.misterY.bot;

import java.util.ArrayList;

import de.misterY.Link;
import de.misterY.Player;
import de.misterY.Station;

public class PositionPredicter {

	/**
	 * Predicts all possible movements the specified player can make from the
	 * specified station with his tickets
	 * 
	 * @param station
	 *            the station the player is on
	 * @param player
	 *            the player to predict
	 * @return All possible positions in an ArrayList
	 */
	private ArrayList<Station> predictPositions(Station station, Player player) {
		boolean hasBusTickets;
		boolean hasUndergroundTickets;
		boolean hasTaxiTickets;
		hasTaxiTickets = player.getTaxiTickets() > 0;
		hasBusTickets = player.getBusTickets() > 0;
		hasUndergroundTickets = player.getUndergroundTickets() > 0;
		ArrayList<Link> stationLinks = station.getLinks();
		ArrayList<Link> possibleLinks = new ArrayList<Link>();
		for (Link l : stationLinks) {
			if (l.isBus() && hasBusTickets)
				possibleLinks.add(l);
			if (l.isUnderground() && hasUndergroundTickets)
				possibleLinks.add(l);
			if (!l.isBus() && !l.isUnderground() && hasTaxiTickets)
				possibleLinks.add(l);
		}
		ArrayList<Station> predictedPositions = new ArrayList<Station>();
		for (Link k : possibleLinks) {
			predictedPositions.add(k.getStation());
		}
		return predictedPositions;
	}

	/**
	 * Returns if for the given parameters the can be a prediction with 100%
	 * accuracy
	 * 
	 * @param station
	 * @param player
	 * @return
	 */
	public boolean canPredictDefinitePosition(Station station, Player player) {
		return predictPositions(station, player).size() == 1;
	}

	/**
	 * Gets only the first position in the array
	 * 
	 * @param station
	 * @param player
	 * @return
	 */
	public Station getDefinitePosition(Station station, Player player) {
		return predictPositions(station, player).get(0);
	}

	/**
	 * Gets all possible positions
	 * 
	 * @param station
	 * @param player
	 * @return
	 */
	public ArrayList<Station> getAllPredictions(Station station, Player player) {
		return predictPositions(station, player);
	}
}
