package de.misterY.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import de.misterY.MeansOfTransportation;
import de.misterY.Player;
import de.misterY.language.LANGUAGE;
import de.misterY.net.PROTOCOL;
import de.misterY.pathfinding.PathFinder;

public class Client {

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
	private JMenu mnOptions;
	private JMenuItem mntmReady;
	private JMenu mnLanguage;
	private JMenuItem mntmEnglish;
	private JMenuItem mntmGerman;
	private JMenuItem mntmLogin;
	private JLabel lblChat;
	private JLabel lblRoundsInfo;
	private JButton btnSend;
	private JLabel lblPlayerInfo;
	private JButton btnSkip;
	private JMenuItem mntmConnect;
	private String server;

	/**
	 * Create the application.
	 */
	public Client() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		LANGUAGE.loadLanguage(Locale.getDefault().getLanguage());

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

		mnOptions = new JMenu(LANGUAGE.OPTIONS);
		menuBar.add(mnOptions);

		mntmLogin = new JMenuItem(LANGUAGE.LOGIN);
		mntmLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});

		mntmConnect = new JMenuItem(LANGUAGE.CONNECT);
		mntmConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connect();
			}
		});
		mnOptions.add(mntmConnect);
		mnOptions.add(mntmLogin);

		mntmReady = new JMenuItem(LANGUAGE.READY);
		mntmReady.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameClient.send(PROTOCOL.CS.READY);
			}
		});
		mnOptions.add(mntmReady);

		mnLanguage = new JMenu(LANGUAGE.STR_LANGUAGE);
		menuBar.add(mnLanguage);

		mntmEnglish = new JMenuItem(LANGUAGE.STR_ENGLISH);
		mntmEnglish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateLanguage(LANGUAGE.EN);
			}
		});
		mnLanguage.add(mntmEnglish);

		mntmGerman = new JMenuItem(LANGUAGE.STR_GERMAN);
		mntmGerman.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateLanguage(LANGUAGE.DE);
			}
		});
		mnLanguage.add(mntmGerman);

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
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
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

		lblChat = new JLabel(LANGUAGE.CHAT + ":");
		lblChat.setHorizontalAlignment(SwingConstants.CENTER);
		lblChat.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc_lblChat = new GridBagConstraints();
		gbc_lblChat.insets = new Insets(0, 0, 5, 5);
		gbc_lblChat.gridx = 0;
		gbc_lblChat.gridy = 2;
		panel.add(lblChat, gbc_lblChat);

		lblRoundsInfo = new JLabel(LANGUAGE.ROUNDSINFO + ":");
		lblRoundsInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblRoundsInfo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc_lblRoundsInfo = new GridBagConstraints();
		gbc_lblRoundsInfo.insets = new Insets(0, 0, 5, 0);
		gbc_lblRoundsInfo.gridx = 1;
		gbc_lblRoundsInfo.gridy = 2;
		panel.add(lblRoundsInfo, gbc_lblRoundsInfo);

		roundsTable = new JTable();
		roundsTable.setPreferredScrollableViewportSize(new Dimension(1, 1));
		roundsTable.setFocusable(false);

		JScrollPane scrollPane_2 = new JScrollPane(roundsTable);
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_2.gridy = 3;
		gbc_scrollPane_2.gridx = 1;
		panel.add(scrollPane_2, gbc_scrollPane_2);

		chatTextPane = new JTextPane();
		chatTextPane.setEditable(false);
		chatTextPane.setContentType("text/html");

		JScrollPane scrollPane_1 = new JScrollPane(chatTextPane);
		chatTextPane.setCaretPosition(chatTextPane.getDocument().getLength());
		chatTextPane.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				chatTextPane.setCaretPosition(chatTextPane.getDocument().getLength());
				System.out.println("cha");
			}
		});
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridy = 3;
		gbc_scrollPane_1.gridx = 0;
		panel.add(scrollPane_1, gbc_scrollPane_1);

		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(0, 0, 5, 5);
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

		btnSend = new JButton(LANGUAGE.SEND);
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		panel_4.add(btnSend, BorderLayout.EAST);

		lblPlayerInfo = new JLabel(LANGUAGE.PLAYERINFO);
		GridBagConstraints gbc_lblPlayerInfo = new GridBagConstraints();
		gbc_lblPlayerInfo.insets = new Insets(0, 0, 5, 0);
		gbc_lblPlayerInfo.gridwidth = 2;
		gbc_lblPlayerInfo.fill = GridBagConstraints.BOTH;
		gbc_lblPlayerInfo.gridy = 0;
		gbc_lblPlayerInfo.gridx = 0;
		panel.add(lblPlayerInfo, gbc_lblPlayerInfo);
		lblPlayerInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayerInfo.setFont(new Font("Tahoma", Font.PLAIN, 20));

		btnSkip = new JButton(LANGUAGE.SKIP);
		btnSkip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				skipTurn();
			}
		});
		GridBagConstraints gbc_btnSkip = new GridBagConstraints();
		gbc_btnSkip.insets = new Insets(0, 0, 0, 5);
		gbc_btnSkip.gridx = 1;
		gbc_btnSkip.gridy = 4;
		panel.add(btnSkip, gbc_btnSkip);

		infoLabel = new JLabel("");
		panel_1.add(infoLabel, BorderLayout.SOUTH);

		frame.setVisible(true);
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
		if (gameClient != null && gameClient.isConnected()) {
			JOptionPane.showMessageDialog(frame, LANGUAGE.ALREADYCONNECTEDTO + server);
			return;
		}
		String input = JOptionPane.showInputDialog(frame, LANGUAGE.ENTERIP, PROTOCOL.IP);
		new Thread(() -> {
			infoLabel.setForeground(Color.BLACK);
			infoLabel.setText(LANGUAGE.CONNECTING + "...");
			gameClient = new GameClient(input, PROTOCOL.PORT);
			if (gameClient.isConnected()) {
				infoLabel.setText(LANGUAGE.CONNECTED + ". " + input);
				createUpdateRunnable();
				createErrorRunnable();
				createChatRunnable();
				createStationClickedRunnable();
				server = input;
			} else {
				infoLabel.setForeground(Color.RED);
				infoLabel.setText(LANGUAGE.CONNECTIONFAILED + "!");
				JOptionPane.showMessageDialog(frame, LANGUAGE.CONNECTIONFAILED + "!", LANGUAGE.CONNECTIONFAILED,
						JOptionPane.WARNING_MESSAGE);
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
					JOptionPane.showMessageDialog(frame, LANGUAGE.MISTERYWON + "!");
				} else {
					JOptionPane.showMessageDialog(frame, LANGUAGE.DETECTIVESWON + " - "
							+ gameClient.getWinner().getName() + " " + LANGUAGE.FOUNDMISTERY + "!");
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
				JOptionPane.showMessageDialog(frame, LANGUAGE.USERNAMEALREADYINUSE, "Error", JOptionPane.ERROR_MESSAGE);
				updateTitle("");
				login();
				return;
			} else if (errorCode == PROTOCOL.ERRORCODES.INVALID_MOVEMENT) {
				JOptionPane.showMessageDialog(frame, LANGUAGE.MOVEMENTISINVALID, LANGUAGE.INVALIDMOVEMENT,
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(frame, LANGUAGE.ERROROCURRED + " " + LANGUAGE.ERRORCODE + " " + errorCode,
					LANGUAGE.ERROR, JOptionPane.ERROR_MESSAGE);
		});
	}

	/**
	 * Creates a runnable that updates the chat when a new message arrived
	 */
	private void createChatRunnable() {
		gameClient.setChatRunnable(() -> {
			chatTextPane.setText(gameClient.getChatHandler().getChatString());
		});
	}

	/**
	 * Creates a runnable that processes a click on a station
	 */
	private void createStationClickedRunnable() { // TODO don't check whether the movement is valid, let the server do that
		canvas.setStationClickedRunnable(() -> {
			if (gameClient.isFinished()) {
				return;
			}
			if (!gameClient.isStarted()) {
				JOptionPane.showMessageDialog(frame, LANGUAGE.GAMENOTSTARTEDYET, LANGUAGE.GAMENOTSTARTEDYET,
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if (!gameClient.getPlayerByName(ownName).isTurn()) {
				JOptionPane.showMessageDialog(frame,
						LANGUAGE.ITIS + " " + gameClient.getCurrentPlayer().getName() + LANGUAGE.PLAYERSTURN,
						LANGUAGE.NOTYOURTURN, JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			MeansOfTransportation[] possibleMOT = PathFinder.getPossibleMeansOfTransportation(
					gameClient.getPlayerByName(ownName).getCurrentStation(), canvas.getHoveredStation());
			MeansOfTransportation selection = null;
			if (possibleMOT.length != 0) {
				selection = (MeansOfTransportation) JOptionPane.showInputDialog(frame,
						LANGUAGE.CHOOSEMEANSOFTRANSPORTATION, "", JOptionPane.QUESTION_MESSAGE, null, possibleMOT,
						possibleMOT[0]);
				if (selection == null) { // user canceled
					return;
				}
			}
			if (possibleMOT.length == 0
					|| !gameClient.getPlayerByName(ownName).validateMovement(canvas.getHoveredStation(), selection)) {
				JOptionPane.showMessageDialog(frame, LANGUAGE.MOVEMENTISINVALID, LANGUAGE.INVALIDMOVEMENT,
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
		if (gameClient == null) {
			return;
		}
		ArrayList<Player> players = gameClient.getPlayers();
		if (players.isEmpty()) {
			return;
		}
		String[] columnNames = { LANGUAGE.NAME, LANGUAGE.TAXITICKETS, LANGUAGE.BUSTICKETS, LANGUAGE.UNDERGROUNDTICKETS,
				LANGUAGE.MRY, LANGUAGE.TURN };
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
		if (gameClient == null || gameClient.getMap() == null) {
			return;
		}
		MeansOfTransportation[] ticketsUsed = gameClient.getTicketsUsedByMisterY();
		String[] columnNames = { LANGUAGE.ROUND, LANGUAGE.MISTERYSHOWS, LANGUAGE.MISTERYUSEDTICKETS };
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
			JOptionPane.showMessageDialog(frame, LANGUAGE.ALREADYLOGGEDIN);
			return;
		}
		if (!gameClient.isConnected()) {
			JOptionPane.showMessageDialog(frame, LANGUAGE.CONNECTIONNEEDED);
			return;
		}
		String username = JOptionPane.showInputDialog(frame, LANGUAGE.ENTERUSERNAME);
		if (username == null) {
			return;
		}
		ownName = username;
		gameClient.send(PROTOCOL.buildMessage(PROTOCOL.CS.LOGIN, username));
		infoLabel.setForeground(Color.BLACK);
		infoLabel.setText(LANGUAGE.LOGGEDIN);
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

	/**
	 * Requests a turn skip if it's this players turn
	 */
	private void skipTurn() {
		if (gameClient.getCurrentPlayer() != gameClient.getPlayerByName(ownName)) {
			return;
		}
		gameClient.send(PROTOCOL.buildMessage(PROTOCOL.CS.SKIP_TURN));
	}

	/**
	 * Updates the gui strings with the given language
	 * 
	 * @param lang
	 */
	private void updateLanguage(String lang) {
		LANGUAGE.loadLanguage(lang);

		mnOptions.setText(LANGUAGE.OPTIONS);
		mntmLogin.setText(LANGUAGE.LOGIN);
		mntmReady.setText(LANGUAGE.READY);
		mnLanguage.setText(LANGUAGE.STR_LANGUAGE);
		mntmEnglish.setText(LANGUAGE.STR_ENGLISH);
		mntmGerman.setText(LANGUAGE.STR_GERMAN);
		lblChat.setText(LANGUAGE.CHAT + ":");
		lblRoundsInfo.setText(LANGUAGE.ROUNDSINFO + ":");
		btnSend.setText(LANGUAGE.SEND);
		lblPlayerInfo.setText(LANGUAGE.PLAYERINFO);
		btnSkip.setText(LANGUAGE.SKIP);
		mntmConnect.setText(LANGUAGE.CONNECT);

		updatePlayersTable();
		updateRoundsTable();
	}

}
