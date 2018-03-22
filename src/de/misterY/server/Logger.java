package de.misterY.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.misterY.net.PROTOCOL;

public class Logger {
	private static final File logFile;

	static {
		logFile = new File("misterY_log_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date()) + ".txt");
	}

	public static void logMessage(String msg, User user, Session session, boolean incoming) {
		log((session != null ? ("Session " + session.getId() + ": ") : "")
				+ (incoming ? "Got message from \"" : "Sent message to \"") + user.getPlayer().getName() + "\": "
				+ (msg.startsWith("map" + PROTOCOL.SPLIT + "<?xml") ? "map::[map]"
						: msg.replace(PROTOCOL.SPLIT, "::")));
	}

	public static void log(String msg) {
		String output = "[" + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.S").format(new Date()) + "] " + msg;
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
