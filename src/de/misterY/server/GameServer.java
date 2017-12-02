package de.misterY.server;

import de.misterY.MeansOfTransportation;
import de.misterY.net.PROTOCOL;
import de.misterY.net.Server;

public class GameServer extends Server {

	private Sessions sessions = new Sessions();
	private Users users = new Users();

	public static void main(String[] args) {
		new GameServer(PROTOCOL.PORT);
	}

	public GameServer(int port) {
		super(port);
	}

	@Override
	public void processNewConnection(String clientIP, int clientPort) {

	}

	@Override
	public void processMessage(String clientIP, int clientPort, String message) {
		User user = users.getUserByAdress(clientIP, clientPort);
		String[] msgParts = message.split(PROTOCOL.SPLIT);
		if (user == null) {
			user = new User(clientIP, clientPort, "tmpuser" + clientIP + ":" + clientPort);
			if (!msgParts[0].equals(PROTOCOL.CS.LOGIN)) {
				sendToUser(PROTOCOL.getErrorMessage(PROTOCOL.ERRORCODES.NOT_LOGGED_IN), user);
				return;
			}
		}
		if (user != null) {
			sessions.placeUserInSession(user);
		}

		switch (msgParts[0]) {
		case PROTOCOL.CS.LOGIN:
			processLogin(user, msgParts);
			break;
		case PROTOCOL.CS.REQUEST_MOVEMENT:
			processMovementRequest(user, msgParts);
			break;
		case PROTOCOL.CS.CHAT_POST:
			processChatPost(user, msgParts);
			break;
		case PROTOCOL.CS.REQUEST_INFO:
			sendInfoUpdate(msgParts, user);
			break;
		case PROTOCOL.CS.REQUEST_BOT:
			break;
		case PROTOCOL.CS.READY:
			handleReady(user);
			break;

		default:
			sendToUser(PROTOCOL.getErrorMessage(PROTOCOL.ERRORCODES.INVALID_MESSAGE), user);
			break;
		}
	}

	@Override
	public void processClosingConnection(String clientIP, int clientPort) {
		// Send Errorcode
	}

	/**
	 * Processes a login.
	 * 
	 * @param clientIP
	 * @param clientPort
	 * @param msgParts
	 */
	private void processLogin(User user, String[] msgParts) {
		if (msgParts.length < 2) {
			sendToUser(PROTOCOL.getErrorMessage(PROTOCOL.ERRORCODES.INVALID_MESSAGE), user);
			return;
		}
		if (users.getUserByAdress(user.getIp(), user.getPort()) != null) {
			sendToUser(PROTOCOL.getErrorMessage(PROTOCOL.ERRORCODES.ALREADY_LOGGED_IN), user);
		} else if (users.isNameTaken(msgParts[1])) {
			sendToUser(PROTOCOL.getErrorMessage(PROTOCOL.ERRORCODES.USERNAME_ALREADY_IN_USE), user);
		} else {
			User nUser = new User(user.getIp(), user.getPort(), msgParts[1]);
			users.addUser(nUser);
			sendToUser(PROTOCOL.SC.OK, nUser);
		}
	}

	/**
	 * Processes a movement request.
	 * 
	 * @param user
	 * @param msgParts
	 */
	private void processMovementRequest(User user, String[] msgParts) {
		if (msgParts.length < 3) {
			sendToUser(PROTOCOL.getErrorMessage(PROTOCOL.ERRORCODES.INVALID_MESSAGE), user);
			return;
		}
		Session session = sessions.getSessionByUser(user);// TODO can be null
		int stationId;
		MeansOfTransportation type;
		try {
			stationId = Integer.parseInt(msgParts[1]);
			type = MeansOfTransportation.valueOf(msgParts[2]);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			sendToUser(PROTOCOL.getErrorMessage(PROTOCOL.ERRORCODES.INVALID_MESSAGE), user);
			return;
		}
		if (session.doMovement(user, stationId, type)) {
			sendToUser(PROTOCOL.SC.OK, user);
		} else {
			sendToUser(PROTOCOL.getErrorMessage(PROTOCOL.ERRORCODES.INVALID_MOVEMENT), user);
		}
	}

	/**
	 * Processes a chat post
	 * 
	 * @param user
	 *            the user that sent the chat post
	 * @param msgParts
	 *            the messageparts
	 */
	private void processChatPost(User user, String[] msgParts) {
		if (msgParts.length < 2) {
			sendToUser(PROTOCOL.getErrorMessage(PROTOCOL.ERRORCODES.INVALID_MESSAGE), user);
			return;
		}
		String message = msgParts[1];
		String username = user.getPlayer().getName();
		Session csession = sessions.getSessionByUser(user);
		sendToSession(PROTOCOL.buildMessage(PROTOCOL.SC.CHAT_UPDATE, username, message), csession);
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
		for (User user : session.getAllUsers()) {
			sendToUser(msg, user);
		}
	}
	
	/** Handles a "Ready" from the user 
	 * @param u the user
	 */
	private void handleReady(User u) {
		Session s = sessions.getSessionByUser(u);
		u.getPlayer().setReady(true);
		s.checkReady();
	}
	
	/**
	 * Sends an info update for the specified player to the asking user
	 * 
	 * @param name
	 *            the name of the user to get the update from
	 * @param askingUser
	 *            the user the update will be sent to
	 */
	private void sendInfoUpdate(String[] msgParts, User askingUser) {
		if (msgParts.length < 2) {
			sendToUser(PROTOCOL.getErrorMessage(PROTOCOL.ERRORCODES.INVALID_MESSAGE), askingUser);
			return;
		}
		if (users.getUserByName(msgParts[1]) == null) {
			sendToUser(PROTOCOL.getErrorMessage(PROTOCOL.ERRORCODES.USER_DOES_NOT_EXIST), askingUser);
			return;
		}
		User u = users.getUserByName(msgParts[1]);
		sendToUser(PROTOCOL.buildMessage(PROTOCOL.SC.INFO_UPDATE,u.getPlayer().getName(),u.getPlayer().getInfoString()),askingUser);
	}
}
