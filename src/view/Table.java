package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

public class Table extends JPanel {

    private static final long serialVersionUID = 3893172523770538788L;
  
    private int width;
    private int cellHeight;
    
    private int rowCount;
    
    public Table(int width, int cellHeight) {
        this.width = width;
        this.cellHeight = cellHeight;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(width, cellHeight * rowCount));
        setMinimumSize(new Dimension(width, 10));
        setMaximumSize(new Dimension(width, 10));

    }
    
    public void addRow(String playername, int id, String status, MultiplayerLobbyPanel ref) {
        this.add(new TableRow(playername, id, status, width, cellHeight, ref));
        setPreferredSize(new Dimension(width, cellHeight * rowCount));
        this.validate();
        this.repaint();
    }
    
    public void removeAllRows() {
    	this.removeAll();
    	this.rowCount = 0;
    }
    
    public class TableRow extends JPanel {
        
        private static final long serialVersionUID = 6926895321098526962L;
        
        private JPanel left;
        private JPanel right;
        
        private JLabel nameLabel;
        private JButton challengeButton;
        
        //private int userid;
        
        public TableRow(String playername, int id, String status, int width, int cellHeight, MultiplayerLobbyPanel ref) {
        	//this.userid = id;
        	
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            this.setPreferredSize(new Dimension(width, cellHeight));
            this.setMinimumSize(new Dimension(width, cellHeight));
            this.setMaximumSize(new Dimension(width, cellHeight));
            
            left = new JPanel();
            left.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
            left.setPreferredSize(new Dimension(width / 2, cellHeight));
            left.setMinimumSize(new Dimension(width / 2, cellHeight));
            left.setMaximumSize(new Dimension(width / 2, cellHeight));
            left.setBorder(new MatteBorder(0, 1, 1, 1, Color.BLACK));
            
            nameLabel = new JLabel(playername);
            left.add(nameLabel);
            
            right = new JPanel();
            right.setLayout(new FlowLayout(FlowLayout.RIGHT, 25, 5));
            right.setPreferredSize(new Dimension(width / 2, cellHeight));
            right.setMinimumSize(new Dimension(width / 2, cellHeight));
            right.setMaximumSize(new Dimension(width / 2, cellHeight));
            right.setBorder(new MatteBorder(0, 0, 1, 1, Color.BLACK));
            
            if(id > -1) {
            	if(!status.equals("notavailable")) {
	            	if(status.equals("waitingforanswer")) {
	            		challengeButton = new JButton("Zurückziehen!");
	    	            challengeButton.setActionCommand("revokechallenge");
	            	} else if(status.equals("available")) {
	            		challengeButton = new JButton("Herausfordern!");
	    	            challengeButton.setActionCommand("challenge");
	            	}
		            challengeButton.putClientProperty("id", id);
		            challengeButton.setPreferredSize(new Dimension(150, 20));
		            challengeButton.addActionListener(ref);
		            right.add(challengeButton);
            	}
            }
            
            if(rowCount % 2 == 0) {
                left.setBackground(Color.WHITE);
                right.setBackground(Color.WHITE);
            }
            
            this.add(left);
            this.add(right);
            
            rowCount++;
        }
        
        
        
    }

	public void showNoPlayers() {
		this.add(new TableRow("Keine Spieler in Lobby", -1, "", width, cellHeight, null));
        setPreferredSize(new Dimension(width, cellHeight * rowCount));
        this.validate();
        this.repaint();
	}

}
