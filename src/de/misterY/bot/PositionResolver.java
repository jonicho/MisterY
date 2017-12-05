package de.misterY.bot;

import java.util.ArrayList;

import de.misterY.Link;
import de.misterY.MeansOfTransportation;
import de.misterY.Station;

public class PositionResolver {
	private Station lastStation;
	private ArrayList<MeansOfTransportation> ticketRecord = new ArrayList<MeansOfTransportation>();
	private Station[][] resolvedLayers = new Station[5][200];
	private Integer currentLayer;

	public PositionResolver(Station pStation) {
		currentLayer = 0;
		lastStation = pStation;
		resolvedLayers[0][0] = lastStation;
	}

	/**
	 * Resolves the possible positions of MRY
	 */
	private void resolve() {
		// Increase the layer and clear the last results
		currentLayer++;
		for (int y = 0; y < 5; y++) {
			for (int z = 0; z < 200; z++) {
				resolvedLayers[y][z] = null;
			}
		}
		// Get links
		for (int i = 0; i < currentLayer + 1; i++) {
			ArrayList<Link> possibleLinks = new ArrayList<Link>();
			ArrayList<Station> layerStations = new ArrayList<Station>();
			for (int x = 0; x < 200; x++) {
				if (resolvedLayers[i - 1][x] != null) {
					possibleLinks.addAll(resolvedLayers[currentLayer - 1][x].getLinks());
				}
			}
			// Trace
			for (Link l : possibleLinks) {
				Station target = l.getStation();
				if (!layerStations.contains(target)) {
					layerStations.add(target);
				}
			}
			// Output
			for (int k = 0; k < layerStations.size(); k++) {
				Station temp = layerStations.get(k);
				resolvedLayers[i][k] = temp;
				layerStations.remove(temp);
			}
		}
	}

	/**
	 * Feeds the resolver the last ticket used by MRY
	 * 
	 * @param ticket
	 */
	public void feedTicketUpdate(MeansOfTransportation ticket) {
		ticketRecord.add(ticket);
		resolve();
	}

	/**
	 * Feeds the resolver a new position once MRY has become visible
	 * 
	 * @param pStation
	 */
	public void feedPositionUpdate(Station pStation) {
		lastStation = pStation;
		ticketRecord.clear();
	}

	/**
	 * Returns the resolved positions
	 * 
	 * @return
	 */
	public ArrayList<Station> getResolvedStations() {
		ArrayList<Station> ret = new ArrayList<Station>();
		for (int i = 0; i < 200; i++) {
			if (resolvedLayers[currentLayer][i] != null) {
				ret.add(resolvedLayers[currentLayer][i]);
			}
		}
		return ret;
	}
}
