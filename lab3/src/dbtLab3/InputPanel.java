package dbtLab3;

import javax.swing.*;

import java.awt.*;

/**
 * A GUI panel which contains a number of text fields on top of each other. Each
 * text field has a label.
 */
public class InputPanel extends JPanel {
	private static final long serialVersionUID = 1;

	/**
	 * Create the panel with the specified fields and labels.
	 * 
	 * @param texts
	 *            The labels on the fields.
	 * @param fields
	 *            The text fields.
	 */
	public InputPanel(String[] texts, JTextField[] fields) {
		JPanel left = new JPanel();
		left.setLayout(new GridLayout(texts.length, 1));
		for (int i = 0; i < texts.length; i++) {
			JLabel label = new JLabel(texts[i] + "      ", JLabel.RIGHT);
			left.add(label);
		}

		JPanel right = new JPanel();
		right.setLayout(new GridLayout(fields.length, 1));
		for (int i = 0; i < fields.length; i++) {
			right.add(fields[i]);
		}

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(left);
		add(right);
	}
}
