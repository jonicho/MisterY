package de.misterY.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import de.misterY.Map;
import de.misterY.MeansOfTransportation;

public class Session {
	private Map map;
	private int currentUserIndex = 0;
	private boolean isDoubleTurn;
	private boolean wasDoubleTurn;
	private boolean isActive = false;
	private boolean gameStarted = false;
	private int round = 1;
	private ArrayList<User> users = new ArrayList<User>();
	private ArrayList<MeansOfTransportation> ticketsUsedByMisterY = new ArrayList<MeansOfTransportation>();
	private User winner = null;
	private final int id;

	/**
	 * Creates a new session with the given users
	 * 
	 * @param user
	 *            The users to add to the session
	 */
	public Session(int id, User... user) {
		this.id = id;
		for (int i = 0; i < user.length; i++) {
			users.add(user[i]);
		}
	}

	public User getMrY() {
		for (User user : users) {
			if (user.getPlayer().isMrY()) {
				return user;
			}
		}
		return null;
	}

	public int getRound() {
		return round;
	}

	public int getId() {
		return id;
	}

	/**
	 * @return Whether misterY is showing.
	 */
	public boolean isMisterYShowing() {
		return Arrays.binarySearch(map.getShowRounds(), round) >= 0;
	}

	/**
	 * Returns the user whose turn it is.
	 * 
	 * @return the user whose turn it is.
	 */
	public User getCurrentUser() {
		return users.get(currentUserIndex);
	}

	public MeansOfTransportation[] getTicketsUsedByMisterY() {
		return ticketsUsedByMisterY.toArray(new MeansOfTransportation[ticketsUsedByMisterY.size()]);
	}

	public Map getMap() {
		return map;
	}

	public User getWinner() {
		return winner;
	}

	/**
	 * Checks whether all users are ready. If so, sets isActive to true.
	 */
	public void checkReady() {
//		if (users.size() < 3) {
//			return;
//		}

		if (isActive) {
			return;
		}
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
	 * Prepares the game by doing the following things:<br>
	 * -shuffle user list<br>
	 * -make user 0 to misterY<br>
	 * -call start() on every player
	 * 
	 * @param map
	 *            The map
	 */
	public void prepareGame(Map map) {
		if (gameStarted) {
			return;
		}
		Collections.shuffle(users);
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getPlayer().getName().startsWith("BOT")) {
				continue;
			}
			users.get(i).getPlayer().setMrY(true);
			User temp = users.get(i);
			users.set(i, users.get(0));
			users.set(0, temp);
			break;
		}
		for (User user : users) {
			user.getPlayer().start(map, map.getInitialTickets(MeansOfTransportation.Taxi, user.getPlayer().isMrY()),
					map.getInitialTickets(MeansOfTransportation.Bus, user.getPlayer().isMrY()),
					map.getInitialTickets(MeansOfTransportation.Underground, user.getPlayer().isMrY()));
		}
		this.map = map;
		gameStarted = true;
	}

	/**
	 * Adds a user to a session
	 * 
	 * @param user
	 *            the user to add
	 * @return If the users could be added or not
	 */
	public boolean addUser(User user) {
		if (isActive) {
			return false;
		}
		if (users.size() >= 6) {
			return false;
		}
		users.add(user);
		user.setInSession(true);
		return true;
	}

	/**
	 * Removes the given user from session.
	 * 
	 * @param user
	 *            The user to remove
	 * @return Whether the session is empty after the given user was removed.
	 */
	public boolean removeUser(User user) {
		users.remove(user);
		return users.isEmpty();
	}

	/**
	 * Lets the given user make a movement to the given end station with the given
	 * means of transport if the movement is valid.
	 * 
	 * @return Whether the movement was successful
	 */
	public boolean doMovement(User user, int endId, MeansOfTransportation type) {
		if (user != getCurrentUser()) {
			return false;
		}
		boolean success = user.getPlayer().moveTo(map.getStationById(endId), type);
		if (success) {
			if (user != getMrY()) {
				getMrY().getPlayer().addTicket(type);
			} else {
				ticketsUsedByMisterY.add(type);
			}
			if (!user.getPlayer().isMrY() && user.getPlayer().getCurrentStation().getId() == getMrY().getPlayer()
					.getCurrentStation().getId()) {
				winner = user;
			}
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
	@SuppressWarnings("unchecked")
	public ArrayList<User> getAllUsers() {
		return (ArrayList<User>) users.clone();
	}

	public boolean isFull() {
		return users.size() >= 6;
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
			currentUserIndex++;
			if (currentUserIndex > users.size() - 1) {
				currentUserIndex = 0;
				round++;
			}
			wasDoubleTurn = false;
		}
		if (isDoubleTurn) {
			isDoubleTurn = false;
			wasDoubleTurn = true;
		}
		if (round > map.getRounds()) {
			winner = getMrY();
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

	public boolean isActive() {
		return isActive;
	}
}
