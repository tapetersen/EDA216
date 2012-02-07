package dbtLab3;

import javax.swing.*;
import java.awt.event.*;

/**
 * The GUI pane where a new user logs in. Contains a text field where the user
 * id is entered and a button to log in.
 */
public class UserLoginPane extends BasicPane {
	private static final long serialVersionUID = 1;
	/**
	 * The text field where the user id is entered.
	 */
	private JTextField[] fields;

	/**
	 * The number of the field where the user id is entered.
	 */
	private static final int USER_ID = 0;

	/**
	 * The total number of fields in the fields array.
	 */
	private static final int NBR_FIELDS = 1;

	/**
	 * Create the login pane.
	 * 
	 * @param db
	 *            The database object.
	 */
	public UserLoginPane(Database db) {
		super(db);
	}

	/**
	 * Create the top panel, consisting of the text field.
	 * 
	 * @return The top panel.
	 */
	public JComponent createTopPanel() {
		String[] texts = new String[NBR_FIELDS];
		texts[USER_ID] = "User id";
		fields = new JTextField[NBR_FIELDS];
		fields[USER_ID] = new JTextField(20);
		return new InputPanel(texts, fields);
	}

	/**
	 * Create the bottom panel, consisting of the login button and the message
	 * line.
	 * 
	 * @return The bottom panel.
	 */
	public JComponent createBottomPanel() {
		JButton[] buttons = new JButton[1];
		buttons[0] = new JButton("Login");
		ActionHandler actHand = new ActionHandler();
		fields[USER_ID].addActionListener(actHand);
		return new ButtonAndMessagePanel(buttons, messageLabel, actHand);
	}

	/**
	 * Perform the entry actions of this pane, i.e. clear the message line.
	 */
	public void entryActions() {
		clearMessage();
	}

	/**
	 * A class which listens for button clicks.
	 */
	class ActionHandler implements ActionListener {
		/**
		 * Called when the user clicks the login button. Checks with the
		 * database if the user exists, and if so notifies the CurrentUser
		 * object.
		 * 
		 * @param e
		 *            The event object (not used).
		 */
		public void actionPerformed(ActionEvent e) {
			String userId = fields[USER_ID].getText();
			/* --- insert own code here --- */
		}
	}
}
