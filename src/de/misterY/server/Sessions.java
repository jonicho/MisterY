package de.misterY.server;

import java.util.ArrayList;

public class Sessions {
	private ArrayList<Session> sessions;

	public Sessions() {
		sessions = new ArrayList<Session>();
	}

	public void addSession(Session s) {
		sessions.add(s);
	}

	public Session getSessionWithUser(User user) {
		for (Session session : sessions) {
			if (session.doesContain(user)) {
				return session;
			}
		}
		return null;
	}

	/**
	 * Returns Session which contains user with given ip and port
	 * 
	 * @param ip
	 *            the user's ip
	 * @param port
	 *            the user's port
	 * @return the Session
	 */
	public Session getSessionByAdress(String ip, int port) {
		return null;
	}

	public void removeSession(Session s) {
		sessions.remove(s);
	}

	public int getSessionCount() {
		return sessions.size();
	}

	public ArrayList<Session> getSessionList() {
		return sessions;
	}

	public Session getFirst() {
		return sessions.get(0);
	}
}
