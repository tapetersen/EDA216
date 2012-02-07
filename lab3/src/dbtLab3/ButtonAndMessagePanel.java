package dbtLab3;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * A GUI panel which contains buttons and a message line.
 */
public class ButtonAndMessagePanel extends JPanel {
	private static final long serialVersionUID = 1;

	/**
	 * Create the component with the specified buttons and message line, and
	 * also an action listener for the buttons.
	 * 
	 * @param buttons
	 *            The array of buttons.
	 * @param messageLine
	 *            The message line.
	 * @param actHand
	 *            The action listener for the buttons.
	 */
	public ButtonAndMessagePanel(JButton[] buttons, JLabel messageLine,
			ActionListener actHand) {
		setLayout(new GridLayout(2, 1));

		JPanel buttonPanel = new JPanel();
		for (int i = 0; i < buttons.length; i++) {
			buttonPanel.add(buttons[i]);
		}
		add(buttonPanel);

		add(messageLine);

		for (int i = 0; i < buttons.length; i++) {
			buttons[i].addActionListener(actHand);
		}
	}
}
