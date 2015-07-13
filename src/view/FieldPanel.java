package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import model.Field;
import model.Game;
import model.IRefreshable;
import model.Point;
import model.Ship;
import model.Ship.Orientation;

public class FieldPanel extends JPanel implements MouseListener, MouseMotionListener, IRefreshable {

    // TODO print enemy board on debug mode
    private final boolean DEBUG = false;

    private static final long serialVersionUID = 7347709576413334401L;

    private Point mousePosition = null;

    private GamePanel parent;
    private boolean playerField;

    public FieldPanel(GamePanel parent, boolean playerField) {
        this.parent = parent;
        this.playerField = playerField;
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

    }

    public void redraw() {
        // stuff
        validate();
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Game game = this.parent.getGame();

        g.setColor(Color.GRAY);

        g.fillRect(0, 0, 330, 30);
        g.fillRect(0, 0, 30, 330);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, 330, 330);

        g.setFont(new Font(g.getFont().getName(), Font.BOLD, 15));
        for (int x = 1; x <= 10; x++) {
            g.setColor(Color.BLACK);
            g.drawLine(x * 30, 0, x * 30, 329);
            g.drawLine(0, x * 30, 329, x * 30);
            g.drawString(String.valueOf(x), x * 30 + 5, 23);
            g.drawString(getCharForNumber(x), 10, x * 30 + 25);
        }
        
     // Draw shots
        Field ff = (this.playerField) ? game.getPlayer1Field() : game.getPlayer2Field();

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                int xx = (x + 1) * 30;
                int yy = (y + 1) * 30;

                if (!this.playerField) {
                    if (ff.isHit(new Point(x, y))) {
                        g.setColor(Color.RED);
                        g.fillRect(xx + 1, yy + 1, 29, 29);
                    }
                }
            }
        }
        
        Field field = this.playerField ? this.parent.getGame().getPlayer1Field() : this.parent.getGame().getPlayer2Field();
        if (this.playerField || this.DEBUG || field.isMultiPlayerField()) {
            for (Ship s : field.getShips()) {
                if (s.isPlaced()) {
                    for (int i = 0; i < s.getType().size; i++) {
                        g.setColor(Color.GREEN);
                        int posX = s.getPosX() + (s.getOrientation().x * i) + 1;
                        int posY = s.getPosY() + (s.getOrientation().y * i) + 1;

                        Point tp = new Point(posX - 1, posY - 1);
                        if (field.alreadyFiredHere(tp)) {
                            g.setColor(Color.RED);
                        }
                        g.fillRect(posX * 30 + 1, posY * 30 + 1, 29, 29);
                    }
                    // Draw Ship with border
                    int px1 = (s.getPosX() + 1) * 30 + 1;
                    int py1 = (s.getPosY() + 1) * 30 + 1;
                    int px2 = ((s.getType().size * s.getOrientation().x)) * 30 - 2;
                    int py2 = ((s.getType().size * s.getOrientation().y)) * 30 - 2;
                    px2 = (s.getOrientation() == Orientation.HORIZONTAL) ? px2 : 28;
                    py2 = (s.getOrientation() == Orientation.VERTICAL) ? py2 : 28;
                    g.setColor(Color.DARK_GRAY);
                    g.drawRect(px1, py1, px2, py2);
                }
            }
        }
        
        // Draw markers
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                int xx = (x + 1) * 30;
                int yy = (y + 1) * 30;

                if (ff.alreadyFiredHere(new Point(x, y))) {
                    g.setColor(Color.YELLOW);
                    g.fillOval(xx + 10, yy + 10, 10, 10);
                }
            }
        }

        if (this.playerField) {
            Ship sp = this.parent.getPlayer1Fleet().getSelectedShip();
            Field f = this.parent.getGame().getPlayer1Field();
            if (sp != null) {

                if (f.isSpaceOccupied(sp) || !f.areAdjascentFieldsFree(sp.getPos())) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.YELLOW);
                }

                for (int i = 0; i < sp.getType().size; i++) {
                    int posX = sp.getPosX() + (sp.getOrientation().x * i) + 1;
                    int posY = sp.getPosY() + (sp.getOrientation().y * i) + 1;
                    if (Field.isValidPoint(new Point(posX - 1, posY - 1)))
                        g.fillRect(posX * 30 + 1, posY * 30 + 1, 29, 29);
                }
            }

        }
    }

    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char) (i + 'A' - 1)) : null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
       // this.parent.getGameHandler().onMouseClick(e, playerField);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX() / 30;
        int y = e.getY() / 30;
        if (x > 0 && x < 11 && y > 0 && y < 11) {
           // this.parent.getGameHandler().onMouseClick(e, playerField);
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX() / 30;
        int y = e.getY() / 30;
        Point np = new Point(x, y);
        if (!np.equals(this.mousePosition) && x > 0 && y > 0) {
            this.mousePosition = np;
            this.parent.getGameHandler().mousePositionChanged(np);
            this.refreshUI();
        }
    }

    @Override
    public void refreshUI() {
        this.validate();
        this.repaint();
    }

}
