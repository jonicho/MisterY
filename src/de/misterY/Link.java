package de.misterY;

public class Link {
	private Station station;

	private boolean bus;
	private boolean underground;

	/**
	 * Constructs a new link.
	 * 
	 * @param targetStation
	 *            The target station.
	 * @param bus
	 *            Whether the link is a bus link.
	 * @param underground
	 *            Whether the link is a underground link.
	 */
	public Link(Station targetStation, boolean bus, boolean underground) {
	}

	public Station getStation() {
		return station;
	}

	/**
	 * @return Whether a bus can use this link
	 */
	public boolean isBus() {
		return bus;
	}

	/**
	 * @return Whether an underground can use this link
	 */
	public boolean isUnderground() {
		return underground;
	}
}
