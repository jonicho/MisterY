package de.misterY.bot;

import java.util.ArrayList;

import de.misterY.Link;
import de.misterY.MeansOfTransportation;
import de.misterY.Station;

public class PositionResolver {

	private Station lastKnownPosition;
	private ArrayList<MeansOfTransportation> ticketHistory = new ArrayList<MeansOfTransportation>();
	private int blindTurns;
	private boolean isVisible;

	public PositionResolver() {
	}

	/**
	 * Initializes the Resolver
	 */
	public void initialize() {
		lastKnownPosition = null;
		ticketHistory.clear();
		blindTurns = 0;
	}

	/**
	 * Resolves the possible positions of MRY
	 */
	public ArrayList<Station> resolve(int precision) {
		// Make a new array for the output
		ArrayList<Station> result = new ArrayList<Station>();

		// if MRY is visible just return his pos
		if (isVisible) {
			result.add(lastKnownPosition);
			return result;
		} else {
			// initialize for resolving
			Station[][] layers = new Station[blindTurns][1024];
			layers[0][0] = lastKnownPosition;
			ArrayList<Station> layerResults = new ArrayList<Station>();
			ArrayList<Station> preDump = new ArrayList<Station>();
			if (layers[0][0] == null)
				return null;

			for (int i = 0; i < blindTurns; i++) {
				// Resolve the current layer
				for (int x = 0; x < 1024; x++) {
					if (layers[i][x] == null)
						break;
					preDump = getAllStations(layers[i][x], ticketHistory.get(i));
					for (Station s : preDump) {
						if (!layerResults.contains(s)) {
							layerResults.add(s);
						}
					}
				}
				// Dump the Results of this layer to the ArrayList
				for (int y = 0; y < layerResults.size(); y++) {
					layers[i + 1][y] = layerResults.get(y);
				}
				//Clear the dump
				layerResults.clear();
			}
			// We are done Resolving, lets convert
			for (int x = 0; x < 1024; x++) {
				if (layers[blindTurns][x] == null)
					break;
				if (!result.contains(layers[blindTurns][x])) {
					result.add(layers[blindTurns][x]);
				}
			}
			//Evaluate Results
			if (result.size() > precision) {
				return null;
			} else {
				return result;
			}
		}
	}

	/**
	 * Returns all stations that can be reached from a given station with a given
	 * ticket
	 * 
	 * @param pStation
	 *            The Station
	 * @param Ticket
	 *            The Ticket
	 * @return All Stations that are Reachable
	 */
	private ArrayList<Station> getAllStations(Station pStation, MeansOfTransportation Ticket) {
		ArrayList<Station> ret = new ArrayList<Station>();
		ArrayList<Link> temp = new ArrayList<Link>();
		temp = pStation.getLinks();
		for (Link l : temp) {
			if (l.isMeansOfTransportation(Ticket)) {
				ret.add(l.getStation());
			}
		}
		return ret;
	}

	/**
	 * Updates the Data Record of MRY
	 * 
	 * @param lastTicket
	 *            the last ticket MRY used
	 * @param lastStation
	 *            the last known station of MRY
	 */
	public void updateData(MeansOfTransportation lastTicket, Station lastStation) {
		if (lastStation != null) {
			isVisible = true;
			lastKnownPosition = lastStation;
		} else {
			isVisible = false;
		}
		if (isVisible) {
			ticketHistory.clear();
			blindTurns = 0;
		} else {
			ticketHistory.add(lastTicket);
			blindTurns++;
		}
	}
}
