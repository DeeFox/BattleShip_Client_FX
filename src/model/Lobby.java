package model;

import java.util.HashMap;

import main.BattleShip;
import network.WebsocketHandler;
import view.BattleShipWindow;
import view.MultiplayerLobbyPanel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Lobby {
	
	private HashMap<Integer, String> playerlist;
	private HashMap<Integer, String> playerstatuslist;
	
	private boolean isChallenged = false;
	private String challengedBy;
	
	private WebsocketHandler net;
	
	private BattleShip bs;
	private BattleShipWindow window;
	private MultiplayerLobbyPanel mpanel;
	
	public Lobby(BattleShip bs, BattleShipWindow window, String uname) {
		this.bs = bs;
		this.window = window;
		this.mpanel = window.getLobbyPanel();
		
		this.playerlist = new HashMap<Integer, String>();
		this.playerstatuslist = new HashMap<Integer, String>();
		this.net = new WebsocketHandler(this);
		if(!this.net.isSocketOk())
			return;
		
		this.mpanel.setLobby(this);
		this.bs.setNet(this.net);
		
		this.net.lobbySignin(uname);
	}
	
	public void setChallenged(String username) {
		this.challengedBy = username;
	}
	
	public void setUnchallenged() {
		this.isChallenged = false;
		this.challengedBy = null;
	}
	
	public boolean isChallenged() {
		return this.isChallenged;
	}
	
	public String challengedBy() {
		return this.challengedBy;
	}
	
	public void setPlayerlist(HashMap<Integer, String> playerlist) {
		this.playerlist = playerlist;
	}
	
	public HashMap<Integer, String> getPlayerlist() {
		return this.playerlist;
	}
	
	public String getStatusForPlayer(int id) {
		return this.playerstatuslist.get(id);
	}
	
	public BattleShip getBS() {
	    return this.bs;
	}
	
	public void refreshLobby() {
		//this.playerlist = net.getLobbyPlayers();
		
		//this.net.getLobbyStatus(this);
	}
	
	public void connectionFailed() {
		this.window.backToMenu();
		this.window.showAlert("Connection to Multiplayer Server failed!");
	}
	
	public void closeConnection() {
		this.net.closeConnection();
	}

	public void parsePlayerlist(JsonElement jsonelem, JsonElement challengedby) {
		HashMap<Integer, String> playerlist = new HashMap<Integer, String>();
		HashMap<Integer, String> statuslist = new HashMap<Integer, String>();
		if(jsonelem.isJsonArray()) {
			JsonArray ar = jsonelem.getAsJsonArray();
			int size = ar.size();
			if(size > 0) {
				for(int i = 0; i < size; i++) {
					JsonElement ele = ar.get(i);
					if(ele.isJsonObject()) {
						JsonObject obj = ele.getAsJsonObject();
						String username = obj.get("username").getAsString();
						int id = Integer.parseInt(obj.get("userid").getAsString());
						playerlist.put(id, username);
						String status = obj.get("status").getAsString();
						statuslist.put(id, status);
					}
				}
			}
			this.setPlayerlist(playerlist);
			this.playerstatuslist = statuslist;
		}
		
		if(challengedby.isJsonPrimitive()) {
			setChallenged(challengedby.getAsString());
		}
		
		this.mpanel.refreshUI();
	}
	
	public void challengePlayer(int id) {
		this.net.challengePlayer(id);
	}
	
	public void revokeChallenge() {
		this.net.revokeChallenge();
	}

	public void declineChallenge() {
		this.net.declineChallenge();
	}

    public void acceptChallenge() {
        this.net.acceptChallenge();
        
    }
}
