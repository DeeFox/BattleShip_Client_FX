package view;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class MainMenuPanel extends JPanel {

    private static final long serialVersionUID = 2326340435914765337L;
    
    BattleShipWindow parent;
    
    public MainMenuPanel(BattleShipWindow window) {
        parent = window;
        setLayout(null);
        
        JLabel titleLabel = new JLabel("Battleship 3.9.3.2.4.5.2.2.3.4.5.4 RC", SwingConstants.CENTER);
        titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), 0, 42));
        titleLabel.setBounds(0, 10, 1000, 50);
        add(titleLabel);
        
        JButton singlePlayerButton = new JButton("Einzelspieler");
        singlePlayerButton.setFont(new Font(titleLabel.getFont().getFontName(), 0, 32));
        singlePlayerButton.setBounds(350, 100, 300, 100);
        
        singlePlayerButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.newSinglePlayerButtonClicked();
            }
        });
        
        add(singlePlayerButton);
        JButton multiPlayerButton = new JButton("Mehrspieler");
        multiPlayerButton.setFont(new Font(titleLabel.getFont().getFontName(), 0, 32));
        multiPlayerButton.setBounds(350, 210, 300, 100);
        multiPlayerButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
            	parent.multiplayerButtonClicked();
                //parent.showMulti();
            }
        });
        add(multiPlayerButton);
        JButton settingsButton = new JButton("Einstellungen");
        settingsButton.setFont(new Font(titleLabel.getFont().getFontName(), 0, 32));
        settingsButton.setBounds(350, 400, 300, 100);
        add(settingsButton);
        
        
        
    }
    
    

}
