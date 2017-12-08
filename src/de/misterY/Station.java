package de.misterY;

import java.util.ArrayList;

/**
 * A station where the players can travel to with the set means of transport.
 * Each station has a list of links pointing to the next station.
 */
public class Station {
	private ArrayList<Link> links = new ArrayList<Link>();

	private final int id;

	private boolean bus;
	private boolean underground;

	private Vector2D pos;

	/**
	 * Constructs a new station.
	 * 
	 * @param bus
	 *            Whether the station is a bus station.
	 * @param underground
	 *            Whether the station is an underground station.
	 * @param id
	 *            The station's id
	 * @param pos
	 *            The station's position. The x and y components must each be
	 *            between 0.0 and 1.0.
	 */
	public Station(boolean bus, boolean underground, int id, Vector2D pos) {
		if (pos.getX() < 0.0 || pos.getX() > 1.0 || pos.getY() < 0.0 || pos.getY() > 1.0) {
			throw new IllegalArgumentException("The coordinates must be between 0.0 and 1.0!");
		}
		this.bus = bus;
		this.underground = underground;
		this.id = id;
		this.pos = pos;
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

	public Vector2D getPos() {
		return pos;
	}

	public ArrayList<Link> getLinks() {
		return links;
	}
}
