package de.misterY.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import de.misterY.MeansOfTransportation;
import de.misterY.Player;
import de.misterY.net.PROTOCOL;

public class Main {

	private JFrame frame;
	private GameClient gameClient;
	private JLabel infoLabel;
	private Canvas canvas;
	private JTable playersTable;
	private String ownName;

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

		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);

		JMenuItem mntmLogin = new JMenuItem("Login");
		mntmLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		mnOptions.add(mntmLogin);

		JMenuItem mntmReady = new JMenuItem("Ready");
		mntmReady.addActionListener(new ActionListener() {
			@Override
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
		panel.setLayout(new GridLayout(1, 1, 0, 0));

		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		JLabel lblPlayerInfo = new JLabel("Player info:");
		lblPlayerInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayerInfo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel_2.add(lblPlayerInfo, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		panel_2.add(scrollPane);

		playersTable = new JTable();
		scrollPane.setViewportView(playersTable);
		playersTable.setDefaultEditor(Object.class, null);
		playersTable.setFocusable(false);

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
				createErrorRunnable();
				createStationClickedRunnable();
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
			updatePlayersTable();
		});
	}

	/**
	 * Creates an update runnable and gives it to the game client to let the gui
	 * react to incoming error messages
	 */
	private void createErrorRunnable() {
		gameClient.setErrorRunnable(() -> {
			int errorCode = gameClient.getErrorCode();
			if (errorCode == PROTOCOL.ERRORCODES.USERNAME_ALREADY_IN_USE) {
				ownName = null;
				JOptionPane.showMessageDialog(frame, "This username is already in use! Please take another one.",
						"Error", JOptionPane.ERROR_MESSAGE);
				login();
				return;
			}
			JOptionPane.showMessageDialog(frame, "An error ocurred. Errorcode: " + errorCode, "Error",
					JOptionPane.ERROR_MESSAGE);
		});
	}

	private void createStationClickedRunnable() {
		canvas.setStationClickedRunnable(() -> {
			if (!gameClient.isStarted()) {
				JOptionPane.showMessageDialog(frame, "The game has not started yet!", "The game has not started yet!",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if (!gameClient.getPlayerByName(ownName).isTurn()) {
				JOptionPane.showMessageDialog(frame, "It is " + gameClient.getCurrentPlayer().getName() + "'s turn!",
						"It is not your turn!", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			MeansOfTransportation selection = (MeansOfTransportation) JOptionPane.showInputDialog(frame,
					"Choose a means of transportation", "", JOptionPane.QUESTION_MESSAGE, null,
					MeansOfTransportation.values(), MeansOfTransportation.Taxi);
			if (selection == null) { // user canceled
				return;
			}
			if (!gameClient.getPlayerByName(ownName).validateMovement(canvas.getHoveredStation(), selection)) {
				JOptionPane.showMessageDialog(frame, "This movement is invalid!", "Invalid movement!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			gameClient.send(PROTOCOL.buildMessage(PROTOCOL.CS.REQUEST_MOVEMENT, canvas.getHoveredStation().getId() + "", selection + ""));
		});
	}

	/**
	 * Updates the players table
	 */
	private void updatePlayersTable() {
		ArrayList<Player> players = gameClient.getPlayers();
		if (players.isEmpty()) {
			return;
		}
		String[] columnNames = { "Name", "Taxi tickets", "Bus tickets", "Underground tickets", "MrY" };
		String[][] data = new String[players.size()][5];
		int thisPlayerIndex = 0;
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			data[i][0] = player.getName();
			data[i][1] = player.getTaxiTickets() + "";
			data[i][2] = player.getBusTickets() + "";
			data[i][3] = player.getUndergroundTickets() + "";
			data[i][4] = player.isMrY() ? "X" : "";
			if (ownName.equals(player.getName())) {
				thisPlayerIndex = i;
			}
		}
		playersTable.setModel(new DefaultTableModel(data, columnNames));
		playersTable.setRowSelectionInterval(thisPlayerIndex, thisPlayerIndex);
		for (MouseListener l : playersTable.getMouseListeners()) {
			playersTable.removeMouseListener(l);
		}
		final int index = thisPlayerIndex;
		playersTable.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				playersTable.setRowSelectionInterval(index, index);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				playersTable.setRowSelectionInterval(index, index);
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
	}

	/**
	 * Asks the user to enter a user name and logs in using that user name.
	 */
	private void login() {
		if (ownName != null) {
			JOptionPane.showMessageDialog(frame, "You are already logged in!");
			return;
		}
		if (!gameClient.isConnected()) {
			JOptionPane.showMessageDialog(frame, "You have to be connected to the server!");
			return;
		}
		String username = JOptionPane.showInputDialog(frame, "Enter your user name:");
		if (username == null) {
			return;
		}
		ownName = username;
		gameClient.send(PROTOCOL.buildMessage(PROTOCOL.CS.LOGIN, username));
		infoLabel.setForeground(Color.BLACK);
		infoLabel.setText("Logged in.");
	}
}
