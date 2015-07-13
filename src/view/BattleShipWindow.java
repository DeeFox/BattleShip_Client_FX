package view;
import gamelogic.GameHandler;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import main.BattleShip;

public class BattleShipWindow {

    JFrame frame;
    MainMenuPanel menuPanel;
    GamePanel gamePanel;
    MultiplayerLobbyPanel mpPanel;
    
    BattleShip parent;

    public BattleShipWindow(BattleShip p) {
    	this.parent = p;
        frame = new JFrame("BattleShip 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        menuPanel = new MainMenuPanel(this);
        frame.add(menuPanel);
        frame.setVisible(true);
    }

    public void showSingle(GameHandler gh) {
    	// Always create fresh GamePanel
        gamePanel = new GamePanel(this, gh);
        gh.setGamePanel(gamePanel);
        
        frame.remove(menuPanel);
        frame.add(gamePanel);
        frame.validate();
        frame.repaint();
    }
    
    public void showMulti(GameHandler gh) {
        gamePanel = new GamePanel(this, gh);
        gh.setGamePanel(gamePanel);
        
        frame.remove(mpPanel);
        frame.add(gamePanel);
        frame.validate();
        frame.repaint();
    }

    public void showLobby() {
        if (mpPanel == null) {
            mpPanel = new MultiplayerLobbyPanel(this);
        }
        frame.remove(menuPanel);
        frame.add(mpPanel);
        
        frame.validate();
        frame.repaint();
    }
    
    public void showSettings() {

    }
    
    public void backToMenu() {
        this.parent.closeConnection();
    	if(mpPanel != null) {
            frame.remove(mpPanel);
    	}
        if(gamePanel != null) {
            frame.remove(gamePanel);
        }
        frame.add(menuPanel);
        frame.validate();
        frame.repaint();
    }
    
    public void newSinglePlayerButtonClicked() {
    	this.parent.createNewSinglePlayerGame();
    }

	public void multiplayerButtonClicked() {
		this.parent.openLobby();
	}
	
	public MultiplayerLobbyPanel getLobbyPanel() {
		return this.mpPanel;
	}
	
	public void showAlert(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}
	
	public void refreshUI() {
	    if(menuPanel != null) {
	        menuPanel.validate();
	        menuPanel.repaint();
	    }
	    
	    if(gamePanel != null) {
	        gamePanel.validate();
	        gamePanel.repaint();
	    }
	    
	    if(mpPanel != null) {
	        mpPanel.validate();
	        mpPanel.repaint();
	    }
	        
	}
}
