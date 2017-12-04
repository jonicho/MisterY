package de.misterY.bot;

import java.util.ArrayList;
import de.misterY.MeansOfTransportation;
import de.misterY.Station;
import de.misterY.Link;

public class PositionResolver {
	private Station lastStation;
	private ArrayList<MeansOfTransportation> TicketRecord = new ArrayList<MeansOfTransportation>();
	private Station[][] ResolvedLayers = new Station[5][200];
	private Integer currentLayer;
	
	public PositionResolver(Station pStation) {
		currentLayer = 0;
		lastStation = pStation;
		ResolvedLayers[0][0] = lastStation;
	}
	
	
	/**
	 * Resolves the Possible Positions of MRY
	 */
	private void resolve() {
		//Increase the Layer and Clear the Last Results
		currentLayer++;
		for (int y = 0; y < 5; y++) {
			for (int z = 0; z < 200; z++) {
				ResolvedLayers[y][z] = null;
			}
		}
		//Get Links
		for (int i = 0; i < currentLayer+1; i++) {
			ArrayList<Link> PossibleLinks = new ArrayList<Link>();
			ArrayList<Station> LayerStations = new ArrayList<Station>();
			for (int x = 0; x < 200; x++) {
				if (ResolvedLayers[i-1][x] != null) {
					PossibleLinks.addAll(ResolvedLayers[currentLayer-1][x].getLinks());
				}
			}
			//Trace
			for (Link l : PossibleLinks) {
				Station target = l.getStation();
				if (!LayerStations.contains(target)) {
					LayerStations.add(target);
				}
			}
			//Output
			for (int k = 0; k < LayerStations.size(); k++) {
				Station temp = LayerStations.get(k);
				ResolvedLayers[i][k] = temp;
				LayerStations.remove(temp);
			}
		}
	}
	
	
	/** Feeds the Resolver the last ticket used by MRY
	 * @param ticket
	 */
	public void feedTicketUpdate(MeansOfTransportation ticket) {
		TicketRecord.add(ticket);
		resolve();
	}
	
	
	/** Feeds the Resolver a new position once MRY has become visible
	 * @param pStation
	 */
	public void feedPositionUpdate(Station pStation) {
		lastStation = pStation;
		TicketRecord.clear();
	}
	
	
	/** Returns the Resolved Positions
	 * @return
	 */
	public ArrayList<Station> getResolvedStations() {
		ArrayList<Station> ret = new ArrayList<Station>();
		for (int i = 0; i < 200; i++) {
			if (ResolvedLayers[currentLayer][i] != null) {
				ret.add(ResolvedLayers[currentLayer][i]);
			}
		}
		return ret;
	}
}
