package de.misterY.client;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import de.misterY.Link;
import de.misterY.Map;
import de.misterY.Player;
import de.misterY.Station;
import de.misterY.Vector2D;

public class MapDrawer {

	private Map map;
	private int width;
	private int height;
	private Vector2D mousePos;

	/**
	 * Draws the map onto the given graphics.<br>
	 * If the map is null, this method does nothing.
	 * 
	 * @param g
	 *            The graphics on which is drawn
	 */
	public void drawMap(Graphics2D g) {
		g.setColor(Color.BLACK);
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
	private void drawLinks(Graphics2D g, Station station) {
		for (Link link : station.getLinks()) {
			g.setColor(Color.BLACK);
			int x1 = station.getPos().getDrawX(width);
			int y1 = station.getPos().getDrawY(height);
			int x2 = link.getStation().getPos().getDrawX(width);
			int y2 = link.getStation().getPos().getDrawY(height);
			g.drawLine(x1, y1, x2, y2);
			Vector2D pos = link.getStation().getPos().getClone();
			Vector2D vec = station.getPos().getClone().subtract(link.getStation().getPos());
			if (link.isTaxi()) {
				g.setColor(Color.YELLOW);
				drawArrow(g, pos, vec, 0.02);
			}
			if (link.isBus()) {
				g.setColor(Color.GREEN);
				drawArrow(g, pos, vec, 0.03);
			}
			if (link.isUnderground()) {
				g.setColor(Color.RED);
				drawArrow(g, pos, vec, 0.04);
			}
		}
	}

	/**
	 * Draws an arrow pointing to pos with the direction of vec and the distance
	 * dis.
	 * 
	 * @param pos
	 *            The position the arrow will be pointing to
	 * @param vec
	 *            The direction in which the arrow will point
	 * @param dis
	 *            The distance from pos
	 */
	private void drawArrow(Graphics2D g, Vector2D pos, Vector2D vec, double dis) {
		vec.setLength(0.03);
		pos.add(vec.getClone().setLength(dis));
		Vector2D left = vec.getClone().rotate(Math.toRadians(30)).add(pos);
		Vector2D right = vec.getClone().rotate(Math.toRadians(-30)).add(pos);
		g.drawLine(pos.getDrawX(width), pos.getDrawY(height), left.getDrawX(width), left.getDrawY(height));
		g.drawLine(pos.getDrawX(width), pos.getDrawY(height), right.getDrawX(width), right.getDrawY(height));
	}

	/**
	 * Draws the given station onto the given graphics
	 * 
	 * @param g
	 *            The graphics on which is drawn
	 * @param station
	 *            The station which is drawn
	 */
	private void drawStations(Graphics2D g, Station station) {
		g.setColor(Color.BLACK);
		int x = station.getPos().getDrawX(width);
		int y = station.getPos().getDrawY(height);
		int size;
		double sizeFactor = 1;
		if (station.getPos().getDistance(mousePos) < 0.01) {
			sizeFactor = 1.5;
		}
		if (station.isUnderground()) {
			size = (int) (20 * sizeFactor);
			g.setColor(Color.RED);
			g.fillOval(x - size / 2, y - size / 2, size, size);
		}
		if (station.isBus()) {
			size = (int) (16 * sizeFactor);
			g.setColor(Color.GREEN);
			g.fillOval(x - size / 2, y - size / 2, size, size);
		}
		size = (int) (12 * sizeFactor);
		g.setColor(Color.YELLOW);
		g.fillOval(x - size / 2, y - size / 2, size, size);

		g.setColor(Color.BLACK);
		drawCenteredString(g, station.getId() + "", x, y);
	}

	/**
	 * Draws all names of the given players onto the given graphics
	 * 
	 * @param g
	 *            The graphics to draw the player names on
	 */
	public void drawPlayers(Graphics2D g, ArrayList<Player> players) {
		g.setColor(Color.BLACK);
		if (players == null) {
			return;
		}
		for (Player player : players) {
			drawCenteredString(g, player.getName(), player.getCurrentStation().getPos().getDrawX(width),
					player.getCurrentStation().getPos().getDrawY(height));
		}
	}

	/**
	 * Draws the given string onto the given graphics
	 * 
	 * @param g
	 *            The graphics to draw the string on
	 * @param string
	 *            The string to draw
	 * @param x
	 *            The center-x-coordinate
	 * @param y
	 *            The center-y-coordinate
	 */
	private void drawCenteredString(Graphics2D g, String string, int x, int y) {
		int width = g.getFontMetrics().stringWidth(string);
		int height = g.getFontMetrics().getHeight();
		g.drawString(string, x - width / 2, y - height / 2 + g.getFontMetrics().getAscent());
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setMousePos(Vector2D mousePos) {
		this.mousePos = mousePos;
	}
}
