package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import model.IRefreshable;
import model.Lobby;

public class MultiplayerLobbyPanel extends JPanel implements IRefreshable, ActionListener {

    private static final long serialVersionUID = 5678546710919898149L;

    private JLabel titleLabel;
    private JLabel subTitleLabel;
    private Table tableParent;
    
    JPanel messagePanel;
    JLabel messageLabel;
    
    private BattleShipWindow window;

	private Lobby lobby;

	private JButton backButton;

    public MultiplayerLobbyPanel(BattleShipWindow parent) {
        this.window = parent;
        setLayout(null);

        titleLabel = new JLabel("BattleShip", SwingConstants.CENTER);
        titleLabel.setBounds(0, 10, 1000, 50);
        subTitleLabel = new JLabel("Now with 103% more Lobby!", SwingConstants.CENTER);
        subTitleLabel.setBounds(0, 50, 1000, 50);

        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 32));
        subTitleLabel.setFont(new Font("Tahoma", Font.PLAIN, 22));

        add(titleLabel);
        add(subTitleLabel);
        
        JPanel topRow = new JPanel();
        topRow.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        topRow.setPreferredSize(new Dimension(400, 30));
        topRow.setMinimumSize(new Dimension(400, 30));
        topRow.setMaximumSize(new Dimension(400, 30));
        topRow.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
        
        topRow.setBounds(300, 170, 400, 30);
        topRow.setBackground(Color.LIGHT_GRAY);
        JLabel label = new JLabel("Spielername");
        topRow.add(label);
        
        add(topRow);

        tableParent = new Table(400, 30);
        tableParent.setMaximumSize(new Dimension(400, 1000));
        tableParent.setMinimumSize(new Dimension(400, 10));
        tableParent.setPreferredSize(new Dimension(400, 30));
        tableParent.setBounds(300, 200, 400, 300);

        JScrollPane pane = new JScrollPane(tableParent);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setAutoscrolls(true);
        pane.setMaximumSize(new Dimension(400, 300));

        pane.setSize(400, 300);
        pane.setLocation(300, 200);
        pane.setPreferredSize(new Dimension(400, 600));
        pane.setBorder(null);
        
        pane.getVerticalScrollBar().setUnitIncrement(10);
        add(pane);
        
        backButton = new JButton("Zurueck zum Hauptmenu");
        backButton.setSize(190, 50);
        backButton.setLocation(300, 500);
        backButton.addActionListener(this);
        add(backButton);
        
        /*JButton refreshButton = new JButton("Aktualisieren");
        refreshButton.setSize(190, 50);
        refreshButton.setLocation(510, 500);
        add(refreshButton);*/
        
        messagePanel = new JPanel();
        messageLabel = new JLabel("Nils hat Sie zu einem Spiel herausgefordert!");
        JButton acceptButton = new JButton("Akzeptieren");
        JButton declineButton = new JButton("Ablehnen");
        acceptButton.setActionCommand("acceptchallenge");
        acceptButton.addActionListener(this);
        declineButton.setActionCommand("declinechallenge");
        declineButton.addActionListener(this);
        messagePanel.add(messageLabel);
        messagePanel.add(acceptButton);
        messagePanel.add(declineButton);
        messagePanel.setVisible(false);
        messagePanel.setBounds(0, 120, 1000, 50);
        add(messagePanel);
        
        /*refreshButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
            	refreshUI();
            }
        });*/
    }
    
    public void showInvitation(String name) {
        messagePanel.setVisible(true);
        messageLabel.setText(String.format("%s hat dich zu einem Spiel herausgefordert!", name));
    }

    public void hideInvitation() {
        messagePanel.setVisible(false);
    }
    
	@Override
	public void refreshUI() {
		if(this.lobby == null)
			return;
		
		this.tableParent.removeAllRows();
		
		HashMap<Integer, String> playerlist = this.lobby.getPlayerlist();
		
		for(Integer i : playerlist.keySet()) {
			String username = playerlist.get(i);
			int id = i;
			String status = this.lobby.getStatusForPlayer(id);
			this.tableParent.addRow(username, id, status, this);
		}
		
		if(playerlist.size() == 0) {
			this.tableParent.showNoPlayers();
		}
		
		String challenged = this.lobby.challengedBy();
		if(challenged.equals("")) {
			hideInvitation();
		} else {
			showInvitation(challenged);
		}
		
		this.validate();
		this.repaint();
	}

	public void setLobby(Lobby lobby) {
		this.lobby = lobby;
	}
	
	@Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        if(source == backButton) {
        	window.backToMenu();
        } else if(e.getActionCommand().equals("challenge")) {
        	Object property = source.getClientProperty("id");
        	if (property instanceof Integer) {
    		   int id = ((Integer)property);
    		   System.out.println("Challenge ID " + id);
    		   this.lobby.challengePlayer(id);
    		}        	
        } else if(e.getActionCommand().equals("revokechallenge")) {
        	System.out.println("Revoking Challenge");
        	this.lobby.revokeChallenge();
        } else if(e.getActionCommand().equals("declinechallenge")) {
        	System.out.println("Declining Challenge");
        	this.lobby.declineChallenge();
        } else if(e.getActionCommand().equals("acceptchallenge")) {
            System.out.println("Accepting Challenge");
            this.lobby.acceptChallenge();
        }
    }
}
