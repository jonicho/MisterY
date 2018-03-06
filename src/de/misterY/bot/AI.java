package de.misterY.bot;

import java.util.ArrayList;

import de.misterY.Map;
import de.misterY.MeansOfTransportation;
import de.misterY.PathFinder;
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
	private boolean isChasing = false;
	private int RESOLVER_PRECISION = 4;
	private int targetID;

	/**
	 * Initializes the AI
	 * 
	 * @param pStation
	 *            Our Starting position
	 * @param pPlayer
	 *            a Handle to the local player
	 * @param pMap
	 *            The map we are playing on
	 */
	public void initialize(Station pStation, Player pPlayer) {
		resolver = new PositionResolver();
		predicter = new PositionPredicter();
		localPlayer = pPlayer;
		lastMRYStation = pStation;
		moveState = 0;
	}

	/**
	 * This method is called after as soon as MRY has made a turn to update our data
	 * on him
	 * 
	 * @param pStation
	 *            The position where MRY was last seen
	 * @param ticket
	 *            The Ticket MRY last used
	 */
	public void updateData(Station pStation, MeansOfTransportation ticket) {
		if (resolvedPositions.size() == 1 && localPlayer.getCurrentStation().equals(resolvedPositions.get(0))
				|| localPlayer.getCurrentStation().equals(lastMRYStation)) {
			isChasing = true;
		} else {
			isChasing = false;
		}
		lastMRYStation = pStation;
		ticketRecordMRY.add(ticket);
		resolver.updateData(ticket, pStation);
		resolvedPositions = resolver.resolve(RESOLVER_PRECISION);
		if (resolvedPositions.size() == 1) {
			predictedPositions = predicter.getAllPredictions(resolvedPositions.get(0), mryHandle);
		}

	}

	/**
	 * Analyses the Situation and tries to find the best moveState
	 * 
	 */
	public void doAnalysis() {
		// Check everything in order of importance
		if (resolvedPositions.size() == 1 && predictedPositions.size() == 1) {
			moveState = 2;
			return;
		}
		if (resolvedPositions.size() == 1 ) {
			moveState = 1;
			return;
		}
		if (isChasing) {
			moveState = 5;
			return;
		}

		else {
			moveState = 0;
		}

	}
	
	public void MoveExecute() {
		switch (moveState) {
		case 0: // Nothing usefull todo, just go in a random direction
			targetID = -1;
			break;
		case 1: // Go to Definitive Resolved Position
			targetID = resolvedPositions.get(0).getId();
			break;
		case 2: // Go to Definitive Predicted Position
			targetID = predictedPositions.get(0).getId();
			break;
		case 5: // We are chasing MRY & are one turn behind him, pick a link that matches his
				// ticket to maybe get him
			targetID = -5;
			break;
		}
	}
	
	public int getTarget() {
		return targetID;
	}
}
