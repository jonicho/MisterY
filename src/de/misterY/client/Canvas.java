package de.misterY.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import de.misterY.Link;
import de.misterY.Map;
import de.misterY.Station;
import de.misterY.Vector2D;

public class Canvas extends JPanel {
	private static final long serialVersionUID = 1L;

	private Map map;
	private double scale = 1;
	private int x = 0;
	private int y = 0;
	private int mouseX;
	private int mouseY;

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
				x = (int) (mouseX * ((lastScale / scale) - 1) + (lastScale / scale) * x);
				y = (int) (mouseY * ((lastScale / scale) - 1) + (lastScale / scale) * y);
				lastScale = scale;
				repaint();
			}
		});
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				x += e.getX() - mouseX;
				y += e.getY() - mouseY;
				mouseX = e.getX();
				mouseY = e.getY();
				repaint();
			}
		});
	}

	@Override
	protected void paintComponent(Graphics gg) {
		Graphics2D g = (Graphics2D) gg;
		g.clearRect(0, 0, getWidth(), getHeight());

		g.scale(scale, scale);
		g.translate(x, y);

		g.setColor(new Color(200, 200, 200));
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.BLACK);
		drawMap(g);
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
	private void drawArrow(Graphics2D g, Vector2D pos, Vector2D vec) {
		vec.setLength(0.03);
		pos.add(vec);
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
		int x = station.getPos().getDrawX(getWidth());
		int y = station.getPos().getDrawY(getHeight());
		int size = 10;
		g.fillOval(x - size / 2, y - size / 2, size, size);
	}

	public void setMap(Map map) {
		this.map = map;
	}
}
