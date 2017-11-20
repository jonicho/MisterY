package de.misterY.server;

import java.util.ArrayList;

import de.misterY.Map;
import de.misterY.MeansOfTransportation;
import de.misterY.Station;

public class Session {
	private Map map;
	private User mrY;
	private int turn = 0;
	private boolean isDoubleTurn;
	private boolean wasDoubleTurn;
	private ArrayList<User> users;

	/**
	 * Creates a new session with the given users
	 * 
	 * @param user
	 *            The users to add to the session
	 */
	public Session(User... user) {
		users = new ArrayList<User>();
	}

	public User getMrY() {
		return mrY;
	}

	/**
	 * Validates User movement by checking if a movement from the specified starting
	 * point to the specified end point is possible, also considering if the user
	 * has enough tickets
	 * 
	 * @param start
	 *            The Starting Point
	 * @param type
	 *            The mean of transportation
	 * @param end
	 *            The end Point
	 * @return True if movement is valid false if it is not
	 */
	public boolean validateMovement(Station start, MeansOfTransportation type, Station end, User user) {
		return false;
	}

	/**
	 * @return Returns all ips from all the users in the session
	 */
	public ArrayList<String> getAllips() {
		return null;
	}

	/**
	 * Return all the ticket information of a specified user Format is specified in
	 * PROTOCOL
	 * 
	 * @param user
	 *            The user to get the info from
	 * @return the String
	 */
	public String getInfoString(User user) {
		return null;
	}

	public boolean doDoubleTurn() {
		if (wasDoubleTurn) {
			return false;
		} else {
			isDoubleTurn = true;
			return true;
		}
	}

	/**
	 * Is called when a turn ends
	 */
	public void endTurn() {
		if (!isDoubleTurn) {
			turn++;
			wasDoubleTurn = false;
		}
		if (isDoubleTurn) {
			isDoubleTurn = false;
			wasDoubleTurn = true;
		}
	}

	/**
	 * Returns whether this session contains the given user
	 * 
	 * @param user
	 *            The user to check
	 * @return true if this session contains the given user, false otherwise
	 */
	public boolean doesContain(User user) {
		return false;
	}

	public boolean wasDoubleTurn() {
		return wasDoubleTurn;
	}
}
