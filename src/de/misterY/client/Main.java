package de.misterY.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import de.misterY.net.PROTOCOL;

public class Main {

	private JFrame frame;
	private GameClient gameClient;
	private JLabel infoLabel;
	private Canvas canvas;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
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

		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);

		JMenuItem mntmLogin = new JMenuItem("Login");
		mntmLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		mnOptions.add(mntmLogin);

		JMenuItem mntmReady = new JMenuItem("Ready");
		mntmReady.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameClient.send(PROTOCOL.CS.READY); // TODO
			}
		});
		mnOptions.add(mntmReady);

		JSplitPane splitPane = new JSplitPane();
		panel_1.add(splitPane, BorderLayout.CENTER);
		splitPane.setResizeWeight(0.7);

		canvas = new Canvas();
		splitPane.setLeftComponent(canvas);

		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0 };
		gbl_panel.rowHeights = new int[] { 0 };
		gbl_panel.columnWeights = new double[] { Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		infoLabel = new JLabel("New label");
		panel_1.add(infoLabel, BorderLayout.SOUTH);

		connect();
	}

	/**
	 * Connects to server using a separate thread to avoid blocking the gui.
	 */
	private void connect() {
		new Thread(() -> {
			infoLabel.setForeground(Color.BLACK);
			infoLabel.setText("Connecting...");
			gameClient = new GameClient("localhost", PROTOCOL.PORT);
			if (gameClient.isConnected()) {
				infoLabel.setText("Connected.");
				createUpdateRunnable();
			} else {
				infoLabel.setForeground(Color.RED);
				infoLabel.setText("Connection failed!");
				int o = JOptionPane.showConfirmDialog(frame, "Connection failed! Retry?", "Connection failed!",
						JOptionPane.YES_NO_OPTION);
				if (o == JOptionPane.OK_OPTION) {
					connect();
				}
			}
		}).start();
	}

	/**
	 * Creates an update runnable and gives it to the game client to let the gui
	 * react to incoming messages
	 */
	private void createUpdateRunnable() {
		gameClient.setUpdateRunnable(() -> {
			if (gameClient.getMap() != null) {
				canvas.setMap(gameClient.getMap());
			}
			if (gameClient.getPlayers() != null) {
				canvas.setPlayers(gameClient.getPlayers());
			}
			canvas.repaint();
		});
	}

	/**
	 * Asks the user to enter a username and logs in using that usename.
	 */
	private void login() {
		if (!gameClient.isConnected()) {
			JOptionPane.showMessageDialog(frame, "You have to be connected to the server!");
			return;
		}
		String username = JOptionPane.showInputDialog(frame, "Enter your user name:");
		gameClient.send(PROTOCOL.buildMessage(PROTOCOL.CS.LOGIN, username));
		infoLabel.setForeground(Color.BLACK);
		infoLabel.setText("Logged in.");
	}
}
