package main;
import javax.swing.UIManager;

public class Main {
	public static void main(String[] args) {
	    
		try {
            // Set System L&F
	        UIManager.setLookAndFeel(
	            UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (Exception e) {
	       e.printStackTrace();
	    }
		
		// Create BattleShip Object
		new BattleShip();
	}
}
