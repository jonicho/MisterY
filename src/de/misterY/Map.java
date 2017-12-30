package de.misterY;

import java.io.File;
import java.util.ArrayList;

public class Map {
	private ArrayList<Station> stations = new ArrayList<Station>();
	private ArrayList<Station> startStations = new ArrayList<Station>();
	private int[] initialTickets = new int[6];
	private final String mapString;

	/**
	 * Constructs a new map from the given map file.
	 * 
	 * @param file
	 *            The file to load the map from.
	 */
	public Map(File file) {
		mapString = load(file);
	}

	/**
	 * Constructs a new map from the given map string.
	 * 
	 * @param mapString
	 *            The string to load the map from.
	 */
	public Map(String mapString) {
		this.mapString = load(mapString);
	}

	/**
	 * Loads the map.
	 * 
	 * @param file
	 *            The file to load the map from.
	 */
	private String load(File mapFile) {
		return MapLoader.loadMap(stations, startStations, initialTickets, mapFile);
	}

	/**
	 * Loads the map.
	 * 
	 * @param mapString
	 *            The string to load the map from.
	 */
	private String load(String mapString) {
		return MapLoader.loadMap(stations, startStations, initialTickets, mapString);
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
	 * @return The string that represents this map.
	 */
	public String getMapString() {
		return mapString;
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
}
