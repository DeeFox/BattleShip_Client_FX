package network;

import model.Point;
import model.Ship;

public interface NetworkHandler {
    
    public void place(Ship ship);
    public void fire(Point tar);
    public void sendMsg(String msg);

}
