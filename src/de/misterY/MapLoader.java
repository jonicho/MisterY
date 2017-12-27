package de.misterY;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
	 * @return The map string, an empty string if an error occurred.
	 */
	public static String loadMap(ArrayList<Station> stations, ArrayList<Station> startStations, int[] initialTickets, File mapFile) {
		try {
			String mapString = "";
			BufferedReader bf = new BufferedReader(new FileReader(mapFile));
			String line;
			while ((line = bf.readLine()) != null) {
				mapString += line;
			}
			bf.close();
			return loadMap(stations, startStations, initialTickets, mapString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
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
	 * @return The map string, an empty string if an error occurred.
	 */
	public static String loadMap(ArrayList<Station> stations, ArrayList<Station> startStations, int[] initialTickets, String mapString) {
		try {
			mapString = mapString.replaceAll("[\\>][\\t]+[\\<]", "><"); // remove unnecessary tabs
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new ByteArrayInputStream(mapString.getBytes()));
			doc.getDocumentElement().normalize();
			Element initialTicketsElement = (Element) doc.getElementsByTagName("i").item(0);
			initialTickets[0] = Integer.parseInt(initialTicketsElement.getAttribute("t"));
			initialTickets[1] = Integer.parseInt(initialTicketsElement.getAttribute("b"));
			initialTickets[2] = Integer.parseInt(initialTicketsElement.getAttribute("u"));
			NodeList stationElements = doc.getElementsByTagName("s");
			for (int i = 0; i < stationElements.getLength(); i++) {
				Element stationElement = (Element) stationElements.item(i);

				boolean bus = stationElement.getAttribute("b").equalsIgnoreCase("t");
				boolean underground = stationElement.getAttribute("u").equalsIgnoreCase("t");
				int id = Integer.parseInt(stationElement.getAttribute("i"));
				double x = Float.parseFloat(stationElement.getAttribute("x"));
				double y = Float.parseFloat(stationElement.getAttribute("y"));
				boolean start = stationElement.getAttribute("s").equalsIgnoreCase("t");

				Station station = new Station(bus, underground, id, new Vector2D(x, y));
				stations.add(station);

				if (start) {
					startStations.add(station);
				}
			}

			for (int i = 0; i < stationElements.getLength(); i++) {
				Element stationElement = (Element) stationElements.item(i);
				NodeList linkElements = stationElement.getElementsByTagName("l");
				int id = Integer.parseInt(stationElement.getAttribute("i"));
				Station station = null;
				for (Station s : stations) {
					if (s.getId() == id) {
						station = s;
					}
				}

				for (int j = 0; j < linkElements.getLength(); j++) {
					Element linkElement = (Element) linkElements.item(j);

					int targetStationId = Integer.parseInt(linkElement.getAttribute("s"));
					boolean taxi = linkElement.getAttribute("t").equalsIgnoreCase("t");
					boolean bus = linkElement.getAttribute("b").equalsIgnoreCase("t");
					boolean underground = linkElement.getAttribute("u").equalsIgnoreCase("t");

					Station targetStation = null;
					for (Station ts : stations) {
						if (ts.getId() == targetStationId) {
							targetStation = ts;
						}
					}
					station.addLink(new Link(targetStation, taxi, bus, underground));
				}
			}
			return mapString;
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		} catch (NullPointerException | NumberFormatException e) {
			e.printStackTrace();
		}
		return "";
	}
}
