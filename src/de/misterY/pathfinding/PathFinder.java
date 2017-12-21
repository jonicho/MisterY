package de.misterY.pathfinding;

import java.util.ArrayList;

import de.misterY.Link;
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
		while(true) {
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
}
