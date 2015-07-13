package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import model.Ship;

public class FleetCellRenderer extends JPanel implements ListCellRenderer<Ship> {

    private static final long serialVersionUID = 5602963545858784345L;
    private static final Color HIGHLIGHT_COLOR = UIManager.getColor("List.selectionBackground");
    private static final Color DEFAULT_COLOR = UIManager.getColor("Panel.background");
    private JLabel nameLabel;

    private JPanel hit_1;
    private JPanel hit_2;
    private JPanel hit_3;
    private JPanel hit_4;
    private JPanel hit_5;
    
    private boolean selectable;

    public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	public FleetCellRenderer(boolean selectable) {
    	this.selectable = selectable;
        setSize(new Dimension(115, 33));
        setPreferredSize(new Dimension(115, 33));
        setMinimumSize(new Dimension(115, 33));
        setMaximumSize(new Dimension(115, 33));
        setOpaque(true);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UIManager.getColor("Panel.background"));

        JPanel container_1 = new JPanel();
        container_1.setOpaque(false);
        container_1.setLayout(null);
        container_1.setPreferredSize(new Dimension(115, 17));
        container_1.setMinimumSize(new Dimension(115, 17));
        container_1.setMaximumSize(new Dimension(115, 17));
        add(container_1);

        nameLabel = new JLabel("ADOLF");
        container_1.add(nameLabel);
        nameLabel.setSize(new Dimension(115, 14));
        nameLabel.setPreferredSize(new Dimension(115, 17));
        nameLabel.setMinimumSize(new Dimension(115, 17));
        nameLabel.setMaximumSize(new Dimension(115, 17));
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel container_2 = new JPanel();
        container_2.setSize(new Dimension(115, 0));
        container_2.setPreferredSize(new Dimension(115, 16));
        container_2.setMinimumSize(new Dimension(115, 16));
        container_2.setMaximumSize(new Dimension(115, 16));
        container_2.setOpaque(false);
        add(container_2);
        container_2.setLayout(new BoxLayout(container_2, BoxLayout.X_AXIS));

        hit_1 = new JPanel();
        hit_1.setPreferredSize(new Dimension(23, 16));
        hit_1.setMinimumSize(new Dimension(23, 16));
        hit_1.setMaximumSize(new Dimension(23, 16));
        hit_1.setBorder(new LineBorder(new Color(0, 0, 0)));
        container_2.add(hit_1);

        hit_2 = new JPanel();
        hit_2.setPreferredSize(new Dimension(23, 16));
        hit_2.setMinimumSize(new Dimension(23, 16));
        hit_2.setMaximumSize(new Dimension(23, 16));
        hit_2.setBorder(new LineBorder(new Color(0, 0, 0)));
        container_2.add(hit_2);

        hit_3 = new JPanel();
        hit_3.setPreferredSize(new Dimension(23, 16));
        hit_3.setMinimumSize(new Dimension(23, 16));
        hit_3.setMaximumSize(new Dimension(23, 16));
        hit_3.setBorder(new LineBorder(new Color(0, 0, 0)));
        container_2.add(hit_3);

        hit_4 = new JPanel();
        hit_4.setPreferredSize(new Dimension(23, 16));
        hit_4.setMinimumSize(new Dimension(23, 16));
        hit_4.setMaximumSize(new Dimension(23, 16));
        hit_4.setBorder(new LineBorder(new Color(0, 0, 0)));
        container_2.add(hit_4);

        hit_5 = new JPanel();
        hit_5.setPreferredSize(new Dimension(23, 16));
        hit_5.setMinimumSize(new Dimension(23, 16));
        hit_5.setMaximumSize(new Dimension(23, 16));
        hit_5.setBorder(new LineBorder(new Color(0, 0, 0)));
        container_2.add(hit_5);

    }
	
	private JPanel getHitBox(int i) {
	    switch(i) {
	    case 0:
	        return hit_1;
	    case 1:
	        return hit_2;
	    case 2:
	        return hit_3;
	    case 3:
	        return hit_4;
	    case 4:
	        return hit_5;
	    default:
	        return null;
	    }
	}

    @Override
    public Component getListCellRendererComponent(JList<? extends Ship> list, Ship value, int index, boolean isSelected,
            boolean cellHasFocus) {
        nameLabel.setText(value.toString());
        if (value.isDestroyed()) {
            nameLabel.setText("<html><s>" + value.toString() + "</s></html>");
        }

        if (isSelected && this.selectable && !value.isPlaced()) {
            setBackground(HIGHLIGHT_COLOR);
            setForeground(Color.white);
        } else {
            setBackground(DEFAULT_COLOR);
            setForeground(Color.black);

        }
        updateHits(value);
        return this;
    }

    private void updateHits(Ship ship) {
    	int size = ship.getSize();
        hit_1.setVisible(false);
        hit_2.setVisible(false);
        hit_3.setVisible(false);
        hit_4.setVisible(false);
        hit_5.setVisible(false);
        if (size > 0) {
            hit_1.setVisible(true);
        }
        if (size > 1) {
            hit_2.setVisible(true);
        }
        if (size > 2) {
            hit_3.setVisible(true);
        }
        if (size > 3) {
            hit_4.setVisible(true);
        }
        if (size > 4) {
            hit_5.setVisible(true);
        }
        colorHits(ship);
    }

    private void colorHits(Ship ship) {
    	boolean[] hits = ship.getHits();
    	
    	if(!ship.isPlaced()) {
    		hit_1.setBackground(Color.LIGHT_GRAY);
            hit_2.setBackground(Color.LIGHT_GRAY);
            hit_3.setBackground(Color.LIGHT_GRAY);
            hit_4.setBackground(Color.LIGHT_GRAY);
            hit_5.setBackground(Color.LIGHT_GRAY);
            return;
    	}
    	if(selectable) {
    	    hit_1.setBackground(Color.GREEN);
            hit_2.setBackground(Color.GREEN);
            hit_3.setBackground(Color.GREEN);
            hit_4.setBackground(Color.GREEN);
            hit_5.setBackground(Color.GREEN);
            for(int i = 0; i < hits.length; i++) {
                if(hits[i]) {
                    getHitBox(i).setBackground(Color.RED);
                }
            }
    	} else {
    	    if(ship.isDestroyed()) {
    	        hit_1.setBackground(Color.RED);
    	        hit_2.setBackground(Color.RED);
    	        hit_3.setBackground(Color.RED);
    	        hit_4.setBackground(Color.RED);
    	        hit_5.setBackground(Color.RED);
    	    } else {
    	        hit_1.setBackground(Color.LIGHT_GRAY);
                hit_2.setBackground(Color.LIGHT_GRAY);
                hit_3.setBackground(Color.LIGHT_GRAY);
                hit_4.setBackground(Color.LIGHT_GRAY);
                hit_5.setBackground(Color.LIGHT_GRAY);
    	    }
    	}
    	
    }

}
