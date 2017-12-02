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

	public void removeSession(Session s) {
		sessions.remove(s);
	}

	/**
	 * Returns the session containing the given user or null if no session contains
	 * the user.
	 * 
	 * @param user
	 *            The user
	 * @return The session containing the given user or null if no session contains
	 *         the user.
	 */
	public Session getSessionByUser(User user) {
		for (Session session : sessions) {
			if (session.doesContain(user)) {
				return session;
			}
		}
		return null;
	}

	public int getSessionCount() {
		return sessions.size();
	}

	public ArrayList<Session> getSessionList() {
		return sessions;
	}
}
