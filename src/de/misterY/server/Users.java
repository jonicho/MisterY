package de.misterY.server;

import java.util.ArrayList;

public class Users {
	private ArrayList<User> users = new ArrayList<User>();

	public Users() {
		users = new ArrayList<User>();
	}

	/**
	 * Adds the given user.
	 * 
	 * @param user
	 *            The user to add
	 * @throws IllegalArgumentException
	 *             if this user or an user with this name does already exist
	 */
	public void addUser(User user) throws IllegalArgumentException {
		if (users.contains(user)) {
			throw new IllegalArgumentException("User does already exist!");
		}
		if (getUserByName(user.getPlayer().getName()) != null) {
			throw new IllegalArgumentException("A user with this name does already exist!");
		}
		users.add(user);
	}

	public void removeUser(User user) {
		users.remove(user);
	}

	/**
	 * Returns a user with the given ip and port.
	 * 
	 * @param ip
	 *            The ip
	 * @param port
	 *            The port
	 * @return a user with the given ip and port.
	 */
	public User getUserByAdress(String ip, int port) {
		for (User user : users) {
			if (user.getIp().equals(ip) && user.getPort() == port) {
				return user;
			}
		}
		return null;
	}

	/**
	 * Returns a user with the given name.
	 * 
	 * @param name
	 *            The name.
	 * @return The user.
	 */
	public User getUserByName(String name) {
		for (User user : users) {
			if (user.getPlayer().getName().equals(name)) {
				return user;
			}
		}
		return null;
	}

	/**
	 * Checks if a username is already taken
	 * 
	 * @param the
	 *            username to check
	 * 
	 * @return true if the username is already taken, false if it is not
	 */
	public boolean isNameTaken(String name) {
		for (User user : users) {
			if (user.getPlayer().getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public int getUserCount() {
		return users.size();
	}
}
