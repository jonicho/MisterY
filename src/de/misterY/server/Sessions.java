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
	
	/** Gets Session with specified ip
	 * @param ip the ip to get the Session for
	 * @return the Session
	 */
	public Session getSessionByIP(String ip) {
		
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
