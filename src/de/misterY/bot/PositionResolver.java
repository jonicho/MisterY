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
			ArrayList<Link> PossibleLinks = new ArrayList<Link>();
			ArrayList<Station> LayerStations = new ArrayList<Station>();
			for (int x = 0; x < 200; x++) {
				if (resolvedLayers[i - 1][x] != null) {
					PossibleLinks.addAll(resolvedLayers[currentLayer - 1][x].getLinks());
				}
			}
			// Trace
			for (Link l : PossibleLinks) {
				Station target = l.getStation();
				if (!LayerStations.contains(target)) {
					LayerStations.add(target);
				}
			}
			// Output
			for (int k = 0; k < LayerStations.size(); k++) {
				Station temp = LayerStations.get(k);
				resolvedLayers[i][k] = temp;
				LayerStations.remove(temp);
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
