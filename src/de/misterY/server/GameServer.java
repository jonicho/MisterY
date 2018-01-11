package de.misterY.server;

import java.io.File;
import java.util.ArrayList;

import de.misterY.MapLoader;
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
		System.out.println(message);
		User user = users.getUserByAdress(clientIP, clientPort);
		String[] msgParts = message.split(PROTOCOL.SPLIT);
		if (user == null) {
			user = new User(clientIP, clientPort, "tmpuser" + clientIP + ":" + clientPort);
			if (!msgParts[0].equals(PROTOCOL.CS.LOGIN)) {
				sendToUser(PROTOCOL.getErrorMessage(PROTOCOL.ERRORCODES.NOT_LOGGED_IN), user);
				return;
			}
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
		User user = users.getUserByAdress(clientIP, clientPort);
		users.removeUser(user);
		Session session = sessions.getSessionByUser(user);
		if (session.removeUser(user) || session.isActive()) {
			sessions.removeSession(session);
		}
		sendToSession(PROTOCOL.buildMessage(PROTOCOL.SC.PLAYER_LEFT, user.getPlayer().getName()), session);
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
			sessions.placeUserInSession(nUser);
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
		Session session = sessions.getSessionByUser(user);
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
			sendInfoUpdate(user, session);
			sendToSession(PROTOCOL.buildMessage(PROTOCOL.SC.USED_TICKETS,
					PROTOCOL.buildMessage((Object[]) session.getTicketsUsedByMisterY())), session);
			if(session.getWinner() != null) {
				sendToSession(PROTOCOL.buildMessage(PROTOCOL.SC.WIN, session.getWinner().getPlayer().getName()), session);
			} else {
				sendToSession(PROTOCOL.buildMessage(PROTOCOL.SC.TURN, session.getCurrentUser().getPlayer().getName(), session.getRound()),
						session);
			}
			
			
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
		sendToSession(msg, session, new ArrayList<>());
	}

	/**
	 * Sends a server message to all clients in the given session except the
	 * excepted users.
	 * 
	 * @param msg
	 *            The message to send
	 * @param exceptedUsers
	 *            The excepted users
	 * @param session
	 *            The session to send the message to
	 */
	private void sendToSession(String msg, Session session, ArrayList<User> exceptedUsers) {
		for (User user : session.getAllUsers()) {
			if (!exceptedUsers.contains(user)) {
				sendToUser(msg, user);
			}
		}
	}

	/**
	 * Sends a info update of the given user to the given session, also considering
	 * whether misterY is showing.
	 * 
	 * @param user
	 * @param session
	 */
	private void sendInfoUpdate(User user, Session session) {
		ArrayList<User> exceptedUsers = new ArrayList<>();
		if (user.getPlayer().isMrY()) {
			exceptedUsers.add(user);
			sendToUser(PROTOCOL.buildMessage(PROTOCOL.SC.INFO_UPDATE, user.getPlayer().getInfoString(true)), user);
		}
		sendToSession(
				PROTOCOL.buildMessage(PROTOCOL.SC.INFO_UPDATE,
						user.getPlayer().getInfoString(!user.getPlayer().isMrY() || session.isMisterYShowing())),
				session, exceptedUsers);
	}

	/**
	 * Handles a "Ready" from the user
	 * 
	 * @param u
	 *            the user
	 */
	private void handleReady(User u) {
		Session session = sessions.getSessionByUser(u);
		if (session.isActive()) {
			return;
		}
		u.getPlayer().setReady(true);
		session.checkReady();
		if (session.isActive()) {
			session.prepareGame(MapLoader.loadMap(new File("src/de/misterY/maps/OriginalScotlandYard.xml")));// TODO
			sendToSession(PROTOCOL.buildMessage(PROTOCOL.SC.MAP, session.getMap().getMapString()), session);
			for (User user : session.getAllUsers()) {
				sendInfoUpdate(user, session);
			}
			sendToSession(PROTOCOL.buildMessage(PROTOCOL.SC.TURN, session.getCurrentUser().getPlayer().getName(), session.getRound()),
					session);
		}
	}
}
