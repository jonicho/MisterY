package de.misterY;

public class Link {
	private Station station;

	private boolean taxi;
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
	public Link(Station targetStation, boolean taxi, boolean bus, boolean underground) {
		this.station = targetStation;
		this.taxi = taxi;
		this.bus = bus;
		this.underground = underground;
	}

	/**
	 * @return The station the link is pointing at
	 */
	public Station getStation() {
		return station;
	}

	/**
	 * @return Whether a taxi can use this link
	 */
	public boolean isTaxi() {
		return taxi;
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

	/**
	 * @param type
	 * @return Whether the given means of transportation can use this link
	 */
	public boolean isMeansOfTransportation(MeansOfTransportation type) {
		return (type.equals(MeansOfTransportation.Taxi) && isTaxi())
				|| (type.equals(MeansOfTransportation.Bus) && isBus())
				|| (type.equals(MeansOfTransportation.Underground) && isUnderground());
	}
}
