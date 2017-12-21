package de.misterY.pathfinding;

import java.util.ArrayList;

import de.misterY.Station;

public class Path {
	private ArrayList<Station> stations = new ArrayList<>();

	/**
	 * Constructs a path with the given stations as initial stations.
	 * 
	 * @param stations
	 */
	public Path(Station... stations) {
		for (int i = 0; i < stations.length; i++) {
			this.stations.add(stations[i]);
		}
	}

	/**
	 * Adds a station to the path
	 * 
	 * @param station
	 */
	public void addStation(Station station) {
		stations.add(station);
	}

	/**
	 * Appends the given path to this path
	 * 
	 * @param path
	 */
	public void appendPath(Path path) {
		stations.addAll(path.getStations());
	}

	public ArrayList<Station> getStations() {
		return stations;
	}

	public Station getLastStation() {
		return stations.get(stations.size() - 1);
	}

	/**
	 * Returns the station followed by the given one.<br>
	 * Returns null if there is no following station (the given station is the last
	 * one).
	 * 
	 * @param station
	 * @return
	 */
	public Station getFollowingStation(Station station) {
		if (getLastStation() == station) {
			return null;
		}
		return stations.get(stations.indexOf(station) + 1);
	}

	public Path getClone() {
		return (Path) clone();
	}

	@Override
	protected Object clone() {
		return new Path(stations.toArray(new Station[stations.size()]));
	}
}
