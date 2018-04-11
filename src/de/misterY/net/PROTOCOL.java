package de.misterY.net;

public final class PROTOCOL {
	private PROTOCOL() {}

	public static final String IP = "localhost";
	public static final int PORT = 54699;
	public static final String SPLIT = "\u001F"; // U+001F: Unit separator

	public final class SC {
		private SC() {}

		public static final String ERROR = "err", // Base [split] errorcode
				OK = "ok", // Base
				CHAT_UPDATE = "cupdt", // Base [split] name [split] message
				INFO_UPDATE = "upinf", // Base [split] name [split] taxi tickets [split] bus tickets [split] underground tickets [split] current station id (-1 if hidden) [split] is mrY
				USED_TICKETS = "sdtck", // Base [split] tickets used by misterY
				MAP = "map", // Base [split] map
				TURN = "turn", // Base [split] name [split] round
				PLAYER_LEFT = "plleft", // Base [split] name
				WIN = "win"; // Base [split] name
	}

	public final class CS {
		private CS() {}

		public static final String LOGIN = "lgn", // Base [split] name
				REQUEST_MOVEMENT = "reqmv", // Base [split] end id [split] type
				CHAT_POST = "cpst", // Base [split] message
				REQUEST_BOT = "reqbot", // Base
				READY = "rdy", // Base
				SKIP_TURN = "skt", // Base [split] amount
				ADD_BOT = "addbot";// Base 
	}

	public final class ERRORCODES {
		private ERRORCODES() {}

		public static final int UNKNOWN_ERROR = -1, CONNECTION_CLOSED = 0, CONNECTION_REFUSED = 1, INVALID_MESSAGE = 2,
				USERNAME_ALREADY_IN_USE = 3, USER_DOES_NOT_EXIST = 4, NOT_LOGGED_IN = 5, ALREADY_LOGGED_IN = 6, INVALID_MOVEMENT = 7;
	}

	/**
	 * Builds a massage by connecting all arguments into one String, separated by
	 * SPLIT.<br>
	 * First it calls String.valueOf(x) on each given object.
	 * 
	 * @param msgParts
	 *            The message parts to connect
	 * @return The built String
	 */
	public static String buildMessage(Object... msgParts) {
		String result = String.valueOf(msgParts[0]);
		for (int i = 1; i < msgParts.length; i++) {
			result += SPLIT + String.valueOf(msgParts[i]);
		}
		return result;
	}

	/**
	 * Returns an error message based on the following pattern:</br>
	 * {@code SC.ERROR + SPLIT + errorcode}
	 * 
	 * @param errorcode
	 *            The error code
	 * @return The error message
	 */
	public static String getErrorMessage(int errorcode) {
		return buildMessage(SC.ERROR, String.valueOf(errorcode));
	}
}
