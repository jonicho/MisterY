package de.misterY.bot;

import java.util.ArrayList;

import de.misterY.MeansOfTransportation;
import de.misterY.Player;
import de.misterY.Station;

public class AI {
	private ArrayList<Station> resolvedPositions = new ArrayList<Station>();
	private ArrayList<Station> predictedPositions = new ArrayList<Station>();
	private ArrayList<MeansOfTransportation> ticketRecordMRY = new ArrayList<MeansOfTransportation>();
	private PositionResolver resolver;
	private PositionPredicter predicter;
	private Station lastMRYStation;
	private Player localPlayer;
	private Integer moveState;
	private Player mryHandle;

	public AI() {
	}

	public void initialize(Station pStation, Player pPlayer) {
		resolver = new PositionResolver(pStation);
		predicter = new PositionPredicter();
		localPlayer = pPlayer;
		lastMRYStation = pStation;
		moveState = 0;
	}

	public void updateData(Station pStation, MeansOfTransportation ticket) {
		lastMRYStation = pStation;
		ticketRecordMRY.add(ticket);
		resolver.feedPositionUpdate(pStation);
		resolver.feedTicketUpdate(ticket);
		resolvedPositions = resolver.getResolvedStations();
		if (resolvedPositions.size() == 1) {
			predictedPositions = predicter.getAllPredictions(resolvedPositions.get(0), mryHandle);
		}
	}

	public void doAnalysis() {

	}

	public void generateMove() {
		switch (moveState) {
		case 0: //Nothing usefull todo, just go in a random direction
			break; 
		case 1: //Go to Definitive Position
			break;
		case 2: //Go to Predcted Position
			break;
		case 3: // Go in the middle of multiple Resolved Positions
			break;
		case 4: //  
			break;
		}
	}
}
