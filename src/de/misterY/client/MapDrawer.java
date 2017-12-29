package de.misterY.client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
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
	private int avgSize;
	private Vector2D mousePos;

	/**
	 * Draws the map onto the given graphics.<br>
	 * If the map is null, this method does nothing.
	 * 
	 * @param g
	 *            The graphics on which is drawn
	 */
	public void drawMap(Graphics2D g) {
		avgSize = (width + height) / 2;
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
		g.setStroke(new BasicStroke((float) (0.002 * avgSize)));
		for (Link link : station.getLinks()) {
			Vector2D ort = station.getPos().getClone().subtract(link.getStation().getPos()).rotate(Math.PI / 2)
					.setLength(0.000002 * avgSize);
			if (ort.getX() > 0) {
				ort.multiply(-1);
			}
			if (link.isTaxi()) {
				drawLink(g, station.getPos(), link.getStation().getPos(), Color.YELLOW);
			}
			if (link.isBus()) {
				drawLink(g, station.getPos().getClone().add(ort), link.getStation().getPos().getClone().add(ort),
						Color.GREEN);
			}
			if (link.isUnderground()) {
				drawLink(g, station.getPos().getClone().subtract(ort),
						link.getStation().getPos().getClone().subtract(ort), Color.RED);
			}
		}
	}

	/**
	 * Draws a link from v1 to v2 with the given color.<br>
	 * Uses a gradient that has the given color at v1 and a transparent color at v2.
	 * 
	 * @param g
	 *            The graphics on which is drawn
	 * @param v1
	 *            The start position of the link
	 * @param v2
	 *            The end position of the link
	 * @param color
	 *            The color of the link
	 */
	private void drawLink(Graphics2D g, Vector2D v1, Vector2D v2, Color color) {
		int x1 = v1.getDrawX(width);
		int y1 = v1.getDrawY(height);
		int x2 = v2.getDrawX(width);
		int y2 = v2.getDrawY(height);
		g.setPaint(new GradientPaint(x1, y1, color, x2, y2, new Color(0, 0, 0, 0)));
		g.drawLine(x1, y1, x2, y2);
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
		double sizeFactor = 0.001 * avgSize;
		if (station.getPos().getDistance(mousePos) < 0.01) {
			sizeFactor *= 1.5;
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
		drawCenteredString(g, station.getId() + "", x, y, (float) (sizeFactor * 10));
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
					player.getCurrentStation().getPos().getDrawY(height), (float) (0.01 * avgSize));
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
	private void drawCenteredString(Graphics2D g, String string, int x, int y, float size) {
		g.setFont(g.getFont().deriveFont(size));
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
