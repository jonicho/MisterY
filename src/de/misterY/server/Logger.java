package de.misterY.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.misterY.net.PROTOCOL;

/**
 * A logger used to log things. Writes logs to System.out and the log file. The
 * log file is named in the following way:<br>
 * misterY_log_yyyy-MM-dd_HH-mm-ss-SSS.txt
 *
 */
public class Logger {
	private static final File logFile;

	static {
		logFile = new File(
				"misterY_log_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date()) + ".txt");
	}

	/**
	 * Logs a message
	 * 
	 * @param msg
	 *            The message
	 * @param user
	 *            The user to whom the message was sent / from whom the message came
	 * @param session
	 *            The session in which the user is. Can be null
	 * @param incoming
	 *            Whether the message was an incoming one. If false it is assumed
	 *            that the message was outgoing
	 */
	public static void logMessage(String msg, User user, Session session, boolean incoming) {
		log((session != null ? ("Session " + session.getId() + ": ") : "")
				+ (incoming ? "Got message from \"" : "Sent message to \"") + user.getPlayer().getName() + "\": "
				+ (msg.startsWith("map" + PROTOCOL.SPLIT + "<?xml") ? "map::[map]"
						: msg.replace(PROTOCOL.SPLIT, "::")));
	}

	/**
	 * Logs a message. Writes a line in System.out and appends a line to the log
	 * file with the current time and the message in the following format:<br>
	 * [yyyy.MM.dd HH:mm:ss.SSS] message
	 * 
	 * @param msg
	 */
	public static void log(String msg) {
		String output = "[" + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS").format(new Date()) + "] " + msg;
		System.out.println(output);
		try {
			FileWriter fw = new FileWriter(logFile, true);
			fw.write(output + "\n");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
