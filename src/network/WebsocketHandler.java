package network;

import gamelogic.GameHandler;

import java.io.IOException;
import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import model.Lobby;
import model.Point;
import model.Ship;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@ClientEndpoint
public class WebsocketHandler implements NetworkHandler {
	
	private Lobby lobby;
	private GameHandler gh;
	private Session userSession;
	private Gson gson = new Gson();
	
	private boolean socketOk = false;
	
	public WebsocketHandler(Lobby lobby) {
		this.lobby = lobby;
		
		try {
			 URI endpointURI = new URI("ws://vast-castle-9529.herokuapp.com/");
			 WebSocketContainer container = ContainerProvider
			 .getWebSocketContainer();
			 container.connectToServer(this, endpointURI);
			 socketOk = true;
		 } catch (Exception e) {
			 // TODO Auto-generated catch block
			 socketOk = false;
			 lobby.connectionFailed();
			 e.printStackTrace();
		 }
	}
	
	@OnOpen
	public void onOpen(Session userSession) {
		this.userSession = userSession;
	}

	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		this.userSession = null;
	}

	@OnMessage
	public void onMessage(String message) {
		System.out.println(message);
		JsonObject json = gson.fromJson(message, JsonObject.class);
		switch (json.get("action").getAsString()) {
		case "lobbyplayerlist":
			this.lobby.parsePlayerlist(json.get("playerlist"), json.get("challengedby"));
			break;
		case "startgame":
		    this.lobby.getBS().createNewMultiPlayerGame(json.get("opponent_name").getAsString());
		    break;
		case "field_update":
		    this.gh.getGame().updateField(json, this.gh);
		    this.gh.triggerUIRedraw();
		    break;
		case "gamestate":
		    this.gh.changeState(json.get("state").getAsString());
		    this.gh.triggerUIRedraw();
		    break;
		case "gameover":
            this.gh.changeState(json.get("outcome").getAsString());
            this.gh.triggerUIRedraw();
		    break;
		case "chatmsg":
		    this.gh.receiveChatMessage(json.get("message").getAsString(), this.gh.getGame().getPlayer2());
		    this.gh.triggerUIRedraw();
		    break;
		case "logmsg":
		    this.gh.receiveLogMessage(json.get("sender").getAsString(), json.get("message").getAsString());
		    this.gh.triggerUIRedraw();
		    break;
		    
		}
	}
	
	public void setGH(GameHandler gh) {
	    this.gh = gh;
	}
	
	public void lobbySignin(String name) {
		JsonObject json = new JsonObject();
		json.addProperty("action", "lobbysignin");
		json.addProperty("username", name);
		
		this.userSession.getAsyncRemote().sendText(json.toString());
	}
	
	public void challengePlayer(int id) {
		JsonObject json = new JsonObject();
		json.addProperty("action", "challengeplayer");
		json.addProperty("userid", String.valueOf(id));
		
		this.userSession.getAsyncRemote().sendText(json.toString());
	}
	
	public void revokeChallenge() {
		JsonObject json = new JsonObject();
		json.addProperty("action", "revokechallenge");
		
		this.userSession.getAsyncRemote().sendText(json.toString());
	}
	
	public void declineChallenge() {
		JsonObject json = new JsonObject();
		json.addProperty("action", "declinechallenge");
		
		this.userSession.getAsyncRemote().sendText(json.toString());
	}
	
	public void acceptChallenge() {
	    JsonObject json = new JsonObject();
        json.addProperty("action", "acceptchallenge");
        
        this.userSession.getAsyncRemote().sendText(json.toString());
	}
	
	public boolean isSocketOk() {
		return socketOk;
	}
	
	public void closeConnection() {
		try {
			this.userSession.close();
		} catch (IOException e) {
			
		}
	}

    @Override
    public void place(Ship ship) {
        JsonObject json = new JsonObject();
        json.addProperty("action", "ship_placed");
        json.addProperty("shiptype", ship.getShipID());
        json.addProperty("x", ship.getPosX());
        json.addProperty("y", ship.getPosY());
        json.addProperty("orientation", ship.getOrientation().shortName());
        
        this.gh.clearSelection();
        
        this.userSession.getAsyncRemote().sendText(json.toString());
        
    }

    @Override
    public void fire(Point tar) {
        JsonObject json = new JsonObject();
        json.addProperty("action", "attack");
        json.addProperty("x", tar.getX());
        json.addProperty("y", tar.getY());
        
        this.userSession.getAsyncRemote().sendText(json.toString());
        
    }

    @Override
    public void sendMsg(String msg) {
        JsonObject json = new JsonObject();
        json.addProperty("action", "send_chat");
        json.addProperty("message", msg);
        
        this.userSession.getAsyncRemote().sendText(json.toString());
    }

	

}
