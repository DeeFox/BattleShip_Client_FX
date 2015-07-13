package view;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import model.Game.LogItem;


public class ChatPanel extends JPanel {
    
    private static final long serialVersionUID = -3875116257014544997L;
    private JTextPane textPane;
    private LogItem lastEntry;
    private DefaultStyledDocument doc;
    
    public ChatPanel(String name) {
        setLayout(null);
        
        JLabel lblNewLabel = new JLabel(name);
        lblNewLabel.setBounds(0, 0, 100, 15);
        add(lblNewLabel);
        doc = new DefaultStyledDocument();
        textPane = new JTextPane(doc);
        textPane.setEditable(false);
        //textArea.setBounds(0, 15, 465, 100);
        DefaultCaret caret = (DefaultCaret)textPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        

        
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setBounds(0, 15, 465, 100);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);
    }
    
    public String getText() {
    	return textPane.getText();
    }
    
    public void printTime(String time) {
        Style style = textPane.addStyle("I'm a Style", null);
        StyleConstants.setBold(style, true);
        try {
            doc.insertString(doc.getLength(), time, style);
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        textPane.removeStyle("I'm a Style");
    }
    
    public void printName(String name) {
        Style style = textPane.addStyle("I'm a Style", null);
        StyleConstants.setForeground(style, Color.red);
        try {
            doc.insertString(doc.getLength(), name, style);
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        textPane.removeStyle("I'm a Style");
    }
    
    public void printText(String text) {
        Style style = textPane.addStyle("I'm a Style", null);
        StyleConstants.setForeground(style, Color.BLUE);
        try {
            doc.insertString(doc.getLength(), text + "\n", style);
            
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        textPane.removeStyle("I'm a Style");
    }
    
    public void printEntry(LogItem entry) {
        this.lastEntry = entry;
        this.printTime(entry.getTime());
        this.printName(entry.getName());
        this.printText(entry.getText());
    }
    
    public LogItem getLast() {
        return this.lastEntry;
    }
    
    public void clearText() {
        
       
    }
}
