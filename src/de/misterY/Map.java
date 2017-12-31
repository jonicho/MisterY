package de.misterY;

import java.util.ArrayList;

public class Map {
	private final ArrayList<Station> stations;
	private final ArrayList<Station> startStations;
	private final int[] initialTickets;
	private final int rounds;
	private final int[] showRounds;
	private final String mapString;

	public Map(ArrayList<Station> stations, ArrayList<Station> startStations, int[] initialTickets, int rounds,
			int[] showRounds, String mapString) {
		this.stations = stations;
		this.startStations = startStations;
		this.initialTickets = initialTickets;
		this.rounds = rounds;
		this.showRounds = showRounds;
		this.mapString = mapString;
	}

	/**
	 * Returns the station with the given id.
	 * 
	 * @param id
	 *            The id.
	 * @return The station with the given id. Null if there does not exist a station
	 *         with the given id.
	 */
	public Station getStationById(int id) {
		for (Station station : stations) {
			if (station.getId() == id)
				return station;
		}
		return null;
	}

	/**
	 * Returns an available random station to start from and deletes it from the
	 * available start stations.
	 * 
	 * @return The station. Null if there are no available start stations.
	 */
	public Station getRandomStartStation() {
		if (startStations.isEmpty())
			return null;
		Station station = startStations.get((int) (startStations.size() * Math.random()));
		startStations.remove(station);
		return station;
	}

	/**
	 * Returns the number of stations in this map. Map must be loaded.
	 * 
	 * @return the number of stations
	 */
	public int getStationCount() {
		return stations.size();
	}

	/**
	 * Returns the number of links the map has. Map must be loaded.
	 * 
	 * @return the number of links
	 */
	public int getLinkCount() {
		int result = 0;
		for (Station s : stations) {
			result += s.getLinks().size();
		}
		return result;
	}

	public ArrayList<Station> getStations() {
		return stations;
	}

	/**
	 * Returns the initial tickets of the given means of transportation.<br>
	 * If the mrY boolean is true the initial tickets of misterY are returned.
	 * 
	 * @param type
	 * @param mrY
	 * @return
	 */
	public int getInitialTickets(MeansOfTransportation type, boolean mrY) {
		int index = 0;
		switch (type) {
		case Taxi:
			break;
		case Bus:
			index += 1;
			break;
		case Underground:
			index += 2;
			break;
		default:
			break;
		}
		if (mrY) {
			index += 3;
		}
		return initialTickets[index];
	}
	
	/**
	 * @return The number of rounds.
	 */
	public int getRounds() {
		return rounds;
	}
	
	/**
	 * @return The rounds in which misterY shows himself.
	 */
	public int[] getShowRounds() {
		return showRounds;
	}

	/**
	 * @return The string that represents this map.
	 */
	public String getMapString() {
		return mapString;
	}
}
