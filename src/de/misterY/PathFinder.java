package de.misterY;

import java.util.ArrayList;

public class PathFinder {
	private PathFinder() {
	}

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
	 * Returns the possible means of transportation with which the end station can
	 * be reached from the start station.
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static MeansOfTransportation[] getPossibleMeansOfTransportation(Station start, Station end) {
		int count = 0;
		MeansOfTransportation[] values = MeansOfTransportation.values();
		MeansOfTransportation[] possibleMOT = new MeansOfTransportation[values.length];
		for (int i = 0; i < values.length; i++) {
			if (isReachable(start, end, values[i])) {
				possibleMOT[count] = values[i];
				count++;
			}
		}
		MeansOfTransportation[] result = new MeansOfTransportation[count];
		for (int i = 0; i < result.length; i++) {
			result[i] = possibleMOT[i];
		}
		return result;
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
		if (!start.isMeansOfTransportation(meansOfTransportation)) {
			return false;
		}
		return isReachable(start, end, meansOfTransportation, new ArrayList<Station>());
	}

	/**
	 * Returns whether the end station is reachable from the start station with the
	 * given means of transportation in one turn, not considering the given
	 * considered stations.
	 * 
	 * @param start
	 * @param end
	 * @param meansOfTransportation
	 * @param consideredStations
	 * @return
	 */
	private static boolean isReachable(Station start, Station end, MeansOfTransportation meansOfTransportation,
			ArrayList<Station> consideredStations) {
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
	 * Returns all Stations that are reachable in one step.
	 * 
	 * @param start
	 *            The station where the path starts
	 * @param considerStart
	 *            Whether the start station should be considered
	 * @return The path
	 */
	public static Station[] findPossibleStations(Station start) {
		ArrayList<Path> possiblePaths = new ArrayList<>();
		ArrayList<Station> consideredStations = new ArrayList<>();
		ArrayList<Station> resultStations = new ArrayList<>();
		consideredStations.add(start);
		possiblePaths.add(new Path(start));
		MeansOfTransportation mof = MeansOfTransportation.Taxi;
		while (true) {
			ArrayList<Path> toRemove = new ArrayList<>();
			ArrayList<Path> toAdd = new ArrayList<>();
			for (Path path : possiblePaths) {
				for (Link link : path.getLastStation().getLinks()) {
					Station station = link.getStation();
					if (!link.isMeansOfTransportation(mof) || consideredStations.contains(station))
						continue;
					consideredStations.add(station);
					if (!station.isMeansOfTransportation(mof)) {
						Path nextPath = path.getClone();
						nextPath.addStation(station);
						toAdd.add(nextPath);
					} else if (!resultStations.contains(station)){
						resultStations.add(station);
					}
				}
				toRemove.add(path);
			}
			possiblePaths.addAll(toAdd);
			possiblePaths.removeAll(toRemove);
			if (possiblePaths.isEmpty()) {
				switch (mof) {
				case Taxi:
					mof = start.isBus() ? MeansOfTransportation.Bus
							: (start.isUnderground() ? MeansOfTransportation.Underground : null);
					break;
				case Bus:
					mof = start.isUnderground() ? MeansOfTransportation.Underground : null;
					break;
				default:
					mof = null;
					break;
				}
				if (mof == null) {
					return resultStations.toArray(new Station[0]);
				}
			}
		}
	}
}
