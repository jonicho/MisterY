package de.misterY.client;

import java.awt.Graphics;

import javax.swing.JPanel;

import de.misterY.Link;
import de.misterY.Map;
import de.misterY.Station;

public class Canvas extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Map map;

	@Override
	protected void paintComponent(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		drawMap(g);
	}
	
	private void drawMap(Graphics g) {
		if (map == null) {
			return;
		}
		for (Station station : map.getStations()) {
			for (Link link : station.getLinks()) {
				int x1 = (int) (station.getX() * getWidth());
				int y1 = (int) (station.getY() * getHeight());
				int x2 = (int) (link.getStation().getX() * getWidth());
				int y2 = (int) (link.getStation().getY() * getHeight());
				g.drawLine(x1, y1, x2, y2);
			}
		}
		for (Station station : map.getStations()) {
			int x = (int) (station.getX() * getWidth());
			int y = (int) (station.getY() * getHeight());
			int size = 10;
			g.fillOval(x - size / 2, y - size / 2, size, size);
		}
	}
	
	public void setMap(Map map) {
		this.map = map;
	}
}
