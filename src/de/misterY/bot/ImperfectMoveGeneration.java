package de.misterY.bot;

import java.util.ArrayList;

import de.misterY.Link;
import de.misterY.Player;
import de.misterY.Station;

public class ImperfectMoveGeneration {
	private Station currentPosition;
	private Player localPlayer;
	private ArrayList<Station> possiblePositions = new ArrayList<Station>();
	private Integer moveMode = 0;

	public ImperfectMoveGeneration(Player pPlayer, Station station) {
		currentPosition = station;
		localPlayer = pPlayer;
	}

	/**
	 * Updates our current position
	 * 
	 * @param station
	 *            The new position
	 */
	public void updateOwnPosition(Station station) {
		currentPosition = station;
	}

	/**
	 * Updates the resolved positions
	 * 
	 * @param pos
	 *            The updated array
	 */
	public void updateResolvedPositions(ArrayList<Station> pos) {
		possiblePositions = pos;
	}

	/**
	 * Analyzes the scenario we are in and tries to find the best move to execute
	 */
	public void doAnalysis() {
		if (possiblePositions.size() <= 5)
			moveMode = 1; // Not a lot of possible positions -> occupy one of them
		if (possiblePositions.size() >= 6)
			moveMode = 2; // Too many possible position -> move to the middle of all positions
		if (possiblePositions.size() >= 6 && linkedUndergroundStation() != null)
			moveMode = 3; // Many Positions and we are close to a underground station -> lets occupy the
							// underground station for now
		// Implement more modes & Strategies
		if (possiblePositions.size() >= 75)
			moveMode = 0; // Too many Positions to do anything logical -> just take a taxi since they are
							// the least rare
	}

	/**
	 * Runs whatever move was analyzed to be the best in the current scenario
	 */
	public void runMoveGeneration() {
		switch (moveMode) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		}
	}

	/**
	 * Returns a train station that is directly linked to the current position or
	 * null if there isn't one
	 * 
	 * @return
	 */
	public Station linkedUndergroundStation() {
		ArrayList<Link> links = currentPosition.getLinks();
		ArrayList<Station> targets = new ArrayList<Station>();
		for (Link l : links) {
			targets.add(l.getStation());
		}
		for (Station s : targets) {
			if (s.isUnderground())
				return s;
		}
		return null;
	}
}
