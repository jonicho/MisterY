package de.misterY;

import java.util.ArrayList;
import java.util.Random;

public class MapGenerator {
	public static Map generateMap(long seed, int players) {
		Random rand = new Random(seed);
		double min = 0.75;
		double max = 1.25;
		double mult = min + rand.nextDouble() * (max - min);
		int stationAmount = (int) (players * 35 * mult);
		int rounds = (int) (players * 4.5 * mult);
		ArrayList<Station> stations = new ArrayList<Station>();
		for (int i = 0; i < stationAmount; i++) {
			Station station = new Station(false, false, i + 1, new Vector2D(rand.nextDouble(), rand.nextDouble()));
			stations.add(station);
		}
		
		for (Station station : stations) {
			Station nearestStation = null;
			double shortestDistance = Double.MAX_VALUE;
			for (Station s : stations) {
				double d = s.getPos().getDistance(station.getPos());
				if (d < shortestDistance && s != station) {
					nearestStation = s;
					shortestDistance = d;
				}
			}
			station.getPos().add(station.getPos().getClone().subtract(nearestStation.getPos()).multiply(1/shortestDistance/1/shortestDistance));
		}
		ArrayList<Station> startStations = new ArrayList<Station>();
		for (int i = 0; i < stationAmount * 0.09; i++) {
			Station startStation = stations.get((int) (stationAmount * rand.nextDouble()));
			if (startStations.contains(startStation)) {
				i--;
			} else {
				startStations.add(startStation);
			}
		}
		return new Map(stations, startStations, new int[]{}, rounds, new int[]{}, "");
	}
}
