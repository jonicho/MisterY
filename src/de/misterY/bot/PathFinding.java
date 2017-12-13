package de.misterY.bot;

import java.util.ArrayList;

import de.misterY.Link;
import de.misterY.Map;
import de.misterY.Station;

public class PathFinding {
	private Map map;

	public PathFinding(Map pMap) {
		map = pMap;
	}

	public Path findPath(Station start, Station end) {
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
}
