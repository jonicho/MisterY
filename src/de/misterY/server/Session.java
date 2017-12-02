package de.misterY.server;

import java.util.ArrayList;

import de.misterY.Link;
import de.misterY.Map;
import de.misterY.MeansOfTransportation;
import de.misterY.Station;

public class Session {
	private Map map;
	private User mrY;
	private int turn = 0;
	private boolean isDoubleTurn;
	private boolean wasDoubleTurn;
	private ArrayList<User> users = new ArrayList<User>();

	/**
	 * Creates a new session with the given users
	 * 
	 * @param user
	 *            The users to add to the session
	 */
	public Session(User... user) {
		for (int i = 0; i < user.length; i++) {
			users.add(user[i]);
		}
	}

	public User getMrY() {
		return mrY;
	}

	/**
	 * Lets the given user make a movement to the given end station with the given
	 * means of transport if the movement is valid.
	 * 
	 * @return Whether the movement was successful
	 */
	public boolean doMovement(User user, Station end, MeansOfTransportation type) {
		return user.getPlayer().moveTo(end, type);
	}

	/**
	 * Returns all users of this session. Returned ArrayList is not the internal
	 * ArrayList
	 * 
	 * @return all users of this session
	 */
	public ArrayList<User> getAllUsers() {
		return users;
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
		for (User cUser : users) {
			if (cUser == user) {
				return true;
			}
		}
		return false;
	}

	public boolean wasDoubleTurn() {
		return wasDoubleTurn;
	}
}
