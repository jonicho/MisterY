package de.misterY.pathfinding;

import java.util.ArrayList;

import de.misterY.Link;
import de.misterY.Station;

public class PathFinding {

	private PathFinding() {}

	/**
	 * Finds the fastest path between two stations
	 * 
	 * @param start
	 *            The first station
	 * @param end
	 *            The second station
	 * @return the Path as a Path object
	 */
	public static Path findPath(Station start, Station end) {
		ArrayList<Path> paths = new ArrayList<Path>();
		ArrayList<Station> traceStations = new ArrayList<Station>();
		Path initial = new Path();
		initial.addStation(start);
		paths.add(initial);
		traceStations.add(start);
		while (true) {
			for (Station s : traceStations) {
				ArrayList<Link> clinks = new ArrayList<Link>();
				clinks = s.getLinks();
				Path origin = new Path();
				for (Path p : paths) {
					if (p.getLastStation().equals(s)) {
						origin = p;
						break;
					} else {
						return null;
					}
				}
				for (Link l : clinks) {
					Path cpath = new Path(origin.getStations());
					cpath.addStation(l.getStation());
					if (!traceStations.contains(l.getStation())) {
						traceStations.add(l.getStation());
					}
					if (l.getStation().equals(end)) {
						return cpath;
					} else {
						paths.add(cpath);
					}
				}
				traceStations.remove(s);
			}
			if (traceStations.size() == 0) {
				return null;
			}
		}
	}
	
	/** Finds the fastest path
	 * @param start The current position
	 * @return The Path to the Trainstation as a Path object
	 */
	public static Path findFirstTrainStation(Station start) {
		ArrayList<Path> paths = new ArrayList<Path>();
		ArrayList<Station> traceStations = new ArrayList<Station>();
		Path initial = new Path();
		initial.addStation(start);
		paths.add(initial);
		traceStations.add(start);
		while (true) {
			for (Station s : traceStations) {
				ArrayList<Link> clinks = new ArrayList<Link>();
				clinks = s.getLinks();
				Path origin = new Path();
				for (Path p : paths) {
					if (p.getLastStation().equals(s)) {
						origin = p;
						break;
					} else {
						return null;
					}
				}
				for (Link l : clinks) {
					Path cpath = new Path(origin.getStations());
					cpath.addStation(l.getStation());
					if (!traceStations.contains(l.getStation())) {
						traceStations.add(l.getStation());
					}
					if (l.getStation().isUnderground()) {
						return cpath;
					} else {
						paths.add(cpath);
					}
				}
				traceStations.remove(s);
			}
			if (traceStations.size() == 0) {
				return null;
			}
		}
	}

	/**
	 * Finds the distance between the specified stations in turns required to move
	 * there
	 * 
	 * @param start
	 *            The First station
	 * @param end
	 *            The second station
	 * @return The distance
	 */
	public static int getDistance(Station start, Station end) {
		return findPath(start, end).getStationCount() - 1;
	}
}
