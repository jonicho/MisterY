package de.misterY.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
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
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import de.misterY.MeansOfTransportation;
import de.misterY.Player;
import de.misterY.net.PROTOCOL;
import de.misterY.pathfinding.PathFinder;

public class Main {

	private JFrame frame;
	private GameClient gameClient;
	private JLabel infoLabel;
	private Canvas canvas;
	private JTable playersTable;
	private String ownName;
	private JTextField chatTextField;
	private JTextPane chatTextPane;
	private JScrollPane scrollPane;
	private JTable roundsTable;

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
		updateTitle("");
		frame.setBounds(100, 100, 1080, 720);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
				gameClient.send(PROTOCOL.CS.READY);
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
		gbl_panel.rowWeights = new double[] { 0.0, 1.0, 0.0, 5.0, 0.0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 1.0 };
		gbl_panel.columnWidths = new int[] { 0, 0 };
		panel.setLayout(gbl_panel);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridy = 1;
		gbc_scrollPane.gridx = 0;
		panel.add(scrollPane, gbc_scrollPane);

		playersTable = new JTable();
		playersTable.setPreferredScrollableViewportSize(new Dimension(1, 1));
		scrollPane.setViewportView(playersTable);
		playersTable.setDefaultEditor(Object.class, null);
		playersTable.setFocusable(false);

		JLabel lblChat = new JLabel("Chat:");
		lblChat.setHorizontalAlignment(SwingConstants.CENTER);
		lblChat.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc_lblChat = new GridBagConstraints();
		gbc_lblChat.gridx = 0;
		gbc_lblChat.gridy = 2;
		panel.add(lblChat, gbc_lblChat);

		JLabel lblRoundsInfo = new JLabel("Rounds info:");
		lblRoundsInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblRoundsInfo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc_lblRoundsInfo = new GridBagConstraints();
		gbc_lblRoundsInfo.gridx = 1;
		gbc_lblRoundsInfo.gridy = 2;
		panel.add(lblRoundsInfo, gbc_lblRoundsInfo);

		roundsTable = new JTable();
		roundsTable.setPreferredScrollableViewportSize(new Dimension(1, 1));
		roundsTable.setFocusable(false);

		JScrollPane scrollPane_2 = new JScrollPane(roundsTable);
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.gridheight = 2;
		gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_2.gridy = 3;
		gbc_scrollPane_2.gridx = 1;
		panel.add(scrollPane_2, gbc_scrollPane_2);

		chatTextPane = new JTextPane();
		chatTextPane.setEditable(false);
		chatTextPane.setContentType("text/html");

		JScrollPane scrollPane_1 = new JScrollPane(chatTextPane);
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridy = 3;
		gbc_scrollPane_1.gridx = 0;
		panel.add(scrollPane_1, gbc_scrollPane_1);

		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridy = 4;
		gbc_panel_4.gridx = 0;
		panel.add(panel_4, gbc_panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));

		chatTextField = new JTextField();
		chatTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		panel_4.add(chatTextField);
		chatTextField.setColumns(10);

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		panel_4.add(btnSend, BorderLayout.EAST);

		JLabel lblPlayerInfo = new JLabel("Player info:");
		GridBagConstraints gbc_lblPlayerInfo = new GridBagConstraints();
		gbc_lblPlayerInfo.gridwidth = 2;
		gbc_lblPlayerInfo.fill = GridBagConstraints.BOTH;
		gbc_lblPlayerInfo.gridy = 0;
		gbc_lblPlayerInfo.gridx = 0;
		panel.add(lblPlayerInfo, gbc_lblPlayerInfo);
		lblPlayerInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayerInfo.setFont(new Font("Tahoma", Font.PLAIN, 20));

		infoLabel = new JLabel("New label");
		panel_1.add(infoLabel, BorderLayout.SOUTH);

		connect();
	}

	/**
	 * Updates the title with the given user name in the following way: "MisterY" +
	 * (username.isEmpty() ? "" : " - " + username)
	 * 
	 * @param username
	 */
	private void updateTitle(String username) {
		frame.setTitle("MisterY" + (username.isEmpty() ? "" : " - " + username));
	}

	/**
	 * Connects to server using a separate thread to avoid blocking the gui.
	 */
	private void connect() {
		new Thread(() -> {
			infoLabel.setForeground(Color.BLACK);
			infoLabel.setText("Connecting...");
			gameClient = new GameClient();
			if (gameClient.isConnected()) {
				infoLabel.setText("Connected.");
				createUpdateRunnable();
				createErrorRunnable();
				createChatRunnable();
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
			updateRoundsTable();
			if (gameClient.getWinner() != null) {
				if (gameClient.getWinner().isMrY()) {
					JOptionPane.showMessageDialog(frame, "MisterY won the game!");
				} else {
					JOptionPane.showMessageDialog(frame,
							"The detectives won the game - " + gameClient.getWinner().getName() + " found MisterY!");
				}
			}
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
				updateTitle("");
				login();
				return;
			} else if (errorCode == PROTOCOL.ERRORCODES.INVALID_MOVEMENT) {
				JOptionPane.showMessageDialog(frame, "This movement is invalid!", "Invalid movement!",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(frame, "An error ocurred. Errorcode: " + errorCode, "Error",
					JOptionPane.ERROR_MESSAGE);
		});
	}

	private void createChatRunnable() {
		gameClient.setChatRunnable(() -> {
			chatTextPane.setText(gameClient.getChatHandler().getChatString());
		});
	}

	private void createStationClickedRunnable() {
		canvas.setStationClickedRunnable(() -> {
			if (gameClient.isFinished()) {
				return;
			}
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
			MeansOfTransportation[] possibleMOT = PathFinder.getPossibleMeansOfTransportation(
					gameClient.getPlayerByName(ownName).getCurrentStation(), canvas.getHoveredStation());
			MeansOfTransportation selection = null;
			if (possibleMOT.length != 0) {
				selection = (MeansOfTransportation) JOptionPane.showInputDialog(frame,
						"Choose a means of transportation", "", JOptionPane.QUESTION_MESSAGE, null, possibleMOT,
						possibleMOT[0]);
				if (selection == null) { // user canceled
					return;
				}
			}
			if (possibleMOT.length == 0
					|| !gameClient.getPlayerByName(ownName).validateMovement(canvas.getHoveredStation(), selection)) {
				JOptionPane.showMessageDialog(frame, "This movement is invalid!", "Invalid movement!",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			gameClient.send(PROTOCOL.buildMessage(PROTOCOL.CS.REQUEST_MOVEMENT, canvas.getHoveredStation().getId(),
					selection + ""));
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
		String[] columnNames = { "Name", "Taxi tickets", "Bus tickets", "Underground tickets", "MrY", "Turn" };
		String[][] data = new String[players.size()][6];
		int thisPlayerIndex = 0;
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			data[i][0] = player.getName();
			data[i][1] = player.getTaxiTickets() + "";
			data[i][2] = player.getBusTickets() + "";
			data[i][3] = player.getUndergroundTickets() + "";
			data[i][4] = player.isMrY() ? "X" : "";
			data[i][5] = player.isTurn() ? "X" : "";
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
		Dimension d = playersTable.getPreferredSize();
		scrollPane.setPreferredSize(new Dimension(d.width, playersTable.getRowHeight() * 2));
	}

	/**
	 * Updates the rounds table.
	 */
	private void updateRoundsTable() {
		if (gameClient.getMap() == null) {
			return;
		}
		MeansOfTransportation[] ticketsUsed = gameClient.getTicketsUsedByMisterY();
		String[] columnNames = { "Round", "MisterY shows", "Ticket used by MisterY" };
		String[][] data = new String[gameClient.getMap().getRounds()][3];
		for (int i = 0; i < gameClient.getMap().getRounds(); i++) {
			data[i][0] = (i + 1) + "";
			data[i][1] = Arrays.binarySearch(gameClient.getMap().getShowRounds(), i + 1) >= 0 ? "X" : "";
			data[i][2] = ticketsUsed.length > i ? ticketsUsed[i] + "" : "";
		}
		int round = gameClient.getRound() - 1;
		roundsTable.setModel(new DefaultTableModel(data, columnNames));
		roundsTable.setRowSelectionInterval(round, round);
		for (MouseListener l : roundsTable.getMouseListeners()) {
			roundsTable.removeMouseListener(l);
		}
		final int index = round;
		roundsTable.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				roundsTable.setRowSelectionInterval(index, index);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				roundsTable.setRowSelectionInterval(index, index);
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
		updateTitle(username);
	}

	/**
	 * Sends the message that is currently in the chat text field. Sends nothing if
	 * the trimmed string is empty.
	 */
	private void sendMessage() {
		String message = chatTextField.getText().trim();
		if (message.isEmpty()) {
			return;
		}
		gameClient.send(PROTOCOL.buildMessage(PROTOCOL.CS.CHAT_POST, message));
		chatTextField.setText("");
	}
}
