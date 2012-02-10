package dbtLab3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.sql.*;

/**
 * Database is a class that specifies the interface to the movie database. Uses
 * JDBC and the MySQL Connector/J driver.
 */
public class Database {
	/**
	 * The database connection.
	 */
	private Connection conn;

	/**
	 * An SQL statement object.
	 */
	private Statement stmt;

	/**
	 * Create the database interface object. Connection to the database is
	 * performed later.
	 */
	public Database() {
		conn = null;
	}

	/**
	 * Open a connection to the database, using the specified user name and
	 * password.
	 * 
	 * @param userName
	 *            The user name.
	 * @param password
	 *            The user's password.
	 * @return true if the connection succeeded, false if the supplied user name
	 *         and password were not recognized. Returns false also if the JDBC
	 *         driver isn't found.
	 */
	public boolean openConnection(String userName, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://puccini.cs.lth.se/" + userName, userName,
					password);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Close the connection to the database.
	 */
	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
		}
		conn = null;
	}

	/**
	 * Check if the connection to the database has been established
	 * 
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		return conn != null;
	}

	public void loginUser(String userId) {
		PreparedStatement ps = null;
		try {
			String sql = "SELECT user_name, name, addres, phone " +
					"FROM User " +
					"WHERE user_name = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				userId = rs.getString("user_name");
				CurrentUser.instance().loginAs(userId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Collection<String> getMovies() {
		PreparedStatement ps = null;
		ArrayList<String> movies = new ArrayList<String>();
		
		try {
			String sql = "SELECT name " +
					"FROM Movie";
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				movies.add(rs.getString("name"));
			}
			return movies;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Collection<Map<String, String>> getPerformances(String movieName) {
		PreparedStatement ps = null;
		ArrayList<Map<String, String>> performances = new ArrayList<Map<String, String>>();
		
		try {
			String sql = "SELECT show_date, movie_name, theatre_name " +
					"FROM Performance " +
					"WHERE movie_name = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, movieName);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Map<String, String> performance = new HashMap<String, String>(3);
				performance.put("show_date", rs.getString("show_date"));
				performance.put("movie_name", rs.getString("movie_name"));
				performance.put("theatre_name", rs.getString("theatre_name"));
				
				performances.add(performance);
			}
			return performances;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Map<String, String> getPerformanceData(String movieName, String date) {
		PreparedStatement ps = null;
		Map<String, String> performance = new HashMap<String, String>(4);
		
		try {
			String sql = "SELECT p.show_date, p.movie_name, p.theatre_name, t.seats - COUNT(r.nbr) AS free_seats " +
					"FROM Performance p " +
					"LEFT JOIN Reservation r ON (p.movie_name = r.movie_name AND p.show_date = r.show_date) " +
					"JOIN Theatre t ON (p.theatre_name = t.name) " +
					"WHERE p.movie_name = ? AND p.show_date = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, movieName);
			ps.setString(2, date); // Should propably be bound as date instead but whatever
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				performance.put("show_date", rs.getString("show_date"));
				performance.put("movie_name", rs.getString("movie_name"));
				performance.put("theatre_name", rs.getString("theatre_name"));
				performance.put("free_seats", rs.getString("free_seats"));
				
				return performance;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean bookTicket(String movieName, String date) {
		PreparedStatement ps = null;
		ArrayList<Map<String, String>> performances = new ArrayList<Map<String, String>>();
		
		try {
			conn.setAutoCommit(false);
			String sql = "INSERT INTO Reservation(user_name, show_date, movie_name) " +
					"VALUES (?, ?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, CurrentUser.instance().getCurrentUserId());
			ps.setString(2, date);
			ps.setString(3, movieName);
			ps.executeUpdate();
			ps.close();
			
			sql = "SELECT t.seats - COUNT(r.nbr) AS free_seats " +
					"FROM Performance p " +
					"JOIN Theatre t ON(t.name = p.theatre_name) " +
					"LEFT JOIN Reservation r ON(p.show_date = r.show_date AND p.movie_name = r.movie_name) " +
					"WHERE p.movie_name = ? AND p.show_date = ? ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, movieName);
			ps.setString(2, date);
			ResultSet rs = ps.executeQuery();
			rs.next();
			if(rs.getInt("free_seats") < 0) {
				conn.rollback();
				return false;
			} else {
				conn.commit();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		} finally {
			try {
				conn.setAutoCommit(true);
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
}
