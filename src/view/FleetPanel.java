package view;
import java.awt.Color;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Ship;


public class FleetPanel extends JPanel {

    // long long LONG
    private static final long serialVersionUID = -6328642954757462082L;
    private boolean active;
    private JList<Ship> list;
    private FleetCellRenderer renderer;
    private GamePanel parent;
    
    public FleetPanel(boolean activePlayer, GamePanel par) {
    	this.parent = par;
    	this.active = activePlayer;
        setLayout(null);
        this.list = new JList<>();
        if(activePlayer) {
        	list.setModel(new ShipListModel(parent.getGameHandler().getGame().getPlayer1Field().getShips()));
        } else {
            list.setModel(new ShipListModel(parent.getGameHandler().getGame().getPlayer2Field().getShips()));
        }
        this.renderer = new FleetCellRenderer(this.active);
        list.setCellRenderer(this.renderer);
        list.setBorder(new LineBorder(new Color(0, 0, 0)));
        list.setBackground(UIManager.getColor("Panel.background"));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setOpaque(false);
        list.setVisibleRowCount(10);
        list.setSize(115, 331);
        
        list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) {
					@SuppressWarnings("unchecked")
					Ship sel = ((JList<Ship>) e.getSource()).getSelectedValue();
					if(sel != null) {
					    
						if(sel.isPlaced()) {
							list.clearSelection();
							parent.getGameHandler().selectedShipChanged(null);
						} else {
							parent.getGameHandler().selectedShipChanged(sel);
						}
						
					}
				}
			}
		});
       
        add(list);
    }
    
    public Ship getSelectedShip() {
    	return this.list.getSelectedValue();
    }
    
    public void inactivate() {
    	this.active = false;
    	this.renderer.setSelectable(this.active);
    	this.list.clearSelection();
    	this.validate();
    	this.repaint();
    }
    
    public void deselect() {
    	this.list.clearSelection();
    	this.validate();
    	this.repaint();
    }
    
    public void refreshUI() {
        this.validate();
        this.repaint();
    }
    
    public class ShipListModel extends AbstractListModel<Ship> {
        
        private static final long serialVersionUID = 805250100449322899L;
        private List<Ship> model;

        public ShipListModel(List<Ship> model) {
            this.model = model;
        }
        @Override
        public Ship getElementAt(int i) {
            return model.get(i);
        }

        @Override
        public int getSize() {
            return model.size();
        }
        
    }

}
