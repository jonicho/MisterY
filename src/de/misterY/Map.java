package de.misterY;

import java.io.File;
import java.util.ArrayList;

public class Map {
	private ArrayList<Station> stations;

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
	 * Loads the map.
	 * 
	 * @param file
	 *            The file to load the map from.
	 */
	private void load(File file) {

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
		return null;
	}
}
