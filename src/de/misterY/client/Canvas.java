package de.misterY.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import de.misterY.Link;
import de.misterY.Map;
import de.misterY.Player;
import de.misterY.Station;
import de.misterY.Vector2D;

public class Canvas extends JPanel {
	private static final long serialVersionUID = 1L;

	private Map map;
	private ArrayList<Player> players;
	private double scale = 1;
	private double x = 0;
	private double y = 0;
	private int mouseX;
	private int mouseY;
	private Vector2D mousePos;
	private Station hoveredStation;
	private Runnable stationClickedRunnable;

	public Canvas() {
		addMouseWheelListener(new MouseWheelListener() {
			private int scaleSteps = 0;
			private double lastScale = 1;

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				scaleSteps -= e.getWheelRotation();
				if (scaleSteps > 10)
					scaleSteps = 10;
				if (scaleSteps < 0)
					scaleSteps = 0;
				scale = 1 + 0.2 * scaleSteps;
				x = (int) ((mouseX - x / scale) * ((lastScale / scale) - 1) + (lastScale / scale) * x);
				y = (int) ((mouseY - y / scale) * ((lastScale / scale) - 1) + (lastScale / scale) * y);
				lastScale = scale;
				repaint();
			}
		});
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				repaint();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				x += (e.getX() - mouseX) / scale;
				y += (e.getY() - mouseY) / scale;
				mouseX = e.getX();
				mouseY = e.getY();
				repaint();
			}
		});
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (stationClickedRunnable != null) {
					calculateHoveredStation();
					stationClickedRunnable.run();
				}
			}
		});
	}

	@Override
	protected void paintComponent(Graphics gg) {
		mousePos = new Vector2D((mouseX / scale - x) / getWidth(), (mouseY / scale - y) / getHeight());
		Graphics2D g = (Graphics2D) gg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.clearRect(0, 0, getWidth(), getHeight());

		g.scale(scale, scale);
		g.translate(x, y);

		g.setColor(new Color(200, 200, 200));
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.BLACK);
		drawMap(g);
		g.setColor(Color.BLACK);
		drawPlayers(g);
	}

	/**
	 * Draws the map onto the given graphics.<br>
	 * If the map is null, this method does nothing.
	 * 
	 * @param g
	 *            The graphics on which is drawn
	 */
	private void drawMap(Graphics2D g) {
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
			int x1 = station.getPos().getDrawX(getWidth());
			int y1 = station.getPos().getDrawY(getHeight());
			int x2 = link.getStation().getPos().getDrawX(getWidth());
			int y2 = link.getStation().getPos().getDrawY(getHeight());
			g.drawLine(x1, y1, x2, y2);
			Vector2D pos = link.getStation().getPos().getClone();
			Vector2D vec = station.getPos().getClone().subtract(link.getStation().getPos());
			g.setColor(Color.YELLOW);
			drawArrow(g, pos, vec, 0.02);
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
		g.drawLine(pos.getDrawX(getWidth()), pos.getDrawY(getHeight()), left.getDrawX(getWidth()),
				left.getDrawY(getHeight()));
		g.drawLine(pos.getDrawX(getWidth()), pos.getDrawY(getHeight()), right.getDrawX(getWidth()),
				right.getDrawY(getHeight()));
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
		int x = station.getPos().getDrawX(getWidth());
		int y = station.getPos().getDrawY(getHeight());
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
	 * Draws all player names onto the given graphics
	 * 
	 * @param g
	 *            The graphics to draw the player names on
	 */
	private void drawPlayers(Graphics2D g) {
		if (players == null) {
			return;
		}
		for (Player player : players) {
			drawCenteredString(g, player.getName(), player.getCurrentStation().getPos().getDrawX(getWidth()),
					player.getCurrentStation().getPos().getDrawY(getHeight()));
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
	
	private void calculateHoveredStation() {
		for (Station station : map.getStations()) {
			if (mousePos.getDistance(station.getPos()) < 0.01) {
				hoveredStation = station;
				return;
			}
		}
		hoveredStation = null;
	}
	
	public void setStationClickedRunnable(Runnable stationClickedRunnable) {
		this.stationClickedRunnable = stationClickedRunnable;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
	public Station getHoveredStation() {
		return hoveredStation;
	}
}
