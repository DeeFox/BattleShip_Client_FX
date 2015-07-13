package model;

import gamelogic.GameHandler;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CopyOnWriteArrayList;

import model.Ship.Orientation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Game {

    public enum GameState {
        PLACE_YOUR_SHIPS("Platziere deine Flotte!"), 
        WAITING_FOR_SHIP_PLACEMENT("Warte auf Flottenplatzierung des Gegners!"), 
        YOUR_TURN("Du bist dran!"), 
        WAITING_FOR_TURN("Warte auf gegnerischen Spielzug!"), 
        YOU_WON("Du hast das Spiel gewonnen!"), 
        YOU_LOST("Du hast das Spiel verloren!"), 
        PLAYER2_LEFT("Du hast das Spiel gewonnen! Der Gegner hat das Spiel verlassen.");

        public String message;

        private GameState(String val) {
            this.message = val;
        }
    }

    private CopyOnWriteArrayList<LogItem> chatMessages;
    private CopyOnWriteArrayList<LogItem> logMessages;

    /*
     * private Field thisField; private Field opponentField;
     */

    private Player player1;
    private Player player2;

    private GameState gameState;

    public Game(Player player1, Player player2) {
        super();
        this.player1 = player1;
        this.player2 = player2;
        this.chatMessages = new CopyOnWriteArrayList<LogItem>();
        this.logMessages = new CopyOnWriteArrayList<LogItem>();
        // TODO Names
        // this.thisField = new Field();
        // this.opponentField = new Field();

        this.gameState = GameState.PLACE_YOUR_SHIPS;
    }

    public void updateField(JsonObject obj, GameHandler gh) {
        String type = obj.get("type").getAsString();
        Field field;
        if (type.equals("your_field")) {
            field = this.getPlayer1Field();
        } else {
            field = this.getPlayer2Field();
        }
        JsonObject data = obj.get("data").getAsJsonObject();
        JsonArray ships = data.get("ships").getAsJsonArray();
        for (int i = 0; i < ships.size(); i++) {
            JsonObject ship = ships.get(i).getAsJsonObject();
            String sType = ship.get("type").getAsString();

            JsonArray hits = ship.get("hits").getAsJsonArray();
            Ship s = field.getShipFromID(sType);
            if ( (type.equals("your_field") && !s.isPlaced()) || ship.get("x") != null) {
                String x = ship.get("x").getAsString();
                String y = ship.get("y").getAsString();
                String orientation = ship.get("orientation").getAsString();
                s.setPosition(new Point(Integer.parseInt(x), Integer.parseInt(y)));
                s.setOrientation(Orientation.fromString(orientation));
                field.placeShip(s);
            }
            for (int j = 0; j < hits.size(); j++) {
                boolean h = hits.get(j).getAsBoolean();
                if (h) {
                    s.damageAt(j);
                }
            }

        }
        JsonArray hits2 = data.get("hits").getAsJsonArray();
        for (int i = 0; i < hits2.size(); i++) {
            int x = hits2.get(i).getAsJsonArray().get(0).getAsInt();
            int y = hits2.get(i).getAsJsonArray().get(1).getAsInt();
            field.markAsFired(new Point(x, y), true);
        }
        JsonArray hits3 = data.get("missed").getAsJsonArray();
        for (int i = 0; i < hits3.size(); i++) {
            int x = hits3.get(i).getAsJsonArray().get(0).getAsInt();
            int y = hits3.get(i).getAsJsonArray().get(1).getAsInt();
            field.markAsFired(new Point(x, y), false);
        }
    }

    public CopyOnWriteArrayList<LogItem> getChatMessages() {
        return chatMessages;
    }

    public CopyOnWriteArrayList<LogItem> getLogMessages() {
        return logMessages;
    }

    public String getStatusMessage() {
        return this.gameState.message;
    }

    public Field getPlayer1Field() {
        return player1.getField();
    }

    public Field getPlayer2Field() {
        return player2.getField();
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setPlayer1Field(Field field) {
        this.player1.setField(field);
    }

    public void setPlayer2Field(Field field) {
        this.player2.setField(field);
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public String debugOutputFields() {

        String out = "";

        Field f1 = this.player1.getField();
        Field f2 = this.player2.getField();

        String line = "";

        line += "#" + " ";
        for (int y = 1; y <= 10; y++) {
            line += (y % 10) + " ";
        }
        line += "| " + "#" + " ";
        for (int y = 1; y <= 10; y++) {
            line += (y % 10) + " ";
        }
        out += line + "\n";

        for (int y = 0; y < 10; y++) {
            line = "";

            line += Field.getCharForNumber(y + 1) + " ";
            for (int x = 0; x < 10; x++) {
                line += f1.getFieldString(new Point(x, y)) + " ";
            }
            line += "| " + Field.getCharForNumber(y + 1) + " ";
            for (int x = 0; x < 10; x++) {
                line += f2.getFieldString(new Point(x, y)) + " ";
            }
            out += line + "\n";
        }

        return out;
    }

    public void addChatMessage(String sender, String message) {
        DateTimeFormatter ft = DateTimeFormatter.ofPattern("HH:mm:ss");
        LogItem msg = new LogItem("[" + ft.format(LocalTime.now()) + "] ", sender + ": ", message);
        this.chatMessages.add(msg);
    }

    public void addLogMessage(String source, String message) {
        DateTimeFormatter ft = DateTimeFormatter.ofPattern("HH:mm:ss");
        LogItem msg = new LogItem("[" + ft.format(LocalTime.now()) + "] ", source + ": ", message);
        this.logMessages.add(msg);
    }

    public Player getOtherPlayer(Player cur) {
        return cur.equals(player1) ? player2 : player1;
    }

    public LogItem getLastChatEntry() {
        return !chatMessages.isEmpty() ? chatMessages.get(chatMessages.size() - 1) : null;
    }

    public LogItem getLastLogEntry() {
        return !logMessages.isEmpty() ? logMessages.get(logMessages.size() - 1) : null;
    }

    // Methods to interact

    public class LogItem {
        private String time;
        private String name;
        private String text;

        public LogItem(String time, String name, String text) {
            this.time = time;
            this.name = name;
            this.text = text;
        }

        public String getComplete() {
            return time + name + text;
        }

        public String getTime() {
            return time;
        }

        public String getName() {
            return name;
        }

        public String getText() {
            return text;
        }
    }

}