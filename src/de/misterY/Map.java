package de.misterY;

import java.io.File;
import java.util.ArrayList;

public class Map {
	private ArrayList<Station> stations;
	private ArrayList<Station> startStations;

	/**
	 * Constructs a new map from the given map file.
	 * 
	 * @param file
	 *            The file to load the map from.
	 */
	public Map(File file) {
		load(file);
	}

	/**
	 * Constructs a new map from the given map string.
	 * 
	 * @param mapString
	 *            The string to load the map from.
	 */
	public Map(String mapString) {
		load(mapString);
	}

	/**
	 * Loads the map.
	 * 
	 * @param file
	 *            The file to load the map from.
	 */
	private void load(File file) {
		String mapString = "";
		//load string from file
		load(mapString);
	}

	/**
	 * Loads the map.
	 * 
	 * @param mapString
	 *            The string to load the map from.
	 */
	private void load(String mapString) {

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
}
