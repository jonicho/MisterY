package de.misterY.net;

public final class PROTOCOL {
	private PROTOCOL() {}

	public static final int PORT = 54699;
	public static final String SPLIT = "\u2063"; // U+2063: Invisible separator

	public final class SC {
		private SC() {}

		public static final String ERROR = "err", // Base [split] errorcode
				POSITION_UPDATE = "posupdt", // Base [split] name [split] position [split] used ticket
				OK = "ok", // Base
				CHAT_UPDATE = "cupdt", // Base [split] name [split] message
				INFO_UPDATE = "upinf", // Base [split] name [split] info //TODO which info?
				MAP = "map", // Base [split] map
				YOUR_TURN = "yturn", // Base
				PLAYER_LEFT = "plleft"; // Base [split] name
	}

	public final class CS {
		private CS() {}

		public static final String LOGIN = "lgn", // Base [split] name
				REQUEST_MOVEMENT = "reqmv", // Base [split] Start id [split] type [split] end id
				CHAT_POST = "cpst", // Base [split] message
				REQUEST_INFO = "reqinfo", // Base [split] name
				REQUEST_BOT = "reqbot"; // Base
	}

	public final class ERRORCODES {
		private ERRORCODES() {}

		public static final int UNKNOWN_ERROR = -1, CONNECTION_CLOSED = 0, CONNECTION_REFUSED = 1, INVALID_MESSAGE = 2,
				USERNAME_ALREADY_IN_USE = 3, USER_DOES_NOT_EXIST = 4;
	}

	/**
	 * Builds a massage by connect all arguments in one String, separated by SPLIT.
	 * 
	 * @param msgParts The massage parts to connect
	 * @return The builded String
	 */
	public static String buildMessage(String... msgParts) {
		String result = msgParts[0];
		for (int i = 1; i < msgParts.length; i++) {
			result += SPLIT + msgParts[i];
		}
		return result;
	}
}
