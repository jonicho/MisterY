package de.misterY.client;

import java.awt.Graphics;

import javax.swing.JPanel;

import de.misterY.Link;
import de.misterY.Map;
import de.misterY.Station;
import de.misterY.Vector2D;

public class Canvas extends JPanel {
	private static final long serialVersionUID = 1L;

	private Map map;

	@Override
	protected void paintComponent(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		drawMap(g);
	}

	/**
	 * Draws the map onto the given graphics.<br>
	 * If the map is null, this method does nothing.
	 * 
	 * @param g
	 *            The graphics on which is drawn
	 */
	private void drawMap(Graphics g) {
		if (map == null) {
			return;
		}
		for (Station station : map.getStations()) {
			drawLinks(g, station);
		}
		for (Station station : map.getStations()) {
			drawStations(g, station);
		}
	}

	/**
	 * Draws all links of the given station onto the given graphics
	 * 
	 * @param g
	 *            The graphics on which is drawn
	 * @param station
	 *            The station to draw the links of
	 */
	private void drawLinks(Graphics g, Station station) {
		for (Link link : station.getLinks()) {
			int x1 = station.getPos().getDrawX(getWidth());
			int y1 = station.getPos().getDrawY(getHeight());
			int x2 = link.getStation().getPos().getDrawX(getWidth());
			int y2 = link.getStation().getPos().getDrawY(getHeight());
			g.drawLine(x1, y1, x2, y2);
			drawArrow(g, link.getStation().getPos().getClone(),
					station.getPos().getClone().subtract(link.getStation().getPos()));
		}
	}

	/**
	 * Draws an arrow pointing to pos with the direction of vec.
	 * 
	 * @param pos
	 *            The position the arrow will be pointing to
	 * @param vec
	 *            The direction in which the arrow will point
	 */
	private void drawArrow(Graphics g, Vector2D pos, Vector2D vec) {
		vec.setLength(0.03);
		pos.add(vec);
		Vector2D left = vec.getClone().rotate(Math.toRadians(30)).add(pos);
		Vector2D right = vec.getClone().rotate(Math.toRadians(-30)).add(pos);
		g.drawLine(pos.getDrawX(getWidth()), pos.getDrawY(getHeight()), left.getDrawX(getWidth()), left.getDrawY(getHeight()));
		g.drawLine(pos.getDrawX(getWidth()), pos.getDrawY(getHeight()), right.getDrawX(getWidth()), right.getDrawY(getHeight()));
	}

	/**
	 * Draws the given station onto the given graphics
	 * 
	 * @param g
	 *            The graphics on which is drawn
	 * @param station
	 *            The station which is drawn
	 */
	private void drawStations(Graphics g, Station station) {
		int x = station.getPos().getDrawX(getWidth());
		int y = station.getPos().getDrawY(getHeight());
		int size = 10;
		g.fillOval(x - size / 2, y - size / 2, size, size);
	}

	public void setMap(Map map) {
		this.map = map;
	}
}
