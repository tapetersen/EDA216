package dbtLab3;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * The GUI pane where a user books tickets for movie performances. It contains
 * one list of movies and one of performance dates. The user selects a
 * performance by clicking in these lists. The performance data is shown in the
 * fields in the right panel. The bottom panel contains a button which the user
 * can click to book a ticket to the selected performance.
 */
public class BookingPane extends BasicPane {
	private static final long serialVersionUID = 1;
	/**
	 * A label showing the name of the current user.
	 */
	private JLabel currentUserNameLabel;

	/**
	 * The list model for the movie name list.
	 */
	private DefaultListModel nameListModel;

	/**
	 * The movie name list.
	 */
	private JList nameList;

	/**
	 * The list model for the performance date list.
	 */
	private DefaultListModel dateListModel;

	/**
	 * The performance date list.
	 */
	private JList dateList;

	/**
	 * The text fields where the movie data is shown.
	 */
	private JTextField[] fields;

	/**
	 * The number of the movie name field.
	 */
	private static final int MOVIE_NAME = 0;

	/**
	 * The number of the performance date field.
	 */
	private static final int PERF_DATE = 1;

	/**
	 * The number of the movie theater field.
	 */
	private static final int THEATER_NAME = 2;

	/**
	 * The number of the 'number of free seats' field.
	 */
	private static final int FREE_SEATS = 3;

	/**
	 * The total number of fields.
	 */
	private static final int NBR_FIELDS = 4;

	/**
	 * Create the booking pane.
	 * 
	 * @param db
	 *            The database object.
	 */
	public BookingPane(Database db) {
		super(db);
	}

	/**
	 * Create the left panel, containing the movie name list and the performance
	 * date list.
	 * 
	 * @return The left panel.
	 */
	public JComponent createLeftPanel() {
		nameListModel = new DefaultListModel();

		nameList = new JList(nameListModel);
		nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		nameList.setPrototypeCellValue("123456789012");
		nameList.addListSelectionListener(new NameSelectionListener());
		JScrollPane p1 = new JScrollPane(nameList);

		dateListModel = new DefaultListModel();

		dateList = new JList(dateListModel);
		dateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dateList.setPrototypeCellValue("123456789012");
		dateList.addListSelectionListener(new DateSelectionListener());
		JScrollPane p2 = new JScrollPane(dateList);

		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
		p.add(p1);
		p.add(p2);
		return p;
	}

	/**
	 * Create the top panel, containing the fields with the performance data.
	 * 
	 * @return The top panel.
	 */
	public JComponent createTopPanel() {
		String[] texts = new String[NBR_FIELDS];
		texts[MOVIE_NAME] = "Movie";
		texts[PERF_DATE] = "Date";
		texts[THEATER_NAME] = "Plays at";
		texts[FREE_SEATS] = "Free seats";

		fields = new JTextField[NBR_FIELDS];
		for (int i = 0; i < fields.length; i++) {
			fields[i] = new JTextField(20);
			fields[i].setEditable(false);
		}

		JPanel input = new InputPanel(texts, fields);

		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.LEFT));
		p1.add(new JLabel("Current user: "));
		currentUserNameLabel = new JLabel("");
		p1.add(currentUserNameLabel);

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(p1);
		p.add(input);
		return p;
	}

	/**
	 * Create the bottom panel, containing the book ticket-button and the
	 * message line.
	 * 
	 * @return The bottom panel.
	 */
	public JComponent createBottomPanel() {
		JButton[] buttons = new JButton[1];
		buttons[0] = new JButton("Book ticket");
		return new ButtonAndMessagePanel(buttons, messageLabel,
				new ActionHandler());
	}

	/**
	 * Perform the entry actions of this pane: clear all fields, fetch the movie
	 * names from the database and display them in the name list.
	 */
	public void entryActions() {
		clearMessage();
		currentUserNameLabel.setText(CurrentUser.instance().getCurrentUserId());
		fillNameList();
		clearFields();
	}

	/**
	 * Fetch movie names from the database and display them in the name list.
	 */
	private void fillNameList() {
		Collection<String> movies = db.getMovies();
		nameListModel.removeAllElements();
		
		for(String movie : movies) {
			nameListModel.addElement(movie);
		}
	}

	/**
	 * Fetch performance dates from the database and display them in the date
	 * list.
	 */
	private void fillDateList(String movieName) {
		dateListModel.removeAllElements();
		Collection<Map<String, String>> performances;
		
		performances = db.getPerformances(movieName);
		for(Map<String, String> performance : performances) {
			dateListModel.addElement(performance.get("show_date"));
		}
	}

	/**
	 * Clear all text fields.
	 */
	private void clearFields() {
		for (int i = 0; i < fields.length; i++) {
			fields[i].setText("");
		}
	}

	/**
	 * A class that listens for clicks in the name list.
	 */
	class NameSelectionListener implements ListSelectionListener {
		/**
		 * Called when the user selects a name in the name list. Fetches
		 * performance dates from the database and displays them in the date
		 * list.
		 * 
		 * @param e
		 *            The selected list item.
		 */
		public void valueChanged(ListSelectionEvent e) {
			if (nameList.isSelectionEmpty()) {
				return;
			}
			String movieName = (String) nameList.getSelectedValue();
			fillDateList(movieName);
		}
	}

	/**
	 * A class that listens for clicks in the date list.
	 */
	class DateSelectionListener implements ListSelectionListener {
		/**
		 * Called when the user selects a name in the date list. Fetches
		 * performance data from the database and displays it in the text
		 * fields.
		 * 
		 * @param e
		 *            The selected list item.
		 */
		public void valueChanged(ListSelectionEvent e) {
			Map<String, String> performanceData;
			
			if (nameList.isSelectionEmpty() || dateList.isSelectionEmpty()) {
				return;
			}
			String movieName = (String) nameList.getSelectedValue();
			String date = (String) dateList.getSelectedValue();
			
			performanceData = db.getPerformanceData(movieName, date);
			performanceData.get("movie_name");
			fields[MOVIE_NAME].setText(performanceData.get("movie_name"));
			fields[PERF_DATE].setText(performanceData.get("show_date"));
			fields[THEATER_NAME].setText(performanceData.get("theatre_name"));
			fields[FREE_SEATS].setText(performanceData.get("free_seats"));
		}
	}

	/**
	 * A class that listens for button clicks.
	 */
	class ActionHandler implements ActionListener {
		/**
		 * Called when the user clicks the Book ticket button. Books a ticket
		 * for the current user to the selected performance (adds a booking to
		 * the database).
		 * 
		 * @param e
		 *            The event object (not used).
		 */
		public void actionPerformed(ActionEvent e) {
			if (nameList.isSelectionEmpty() || dateList.isSelectionEmpty()) {
				return;
			}
			if (!CurrentUser.instance().isLoggedIn()) {
				displayMessage("Must login first");
				return;
			}
			String movieName = (String) nameList.getSelectedValue();
			String date = (String) dateList.getSelectedValue();
			
			if(db.bookTicket(movieName, date)) {
				// Ticket got booked
			} else {
				// ticket booking failed
			}
		}
	}
}
