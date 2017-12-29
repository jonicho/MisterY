package de.misterY.pathfinding;

import java.util.ArrayList;

import de.misterY.Link;
import de.misterY.MeansOfTransportation;
import de.misterY.Station;

public class PathFinder {

	/**
	 * Tries to find the shortest path between the given start station and end
	 * station.<br>
	 * If there is more than one shortest path, the first one is returned.<br>
	 * Returns null if there is no path that connects the start station and the end
	 * station.
	 * 
	 * @param start
	 *            The station where the path starts
	 * @param end
	 *            The station where the path ends
	 * @return The path
	 */
	public static Path findPath(Station start, Station end) {
		if (start == end) {
			return new Path(start);
		}
		ArrayList<Path> possiblePaths = new ArrayList<>();
		ArrayList<Station> consideredStations = new ArrayList<>();
		consideredStations.add(start);
		possiblePaths.add(new Path(start));
		while (true) {
			ArrayList<Path> toRemove = new ArrayList<>();
			ArrayList<Path> toAdd = new ArrayList<>();
			for (Path path : possiblePaths) {
				for (Link link : path.getLastStation().getLinks()) {
					Station station = link.getStation();
					if (!consideredStations.contains(station)) {
						Path nextPath = path.getClone();
						nextPath.addStation(station);
						if (station == end) {
							return nextPath;
						}
						consideredStations.add(station);
						toAdd.add(nextPath);
					}
				}
				toRemove.add(path);
			}
			possiblePaths.addAll(toAdd);
			possiblePaths.removeAll(toRemove);
			if (possiblePaths.isEmpty()) {
				return null;
			}
		}
	}

	/**
	 * Returns whether the end station is reachable from the start station with the
	 * given means of transportation in one turn.
	 * 
	 * @param start
	 * @param end
	 * @param meansOfTransportation
	 * @return
	 */
	public static boolean isReachable(Station start, Station end, MeansOfTransportation meansOfTransportation) {
		return isReachable(start, end, meansOfTransportation, new ArrayList<Station>());
	}
	
	/**
	 * Returns whether the end station is reachable from the start station with the
	 * given means of transportation in one turn, not considering the given considered stations.
	 * 
	 * @param start
	 * @param end
	 * @param meansOfTransportation
	 * @param consideredStations
	 * @return
	 */
	private static boolean isReachable(Station start, Station end, MeansOfTransportation meansOfTransportation, ArrayList<Station> consideredStations) {
		System.out.println(start.getId());
		if (!end.isMeansOfTransportation(meansOfTransportation)) {
			return false;
		}
		Link link = start.getLink(end);
		if (link != null) {
			return link.isMeansOfTransportation(meansOfTransportation);
		}
		boolean result = false;
		for (Link l : start.getLinks()) {
			if (l.getStation() == start || !l.isMeansOfTransportation(meansOfTransportation)) {
				continue;
			}
			if (l.getStation().isMeansOfTransportation(meansOfTransportation)) {
				if (l.getStation() == end) {
					return true;
				} else {
					continue;
				}
			}
			if (consideredStations.contains(l.getStation())) {
				continue;
			}
			consideredStations.add(l.getStation());
			result = result || isReachable(l.getStation(), end, meansOfTransportation, consideredStations);
			if (result) {
				return true;
			}
		}
		return result;
		
	}

	/**
	 * Tries to find the shortest path to the nearest station of the given means of
	 * transportation.<br>
	 * If there is more than one shortest path to more than one station of the given
	 * means of transportation, the first one is returned.<br>
	 * Returns null if the nearest station of the given means of transportation is
	 * not reachable.
	 * 
	 * @param start
	 *            The station where the path starts
	 * @param type
	 *            The means of transportation of the end station
	 * @return The path
	 */
	public static Path findPathToNearestStation(Station start, MeansOfTransportation type) {
		if (start.isMeansOfTransportation(type)) {
			return new Path(start);
		}
		ArrayList<Path> possiblePaths = new ArrayList<>();
		ArrayList<Station> consideredStations = new ArrayList<>();
		consideredStations.add(start);
		possiblePaths.add(new Path(start));
		while (true) {
			ArrayList<Path> toRemove = new ArrayList<>();
			ArrayList<Path> toAdd = new ArrayList<>();
			for (Path path : possiblePaths) {
				for (Link link : path.getLastStation().getLinks()) {
					Station station = link.getStation();
					if (!consideredStations.contains(station)) {
						Path nextPath = path.getClone();
						nextPath.addStation(station);
						if (station.isMeansOfTransportation(type)) {
							return nextPath;
						}
						consideredStations.add(station);
						toAdd.add(nextPath);
					}
				}
				toRemove.add(path);
			}
			possiblePaths.addAll(toAdd);
			possiblePaths.removeAll(toRemove);
			if (possiblePaths.isEmpty()) {
				return null;
			}
		}
	}
}
