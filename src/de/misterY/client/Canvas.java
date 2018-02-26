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

import de.misterY.Map;
import de.misterY.Player;
import de.misterY.Station;
import de.misterY.Vector2D;

public class Canvas extends JPanel {
	private static final long serialVersionUID = 1L;

	private Map map;
	private MapDrawer mapDrawer = new MapDrawer();
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
				calculateHoveredStation();
				repaint();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				x += (e.getX() - mouseX) / scale;
				y += (e.getY() - mouseY) / scale;
				mouseX = e.getX();
				mouseY = e.getY();
				calculateHoveredStation();
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
				if (stationClickedRunnable != null && hoveredStation != null) {
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

		mapDrawer.setMap(map);
		mapDrawer.setWidth(getWidth());
		mapDrawer.setHeight(getHeight());
		mapDrawer.setHoveredStation(hoveredStation);

		mapDrawer.drawMap(g);
		mapDrawer.drawPlayers(g, players);
	}

	private void calculateHoveredStation() {
		if (map == null) {
			return;
		}
		Station closestStation = null;
		double shortestDistance = Double.POSITIVE_INFINITY;
		for (Station station : map.getStations()) {
			if (mousePos.getDistance(station.getPos()) < shortestDistance) {
				shortestDistance = mousePos.getDistance(station.getPos());
				closestStation = station;
			}
		}
		if (shortestDistance < 0.015) {
			hoveredStation = closestStation;
		} else {
			hoveredStation = null;
		}
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
