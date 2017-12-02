package de.misterY;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MapLoader {

	/**
	 * Loads a map by inserting the Stations into the given ArrayLists
	 * 
	 * @param stations
	 *            The stations
	 * @param startStations
	 *            The start stations
	 * @param mapFile
	 *            The file to load the map from.
	 */
	public static void loadMap(ArrayList<Station> stations, ArrayList<Station> startStations, File mapFile) {
		try {
			loadMap(stations, startStations, new FileInputStream(mapFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads a map by inserting the Stations into the given ArrayLists
	 * 
	 * @param stations
	 *            The stations
	 * @param startStations
	 *            The start stations
	 * @param mapString
	 *            The string to load the map from.
	 */
	public static void loadMap(ArrayList<Station> stations, ArrayList<Station> startStations, String mapString) {
		loadMap(stations, startStations, new ByteArrayInputStream(mapString.getBytes()));
	}

	/**
	 * Loads a map by inserting the Stations into the given ArrayLists
	 * 
	 * @param stations
	 *            The stations
	 * @param startStations
	 *            The start stations
	 * @param inputStream
	 *            The input stream to load the map from.
	 */
	private static void loadMap(ArrayList<Station> stations, ArrayList<Station> startStations, InputStream inputStream) {
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
			doc.getDocumentElement().normalize();
			NodeList stationElements = doc.getElementsByTagName("station");
			for (int i = 0; i < stationElements.getLength(); i++) {
				Element stationElement = (Element) stationElements.item(i);

				boolean bus = Boolean.parseBoolean(stationElement.getAttribute("bus"));
				boolean underground = Boolean.parseBoolean(stationElement.getAttribute("underground"));
				int id = Integer.parseInt(stationElement.getAttribute("id"));
				float x = Float.parseFloat(stationElement.getAttribute("x"));
				float y = Float.parseFloat(stationElement.getAttribute("y"));
				boolean start = Boolean.parseBoolean(stationElement.getAttribute("start"));

				Station station = new Station(bus, underground, id, x, y);
				stations.add(station);

				if (start) {
					startStations.add(station);
				}
			}

			for (int i = 0; i < stationElements.getLength(); i++) {
				Element stationElement = (Element) stationElements.item(i);
				NodeList linkElements = stationElement.getElementsByTagName("link");
				int id = Integer.parseInt(stationElement.getAttribute("id"));
				Station station = null;
				for (Station s : stations) {
					if (s.getId() == id) {
						station = s;
					}
				}

				for (int j = 0; j < linkElements.getLength(); j++) {
					Element linkElement = (Element) linkElements.item(j);

					int targetStationId = Integer.parseInt(linkElement.getAttribute("station"));
					boolean bus = Boolean.parseBoolean(linkElement.getAttribute("bus"));
					boolean underground = Boolean.parseBoolean(linkElement.getAttribute("underground"));

					Station targetStation = null;
					for (Station ts : stations) {
						if (ts.getId() == targetStationId) {
							targetStation = ts;
						}
					}
					station.addLink(new Link(targetStation, bus, underground));
				}
			}
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		} catch (NullPointerException | NumberFormatException e) {
			e.printStackTrace();
		}
	}
}
