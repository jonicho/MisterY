package de.misterY.server;

import de.misterY.Station;
import de.misterY.net.Server;

public class GameServer extends Server {
	
	private Sessions sessions;
	private Users users;

	public GameServer(int pPort) {
		super(pPort);
	}

	@Override
	public void processNewConnection(String pClientIP, int pClientPort) {
		// No double Names
	}

	@Override
	public void processMessage(String pClientIP, int pClientPort, String pMessage) {
		
	}

	@Override
	public void processClosingConnection(String pClientIP, int pClientPort) {
		// Send Errorcode
	}
	
	
	/** Sends a Server Message to all clients in the Specified Session 
	 * @param msg The Message to send
	 * @param session The Session to send it to
	 */
	public void sendToSession(String msg, Session session) {
		
	}
	
	/** The method is called after the movement was verified to actually do the movement
	 * @param ip the ip of the user to move
	 * @param end the station to move the user to
	 */
	public void doMovement(String ip, Station end) {
		//Get User By IP
		//Get session with user
		//move
		//end turn
	}
	
	
	
	/** Sends an info update for the specified player to the asking user 
	 * @param name the name of the user to get the update from
	 * @param askingUser the user the update will be sent to
	 */
	public void sendInfoUpdate(String name, User askingUser) {
		
	}
	
	
}
