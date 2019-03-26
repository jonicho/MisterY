package de.misterY.client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
	private Station hoveredStation;
	private Rectangle drawRect;

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
			if (station != hoveredStation)
				drawStation(g, station);
		}
		if (hoveredStation != null)
			drawStation(g, hoveredStation);
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
			Link oppositeLink = link.getStation().getLink(station);
			if (ort.getX() > 0) {
				ort.multiply(-1);
			}
			if (link.isTaxi()) {
				drawLine(g, station.getPos(), link.getStation().getPos(), Color.YELLOW,
						oppositeLink == null || !oppositeLink.isTaxi());
			}
			if (link.isBus()) {
				drawLine(g, station.getPos().getClone().add(ort), link.getStation().getPos().getClone().add(ort),
						Color.GREEN, oppositeLink == null || !oppositeLink.isBus());
			}
			if (link.isUnderground()) {
				drawLine(g, station.getPos().getClone().subtract(ort),
						link.getStation().getPos().getClone().subtract(ort), Color.RED,
						oppositeLink == null || !oppositeLink.isUnderground());
			}
		}
	}

	/**
	 * Draws a line from v1 to v2 with the given color.<br>
	 * Uses a gradient that has the given color at v1 and a transparent color at v2
	 * if the given gradient boolean is true.
	 * 
	 * @param g
	 *            The graphics on which is drawn
	 * @param v1
	 *            The start position of the line
	 * @param v2
	 *            The end position of the line
	 * @param color
	 *            The color of the link
	 * @param gradient
	 *            Whether gradient should be used
	 */
	private void drawLine(Graphics2D g, Vector2D v1, Vector2D v2, Color color, boolean gradient) {
		int x1 = v1.getDrawX(width);
		int y1 = v1.getDrawY(height);
		int x2 = v2.getDrawX(width);
		int y2 = v2.getDrawY(height);
		if (drawRect != null && !drawRect.intersectsLine(x1, y1, x2, y2)) {
			return;
		}
		if (gradient) {
			g.setPaint(new GradientPaint(x1, y1, color, x2, y2, new Color(0, 0, 0, 0)));
		} else {
			g.setColor(color);
		}
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
	private void drawStation(Graphics2D g, Station station) {
		g.setColor(Color.BLACK);
		int x = station.getPos().getDrawX(width);
		int y = station.getPos().getDrawY(height);
		if (drawRect != null && !drawRect.contains(x, y)) {
			return;
		}
		double sizeFactor = 0.001 * avgSize;
		if (hoveredStation != null && station == hoveredStation) {
			sizeFactor *= 1.5;
		}
		int size = (int) (12 * sizeFactor);
		g.setStroke(new BasicStroke((float) (0.8 * sizeFactor)));
		g.setColor(Color.YELLOW);
		g.fillOval(x - size / 2, y, size, size);
		g.setColor(Color.BLACK);
		g.drawOval(x - size / 2, y, size, size);

		g.setColor(station.isBus() ? Color.GREEN : Color.YELLOW);
		g.fillOval(x - size / 2, y - size, size, size);
		g.setColor(Color.BLACK);
		g.drawOval(x - size / 2, y - size, size, size);

		int arc = (int) (5 * sizeFactor);
		g.setColor(station.isUnderground() ? Color.RED : Color.YELLOW);
		g.fillRoundRect(x - size, y - size / 2, size * 2, size, arc, arc);
		g.setColor(Color.BLACK);
		g.drawRoundRect(x - size, y - size / 2, size * 2, size, arc, arc);

		g.setColor(Color.BLACK);
		drawCenteredString(g, station.getId() + "", x, y, (float) (sizeFactor * 10), null);
	}

	/**
	 * Draws all names of the given players onto the given graphics below their
	 * current stations.
	 * 
	 * @param g
	 *            The graphics to draw the player names on
	 */
	public void drawPlayers(Graphics2D g, ArrayList<Player> players) {
		g.setColor(Color.BLACK);
		if (players == null) {
			return;
		}
		ArrayList<ArrayList<Player>> playerListList = new ArrayList<ArrayList<Player>>();
		outer: for (Player player : players) {
			for (ArrayList<Player> arrayList : playerListList) {
				if (arrayList.get(0).getCurrentStation() == player.getCurrentStation()) {
					if (player.getCurrentStation() != null) {
						arrayList.add(player);
					}
					continue outer;
				}
			}
			if (player.getCurrentStation() != null) {
				ArrayList<Player> newList = new ArrayList<Player>();
				newList.add(player);
				playerListList.add(newList);
			}
		}
		for (ArrayList<Player> arrayList : playerListList) {
			double sizeFactor = arrayList.get(0).getCurrentStation() == hoveredStation ? 1.5 : 1;
			for (int i = 0; i < arrayList.size(); i++) {
				Player player = arrayList.get(i);
				drawCenteredString(g, player.getName(), player.getCurrentStation().getPos().getDrawX(width),
						(int) (player.getCurrentStation().getPos().getDrawY(height) + (i + 1) * 17 * sizeFactor),
						(float) (0.01 * avgSize * sizeFactor), new Color(1, 1, 1, 0.8f));
			}
		}
	}

	/**
	 * Draws the given string onto the given graphics with the given background
	 * color. If the background color is null, no background will be drawn.
	 * 
	 * @param g
	 *            The graphics to draw the string on
	 * @param string
	 *            The string to draw
	 * @param x
	 *            The center-x-coordinate
	 * @param y
	 *            The center-y-coordinate
	 * @param background
	 */
	private void drawCenteredString(Graphics2D g, String string, int x, int y, float size, Color background) {
		g.setFont(g.getFont().deriveFont(size));
		int width = g.getFontMetrics().stringWidth(string);
		int height = g.getFontMetrics().getHeight();
		Rectangle stringRect = new Rectangle(x - width / 2, y - height / 2, width, height);
		if (drawRect != null && !drawRect.intersects(stringRect)) {
			return;
		}
		Color colorSave = g.getColor();
		if (background != null) {
			g.setColor(background);
			g.fillRect(stringRect.x, stringRect.y, stringRect.width, stringRect.height);
		}
		g.setColor(colorSave);
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

	public void setHoveredStation(Station hoveredStation) {
		this.hoveredStation = hoveredStation;
	}

	/**
	 * Sets the rectangle outside which the map should not be drawn.<br>
	 * If {@code drawRect} is {@code null} everything is drawn.
	 */
	public void setDrawRect(Rectangle drawRect) {
		this.drawRect = drawRect;
	}
}
