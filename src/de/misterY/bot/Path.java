package de.misterY.bot;

import java.util.ArrayList;

import de.misterY.Station;

public class Path {
	private ArrayList<Station> stationList = new ArrayList<Station>();

	public Path(ArrayList<Station> stations) {
		stationList = stations;
	}

	public Path() {
	}

	public void setPath(ArrayList<Station> pPath) {
		stationList = pPath;
	}

	/**
	 * Returns the next station on the path
	 * 
	 * @param current
	 * @return
	 */
	public Station getNextStation(Station current) {
		for (Station s : stationList) {
			if (s.equals(current)) {
				Station next = stationList.get(stationList.indexOf(s) + 1);
				if (next != null)
					return next;
			}
		}
		return null;
	}

	public void addStation(Station station) {
		stationList.add(station);
	}

	public ArrayList<Station> getStations() {
		return stationList;
	}

	public Station getLastStation() {
		return stationList.get(stationList.size() - 1);
	}
	
	public int getStationCount() {
		return stationList.size();
	}
}
