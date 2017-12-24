package de.misterY.mapDesigner;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import de.misterY.Map;
import de.misterY.client.Canvas;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {

	private JFrame frame;
	private Canvas canvas;
	private JTextArea txtrMapstring;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1080, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		JMenuBar menuBar = new JMenuBar();
		panel_1.add(menuBar, BorderLayout.NORTH);

		JSplitPane splitPane = new JSplitPane();
		panel_1.add(splitPane, BorderLayout.CENTER);
		splitPane.setResizeWeight(0.7);

		canvas = new Canvas();
		splitPane.setLeftComponent(canvas);

		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));

		txtrMapstring = new JTextArea();
		txtrMapstring.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				reloadMap();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				reloadMap();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				reloadMap();
			}
		});

		scrollPane_1 = new JScrollPane(txtrMapstring);
		panel.add(scrollPane_1, BorderLayout.CENTER);
	}

	private void reloadMap() {
		try {
			canvas.setMap(new Map(txtrMapstring.getText()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		canvas.repaint();
	}
}
