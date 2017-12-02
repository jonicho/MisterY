package de.misterY;

import java.util.ArrayList;

/**
 * A station where the players can travel to with the set means of transport.
 * Each station has a list of links pointing to the next station.
 */
public class Station {
	private ArrayList<Link> links;

	private final int id;

	private boolean bus;
	private boolean underground;
	
	private float x;
	private float y;

	/**
	 * Constructs a new station.
	 * 
	 * @param bus
	 *            Whether the station is a bus station.
	 * @param underground
	 *            Whether the station is an underground station.
	 * @param id
	 *            The station's id
	 * @param x
	 *            The x-coordinate. Must be between 0.0 and 1.0.
	 * @param y
	 *            The y-coordinate. Must be between 0.0 and 1.0.
	 */
	public Station(boolean bus, boolean underground, int id, float x, float y) {
		if (x < 0.0 || x > 1.0 || y < 0.0 || y > 1.0) {
			throw new IllegalArgumentException("The coordinates must be between 0.0 and 1.0!");
		}
		this.bus = bus;
		this.underground = underground;
		this.id = id;
		this.x = x;
		this.y = y;
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
	 * Adds a link if it does not already exist
	 * 
	 * @param link
	 *            The link to add
	 * @throws IllegalArgumentException
	 *             If the link already exists in this station
	 */
	public void addLink(Link link) throws IllegalArgumentException {
		if (getLink(link.getStation().getId()) == null) {
			links.add(link);
		} else {
			throw new IllegalArgumentException(
					"Link to Station id " + link.getStation().getId() + " already exists in this station!");
		}
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
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}
