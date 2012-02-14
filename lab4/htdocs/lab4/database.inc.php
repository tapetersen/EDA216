<?php
/*
 * Class Database: interface to the movie database from PHP.
 *
 * You must:
 *
 * 1) Change the functions userExists and getMovieNames so the
 *    SQL queries are appropriate for your tables.
 * 2) Write more functions.
 *
 */
require_once("MDB2.php");
class Database {
	private $userName;
	private $password;
	private $database;
	private $conn;
	
	/**
	 * Constructs a database object for the specified user.
	 */
	public function __construct($userName, $password, $database) {
		$this->userName = $userName;
		$this->password = $password;
		$this->database = $database;
	}
	
	/** 
	 * Opens a connection to the database, using the earlier specified user
	 * name and password.
	 *
	 * @return true if the connection succeeded, false if the supplied
	 * user name and password were not recognized.
	 */
	public function openConnection() {
		$connectString = "mysqli://" . $this->userName . 
			$this->database;
		$this->conn = MDB2::connect($connectString);
		if (PEAR::isError($this->conn)) {
			$error = "Connection error: " . $this->conn->getMessage().' '.$this->conn->getDebugInfo();
			print $error . "<p>";
			unset($this->conn);
			return false;
		}
		return true;
	}
	
	/**
	 * Closes the connection to the database.
	 */
	public function closeConnection() {
		if (isset($this->conn)) {
			$this->conn->disconnect();
			unset($this->conn);
		}
	}

	/**
	 * Checks if the connection to the database has been established.
	 *
	 * @return true if the connection has been established
	 */
	public function isConnected() {
		return isset($this->conn);
	}
	
	/**
	 * Execute a database query (or insert/update).
	 *
	 * @param The query string (SQL)
	 * @return The result set, or the number of affected rows on
	 * an insert/update
	 */
	private function executeQuery($sql, $types=null, $values=null) {
		
		$results = array();
		if(!is_null($values)) {
			$statement = $this->conn->prepare($sql, $types);
			if(PEAR::isError($statement))
				throw new Exception($statement->getMessage()." ".$statement->getUserInfo());
			$resultset = $statement->execute($values);
			$statement->free();
		}
		
		else {
			$resultset = $this->conn->query($sql);
		}
		
		if(PEAR::isError($resultset)) {
			throw new Exception($resultset->getMessage()." ".$resultset->getUserInfo());
		}
	
		return $resultset;
	}
	
	/**
	 * Check if a user with the specified user id exists in the database.
	 * Queries the Users database table.
	 *
	 * @param userId The user id 
	 * @return true if the user exists, false otherwise.
	 */
	public function userExists($userId) {
		$sql = "SELECT user_name from User WHERE user_name = ?";
		$result = $this->executeQuery($sql, null, $userId);
		$ret = $result->numRows() == 1; 
		$result->free();
		return $ret; 
	}

	/** 
	 * Get the names of movies that are currently showing. Queries
	 * the Performances database table.
	 *
	 * @return The array of movie names.
	 */
	public function getMovieNames() {
		$sql = "SELECT DISTINCT movie_name FROM performance"; 
		$result = $this->executeQuery($sql);
		$ret = array();
		while ($row = $result->fetchRow()) {
			$ret[] = $row[0];
		}
		$result->free();
		return $ret;
	}
	
	public function getPerformanceDates($movie_name) {
		$sql = "SELECT show_date FROM performance WHERE movie_name = ?";
		$result = $this->executeQuery($sql, null, $movie_name);
		$ret = array();
		while ($row = $result->fetchRow()) {
			$ret[] = $row[0];
		}
		$result->free();
		return $ret;
	}
	
	public function getPerformance($movie_name, $show_date) {
		$sql = "SELECT p.show_date, p.movie_name, p.theatre_name, t.seats - COUNT(r.nbr) AS free_seats " .
							"FROM Performance p " .
							"LEFT JOIN Reservation r ON (p.movie_name = r.movie_name AND p.show_date = r.show_date) " .
							"JOIN Theatre t ON (p.theatre_name = t.name) " .
							"WHERE p.movie_name = ? AND p.show_date = ?";
		$result = $this->executeQuery($sql, null, array($movie_name, $show_date));
		$row = $result->fetchRow(MDB2_FETCHMODE_OBJECT);
		$result->free();
		return $row;
	}
	
	public function bookTicket($user_name, $movie_name, $show_date) {
		$this->conn->loadModule('Extended');
		
		// Will be rolled back when we exit this scope if not committed
		$transaction = new Transaction($this->conn);
		
		$insertSql = "INSERT INTO Reservation(user_name, show_date, movie_name) " .
					"VALUES (?, ?, ?)";
		
		$checkSql = "SELECT t.seats - COUNT(r.nbr) AS free_seats " .
					"FROM Performance p " .
					"JOIN Theatre t ON(t.name = p.theatre_name) " .
					"LEFT JOIN Reservation r ON(p.show_date = r.show_date AND p.movie_name = r.movie_name) " .
					"WHERE p.movie_name = ? AND p.show_date = ? ";
		
		$res = $this->executeQuery($insertSql, null, array($user_name, $show_date, $movie_name));
		if($res != 1)
			return false;
		
		$res = $this->executeQuery($checkSql, null, array($movie_name, $show_date));
		if($res->fetchOne()<0)
			return false;
		else {
			$transaction->commit();
			return $this->conn->lastInsertId('Performance');
		}
	}
}


/*
* Class Transaction: Class for RAII of transactions
*
* This class will start a transaction as soon as it's constructed and does a rollback
* when it's destroyed if you don't commit before that
*
*/
class Transaction {
	
	private $conn;
	
	public function __construct($conn) {
		$this->conn = $conn;
		$this->conn->beginTransaction();
	}
	
	public function commit() {
		$this->conn->commit();
		unset($this->conn);
	}
	
	public function rollback() {
		$this->conn->rollback();
		unset($this->conn);
	}
	
	public function __destruct() {
		if($this->conn)
			$this->rollback();
	}
	
}
?>