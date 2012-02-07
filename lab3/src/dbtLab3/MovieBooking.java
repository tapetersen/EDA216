package dbtLab3;

/**
 * MovieBooking is the main class for the movie ticket booking 
 * application. It creates a database object and the GUI to
 * interface to the database.
 */
public class MovieBooking {
    public static void main(String[] args) {
        Database db = new Database();
        new MovieGUI(db);
    }
}
