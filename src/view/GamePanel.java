package view;

import gamelogic.GameHandler;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import model.Game;
import model.Game.LogItem;
import model.IRefreshable;

public class GamePanel extends JPanel implements ActionListener, IRefreshable, KeyListener {

    private static final long serialVersionUID = 1552746400473185110L;

    private JLabel statusLabel;
    private JTextField textField;

    private ChatPanel chatPanel;
    private ChatPanel logPanel;

    private FleetPanel player1Fleet;
    private FleetPanel player2Fleet;

    private FieldPanel player1Field;
    private FieldPanel player2Field;

    private JButton sendButton;
    private JButton leaveButton;

    private GameHandler gameHandler;
    private Game game;
    private BattleShipWindow parent;
    
    public GamePanel(BattleShipWindow parent, GameHandler gh) {
        this.gameHandler = gh;
        this.game = this.gameHandler.getGame();
        this.parent = parent;

        this.setLayout(null);
        // 2 FieldPanels
        player1Field = new FieldPanel(this, true);
        player2Field = new FieldPanel(this, false);

        player1Field.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        player2Field.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));

        // 2 FleetPanels; Draw dem Fleets!
        player1Fleet = new FleetPanel(true, this);
        player2Fleet = new FleetPanel(false, this);

        logPanel = new ChatPanel("Logbuch");
        chatPanel = new ChatPanel("Chat");

        // Dimension size = player1Field.getPreferredSize();
        player1Field.setBounds(160, 25, 331, 331);
        player2Field.setBounds(510, 25, 331, 331);

        player1Fleet.setBounds(25, 25, 115, 331);
        player2Fleet.setBounds(860, 25, 115, 331);

        logPanel.setBounds(25, 400, 465, 150);
        chatPanel.setBounds(510, 400, 465, 150);

        statusLabel = new JLabel("Please wait asdasdasdasdasd.", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", 0, 20));
        statusLabel.setBounds(0, 365, 1000, 25);
        add(statusLabel);
        
        JLabel player1Label = new JLabel(game.getPlayer1().getName(), SwingConstants.LEFT);
        player1Label.setFont(new Font("Arial Black", 0, 12));
        player1Label.setBounds(25, 355, 1000, 25);
        add(player1Label);
        System.out.println(game.getPlayer2().getName().length());
        
        JLabel player2Label = new JLabel(game.getPlayer2().getName(), SwingConstants.RIGHT);
        player2Label.setFont(new Font("Arial Black", 0, 12));
        player2Label.setBounds(-25, 355, 1000, 25);
        add(player2Label);

        leaveButton = new JButton("Aufgeben!");
        leaveButton.setBounds(25, 525, 100, 25);
        leaveButton.setActionCommand("leavegame");
        leaveButton.addActionListener(this);
        add(leaveButton);

        textField = new JTextField();
        textField.setBounds(510, 525, 360, 25);
        add(textField);
        textField.setColumns(10);
        textField.addKeyListener(this);

        sendButton = new JButton("Senden!");
        sendButton.setBounds(875, 525, 100, 25);
        sendButton.setActionCommand("sendmsg");
        sendButton.addActionListener(this);
        add(sendButton);

        this.add(player1Field);
        this.add(player2Field);
        this.add(player1Fleet);
        this.add(player2Fleet);
        this.add(logPanel);
        this.add(chatPanel);

        refreshUI();
    }

    public void updateChat() {
        LogItem entry = this.game.getLastChatEntry();
        LogItem last = chatPanel.getLast();
        if(entry != null && entry != last) {
            chatPanel.printEntry(entry);
        }
    }

    public void updateLog() {
        LogItem entry = this.game.getLastLogEntry();
        LogItem last = logPanel.getLast();
        if(entry != null && entry != last) {
            logPanel.printEntry(entry);
        }
    }
    
    public void morphLeaveButton() {
        this.leaveButton.setText("Spiel verlassen");
    }

    public void sendMessageButtonClicked() {
        if (!textField.getText().equals("")) {
            String message = textField.getText();
            this.gameHandler.sendChatMessage(message, game.getPlayer1());
            updateChat();
            textField.setText("");
            // this.player1Fleet.inactivate();
            this.validate();
            this.repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        switch(arg0.getActionCommand()) {
        case "sendmsg":
            sendMessageButtonClicked();
            break;
        case "leavegame":
            parent.backToMenu();
            break;
        } 
    }

    public FleetPanel getPlayer1Fleet() {
        return player1Fleet;
    }

    public FieldPanel getPlayer1Field() {
        return player1Field;
    }

    public FieldPanel getPlayer2Field() {
        return player2Field;
    }

    public Game getGame() {
        return game;
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    public void clearSelection() {
        player1Fleet.deselect();
    }

    @Override
    public void refreshUI() {
        String gameStatus = this.game.getStatusMessage();
        this.statusLabel.setText(gameStatus);

        player1Field.refreshUI();
        player2Field.refreshUI();

        player1Fleet.refreshUI();
        player1Fleet.refreshUI();

        updateChat();
        updateLog();

        validate();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            sendMessageButtonClicked();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
