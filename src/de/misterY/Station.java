package de.misterY;

import java.util.ArrayList;

public class Station {
	private ArrayList<Link> links;

	private final int id;

	private boolean bus;
	private boolean underground;

	/**
	 * Constructs a new station.
	 * 
	 * @param bus
	 *            Whether the station is a bus station.
	 * @param underground
	 *            Whether the station is an underground station.
	 */
	public Station(boolean bus, boolean underground, int id) {
		this.id = id;
	}

	/**
	 * @return Whether a bus can stop at this station
	 */
	public boolean isBus() {
		return bus;
	}

	/**
	 * @return Whether an underground can stop at this station
	 */
	public boolean isUnderground() {
		return underground;
	}

	public int getId() {
		return id;
	}

	/**
	 * Adds a link
	 * 
	 * @param link
	 *            The link to add
	 */
	public void addLink(Link link) {

	}

	/**
	 * Returns the link with the given station id. Returns null if there is no link
	 * with the given station id.
	 * 
	 * @param station
	 *            The station
	 * @return The link with the given station id. <br>
	 *         Null if there is no link with the given station id.
	 */
	public Link getLink(int id) {
		for (Link link : links) {
			if (link.getStation().getId() == id) {
				return link;
			}
		}
		return null;
	}

	/**
	 * Returns the link with the given station. Returns null if there is no link
	 * with the given station.
	 * 
	 * @param station
	 *            The station
	 * @return The link with the given station. <br>
	 *         Null if there is no link with the given station.
	 */
	public Link getLink(Station station) {
		return getLink(station.getId());
	}
}
