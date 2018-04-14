package de.misterY.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
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
	private Vector2D mousePos;
	private Station hoveredStation;
	private Runnable stationClickedRunnable;
	private AffineTransform at = new AffineTransform();

	public Canvas() {
		MouseAdapter mouseAdapter = new MouseAdapter() {
			private double currentX;
			private double currentY;
			private double previousX;
			private double previousY;
			private double mouseX;
			private double mouseY;
			private double zoom = 1;

			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				calculateMousePos();
				calculateHoveredStation();
				repaint();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				currentX += (e.getX() - mouseX) / zoom;
				currentY += (e.getY() - mouseY) / zoom;
				mouseX = e.getX();
				mouseY = e.getY();
				calculateCurrentTransform();
				calculateMousePos();
				calculateHoveredStation();
				repaint();
			}

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
					zoom = Math.min(10, Math.max(0.7, zoom + 0.2 * zoom * -e.getWheelRotation()));
				}
				calculateCurrentTransform();
				repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (stationClickedRunnable != null && hoveredStation != null) {
					stationClickedRunnable.run();
				}
			}

			private void calculateCurrentTransform() {
				at.translate(-previousX, -previousY);
				Point2D p = getTranslatedPoint(mouseX, mouseY);
				at.setToIdentity();
				at.translate(mouseX, mouseY);
				at.scale(zoom, zoom);
				at.translate(-p.getX(), -p.getY());
				at.translate(currentX, currentY);
				previousX = currentX;
				previousY = currentY;
			}

			private void calculateMousePos() {
				Point2D mouse = getTranslatedPoint(mouseX, mouseY);
				mousePos = new Vector2D(mouse.getX() / getWidth(), mouse.getY() / getHeight());
			}

			private Point2D getTranslatedPoint(double panelX, double panelY) {
				Point2D point2d = new Point2D.Double(panelX, panelY);
				try {
					return at.inverseTransform(point2d, null);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}

			}

		};
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		addMouseWheelListener(mouseAdapter);
	}

	@Override
	protected void paintComponent(Graphics gg) {
		Graphics2D g = (Graphics2D) gg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.clearRect(0, 0, getWidth(), getHeight());
		g.transform(at);
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
