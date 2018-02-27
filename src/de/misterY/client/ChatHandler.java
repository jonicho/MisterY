package de.misterY.client;

import java.util.ArrayList;

import de.misterY.Player;

/**
 * A class to handle chat messages
 */
public class ChatHandler {
	private ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();

	public void addMessage(Player player, String message) {
		messages.add(new ChatMessage(player, message));
	}

	/**
	 * Returns a string representation of this chat in html format.<br>
	 * Can also be used in a JTextPane
	 * 
	 * @return the string
	 */
	public String getChatString() {
		String result = "<html>";
		for (ChatMessage chatMessage : messages) {
			result += "<strong>";
			result += chatMessage.getPlayer().getName();
			if (chatMessage.getPlayer().isMrY())
				result += " <em>[MisterY]</em>";
			result += ":</strong> ";
			result += chatMessage.getMessage();
			result += "<br>";
		}
		result += "</html>";
		return result;
	}

	/**
	 * A chat message class that has the message string the player who sent the
	 * message
	 */
	private class ChatMessage {
		private Player player;
		private String message;

		public ChatMessage(Player player, String message) {
			this.player = player;
			this.message = message;
		}

		/**
		 * @return the player who sent the message
		 */
		public Player getPlayer() {
			return player;
		}

		public String getMessage() {
			return message;
		}
	}
}
