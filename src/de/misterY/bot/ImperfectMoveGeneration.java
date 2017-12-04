package de.misterY.bot;

import java.util.ArrayList;

import de.misterY.Player;
import de.misterY.Station;
import de.misterY.Link;

public class ImperfectMoveGeneration {
	private Station currentPosition;
	private Player LocalPlayer;
	private ArrayList<Station> PossiblePositions = new ArrayList<Station>();
	private Integer MoveMode = 0;
	
	public ImperfectMoveGeneration(Player pPlayer,Station station) {
		currentPosition = station;
		LocalPlayer = pPlayer;
	}
	
	/** Updates our current position
	 * @param station The new position
	 */
	public void updateOwnPosition(Station station) {
		currentPosition = station;
	}
	
	/** Updates the ResolvedPositions
	 * @param pos The Updated array
	 */
	public void updateResolvedPositions(ArrayList<Station> pos) {
		PossiblePositions = pos;
	}
	
	/**
	 * Analyses the scenario we are in and tries to find the best move to execute 
	 */
	public void doAnalysis() {
		if (PossiblePositions.size() <= 5) MoveMode = 1; //Not a lot of possible positions -> occupie one of them
		if (PossiblePositions.size() >= 6) MoveMode = 2; //Too many possible position -> move to the middle of all positions
		if (PossiblePositions.size() >= 6 && LinkedTrainStation() != null) MoveMode = 3; // Many Positions and we are close to a Trainstation -> lets occupie the trainstation for now
		//Implement more modes & Strategies
		if (PossiblePositions.size() >= 75) MoveMode = 0; // Too many Positions to do anything logical -> just take a taxi since they are the least rare
	}
	
	/**
	 * Runs whatever move was analysed to be the best in the current scenario
	 */
	public void RunMoveGeneration() {
		switch (MoveMode) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		}
	}
	
	/** Returns a train station that is directly linked to the current position or null if there isnt one
	 * @return
	 */
	public Station LinkedTrainStation() {
		ArrayList<Link> links = currentPosition.getLinks();
		ArrayList<Station> targets = new ArrayList<Station>();
		for (Link l : links) {
			targets.add(l.getStation());
		}
		for (Station s : targets) {
			if (s.isUnderground()) return s;
		}
		return null;
	}
}
