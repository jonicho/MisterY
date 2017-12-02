package de.misterY.client;

import java.awt.Graphics;

import javax.swing.JPanel;

import de.misterY.Map;

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
	}
	
	public void setMap(Map map) {
		this.map = map;
	}
}
