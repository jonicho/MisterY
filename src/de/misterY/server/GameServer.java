package de.misterY.server;

import de.misterY.net.PROTOCOL;
import de.misterY.net.Server;

public class GameServer extends Server {

	private Sessions sessions;
	private Users users;

	public GameServer(int port) {
		super(port);
	}

	@Override
	public void processNewConnection(String clientIP, int clientPort) {
		// No double Names
	}

	@Override
	public void processMessage(String clientIP, int clientPort, String message) {
		User user = users.getUserByAdress(clientIP, clientPort);
		String[] msgParts = message.split(PROTOCOL.SPLIT);
		
		switch (msgParts[0]) {
		case PROTOCOL.CS.LOGIN:
			
			break;
		case PROTOCOL.CS.REQUEST_MOVEMENT:
			
			break;
		case PROTOCOL.CS.CHAT_POST:
			
			break;
		case PROTOCOL.CS.REQUEST_INFO:
			
			break;
		case PROTOCOL.CS.REQUEST_BOT:
			
			break;

		default:
			sendToUser(PROTOCOL.buildMessage(PROTOCOL.SC.ERROR, String.valueOf(PROTOCOL.ERRORCODES.INVALID_MESSAGE)), user);
			break;
		}
	}

	@Override
	public void processClosingConnection(String clientIP, int clientPort) {
		// Send Errorcode
	}
	
	/**
	 * Sends a server message to the given user
	 * 
	 * @param msg
	 *            The message to send
	 * @param user
	 *            The user to send the message to
	 */
	private void sendToUser(String msg, User user) {
		send(user.getIp(), user.getPort(), msg);
	}

	/**
	 * Sends a server message to all clients in the given session
	 * 
	 * @param msg
	 *            The message to send
	 * @param session
	 *            The session to send the message to
	 */
	private void sendToSession(String msg, Session session) {
		for(User user : session.getAllUsers()) {
			sendToUser(msg, user);
		}
	}

	/**
	 * Sends an info update for the specified player to the asking user
	 * 
	 * @param name
	 *            the name of the user to get the update from
	 * @param askingUser
	 *            the user the update will be sent to
	 */
	private void sendInfoUpdate(String name, User askingUser) {

	}
}
