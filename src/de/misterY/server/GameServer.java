package de.misterY.server;

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
				sendToUser(PROTOCOL.getErrorMessage(PROTOCOL.ERRORCODES.NOT_LOGGED_IN),
						user);
				return;
			}
		}

		switch (msgParts[0]) {
		case PROTOCOL.CS.LOGIN:
			processLogin(user, msgParts);
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
			sendToUser(PROTOCOL.getErrorMessage(PROTOCOL.ERRORCODES.INVALID_MESSAGE),
					user);
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

	/**
	 * Sends an info update for the specified player to the asking user
	 * 
	 * @param name
	 *            the name of the user to get the update from
	 * @param askingUser
	 *            the user the update will be sent to
	 */
	private void sendInfoUpdate(String name, User askingUser) {
		String msg = PROTOCOL.buildMessage(PROTOCOL.SC.INFO_UPDATE,
				users.getUserByName(name).getPlayer().getInfoString());
		sendToUser(msg, askingUser);
	}
}
