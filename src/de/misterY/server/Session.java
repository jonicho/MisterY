package de.misterY.server;

import java.util.ArrayList;

import de.misterY.Map;
import de.misterY.MeansOfTransportation;

public class Session {
	private Map map;
	private User mrY;
	private int turn = 0;
	private boolean isDoubleTurn;
	private boolean wasDoubleTurn;
	private boolean isActive = false;
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
	 * Checks whether all users are ready. If so, set isActive to true.
	 */
	public void checkReady() {
		int ready = 0;
		for (User user : users) {
			if (user.getPlayer().isReady()) {
				ready++;
			}
		}
		if (users.size() == ready) {
			this.isActive = true;
		}
	}

	/**
	 * Adds a user to a session
	 * 
	 * @param user
	 *            the user to add
	 * @return If the users could be added or not
	 */
	public boolean addUser(User user) {
		if (users.size() >= 6) {
			return false;
		}
		users.add(user);
		user.setInSession(true);
		return true;
	}

	/**
	 * Lets the given user make a movement to the given end station with the given
	 * means of transport if the movement is valid.
	 * 
	 * @return Whether the movement was successful
	 */
	public boolean doMovement(User user, int endId, MeansOfTransportation type) {
		if (!(user == getCurrentUser())) {
			return false;
		}
		boolean success = user.getPlayer().moveTo(map.getStationById(endId), type);
		if (success) {
			endTurn();
		}
		return success;
	}

	/**
	 * Returns all users of this session. Returned ArrayList is not the internal
	 * ArrayList
	 * 
	 * @return all users of this session
	 */
	public ArrayList<User> getAllUsers() {
		return (ArrayList<User>) users.clone();
	}

	public boolean isFull() {
		if (users.size() >= 6) {
			return true;
		}
		return false;
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
