package dbtLab3;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;

/**
 * BasicPane is a pane in the user interface. It consists of two subpanels:
 * the left panel and the right panel. The right panel in turn consist
 * of three panels on top of each other: bottom, middle and top. Subclasses
 * can choose to configure these panels as they wish.
 * <p>
 * The class contains a reference to the database object, so subclasses
 * can communicate with the database.
 */
public class BasicPane extends JPanel {
	private static final long serialVersionUID = 1;
    /**
     * The database object.
     */
    protected Database db;
        
    /**
     * A label which is intended to contain a message text.
     */
    protected JLabel messageLabel;
        
    /** 
     * Create a BasicPane object.
     *
     * @param db The database object.
     */
    public BasicPane(Database db) {
        this.db = db;
        messageLabel = new JLabel("      ");
                
        setLayout(new BorderLayout());
                
        JComponent leftPanel = createLeftPanel();
        add(leftPanel, BorderLayout.WEST);
                
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
                
        JComponent topPanel = createTopPanel();
        JComponent middlePanel = createMiddlePanel();
        JComponent bottomPanel = createBottomPanel();
        bottomPanel.setBorder
            (new CompoundBorder(new SoftBevelBorder(BevelBorder.RAISED),
                                bottomPanel.getBorder()));
        rightPanel.add(topPanel, BorderLayout.NORTH);
        rightPanel.add(middlePanel, BorderLayout.CENTER);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(rightPanel, BorderLayout.CENTER);
    }
        
    /** 
     * Create the left panel. Should be overridden by subclasses. 
     *
     * @return An empty panel.
     */
    public JComponent createLeftPanel() { 
        return new JPanel();
    }
        
    /** 
     * Create the top panel. Should be overridden by subclasses. 
     *
     * @return An empty panel.
     */
    public JComponent createTopPanel() { 
        return new JPanel();
    }
        
    /** 
     * Create the middle panel. Should be overridden by subclasses. 
     *
     * @return An empty panel.
     */
    public JComponent createMiddlePanel() { 
        return new JPanel();
    }
        
    /** 
     * Create the bottom panel. Should be overridden by subclasses. 
     *
     * @return An empty panel.
     */
    public JComponent createBottomPanel() { 
        return new JPanel();
    }
        
    /**
     * Perform the entry actions of the pane. Empty here, should be
     * overridden by subclasses.
     */
    public void entryActions() {}
        
    /**
     * Display a message.
     *
     * @param msg The message to display.
     */
    public void displayMessage(String msg) {
        messageLabel.setText(msg);
    }
        
    /**
     * Clear the message line.
     */
    public void clearMessage() {
        messageLabel.setText(" ");
    }
}
