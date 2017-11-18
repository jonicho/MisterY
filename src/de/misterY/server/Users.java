package de.misterY.server;

import java.util.ArrayList;

public class Users {
	private ArrayList<User> users = new ArrayList<User>();
	public Users() {
		users = new ArrayList<User>();
	}

	public void addUser(User user) {
		if (getUserByName(user.getPlayer().getName()) != null) {
			throw new IllegalArgumentException("User does already exist!");
		}
		users.add(user);
	}

	public void removeUser(User user) {
		users.remove(user);
	}

	/**
	 * Returns a user with the given adress.
	 * @param ip
	 * @param port
	 * @return
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
	 * @param name The name.
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
	
	public int getUserCount() {
		return users.size();
	}
}
