package main;

import java.util.Random;

import gamelogic.AIPlayer;
import gamelogic.GameHandler;
import model.Game;
import model.Lobby;
import model.Player;
import network.SimulatedNetworkHandler;
import network.WebsocketHandler;
import view.BattleShipWindow;

public class BattleShip {

	private BattleShipWindow window;
	
	private WebsocketHandler connection;
	
	private String user;
	
	public BattleShip() {
		this.window = new BattleShipWindow(this);
		char a[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char b[] = "aeiou".toCharArray();
        char c[] = "mnlksrp".toCharArray();
        Random rand = new Random();
        String uname = "";
        uname += a[rand.nextInt(a.length)];
        uname += b[rand.nextInt(b.length)];
        uname += c[rand.nextInt(c.length)];
        uname += b[rand.nextInt(b.length)];
        uname += b[rand.nextInt(b.length)];
        uname += c[rand.nextInt(c.length)];
        this.user = uname;
		window.hashCode();
	}
	
	public void openLobby() {
		this.window.showLobby();
		new Lobby(this, window, user);
	}
	
	public void createNewSinglePlayerGame() {
		Player p1 = new Player(user);
		Player p2 = new AIPlayer();
		Game g = new Game(p1, p2);
		GameHandler gh = new GameHandler(g);
		gh.setNet(new SimulatedNetworkHandler(gh));
		this.window.showSingle(gh);
	}
	
	public void createNewMultiPlayerGame(String name) {
		Player p1 = new Player(user);
		Player p2 = new Player(name);
		p2.getField().markAsMultiPlayerField();
		Game g = new Game(p1, p2);
		GameHandler gh = new GameHandler(g);
		gh.setNet(connection);
		connection.setGH(gh);
		this.window.showMulti(gh);
		
	}
	
	public void closeConnection() {
		if(this.connection != null)
			this.connection.closeConnection();
		this.connection = null;
	}

	public void setNet(WebsocketHandler net) {
		this.connection = net;
	}
	
	public void refreshUI() {
	    window.refreshUI();
	}
	
}
