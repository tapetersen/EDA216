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
	public function __construct($userName, $password, $database, $host) {
		$this->host = $host;
		$this->userName = $userName;
		$this->password = $password;
		$this->database = $database;
	}
	
	/** 
	 * Opens a connection to the database, using the earlier specified user
	 * name and password.
	 *
	 * @return true if the connection succeeded, false on failure
	 */
	public function openConnection() {
		$dsn = "mysql:host=$this->host;dbname=$this->database";
		try {
			$this->conn = new PDO($dsn, $this->username, $this->password);
			$this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		} catch (PDOException $e) {
			unset ($this->conn);
			return false;
		}
	}
	
	/**
	 * Closes the connection to the database.
	 */
	public function closeConnection() {
		unset($this->conn);
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
	 * Check if a user with the specified user id exists in the database.
	 * Queries the Users database table.
	 *
	 * @param userId The user id 
	 * @return true if the user exists, false otherwise.
	 */
	public function userExists($userId) {
		$sql = "SELECT user_name FROM User WHERE user_name = :user_name";
		$ps = $this->conn->prepare($sql);
		$ps->execute(array('user_name' => $userId));
		if($ps->fetch())
			return true;
		else 
			return false;
		$ps->closeCursor();
	}

	/** 
	 * Get the names of movies that are currently showing. Queries
	 * the Performances database table.
	 *
	 * @return The array of movie names.
	 */
	public function getMovieNames() {
		$sql = "SELECT distinct movie_name from Performance";
		$ps = $this->conn->prepare($sql);
		$ps->execute();
		$res = $ps->fetchAll(PDO::FETCH_ASSOC);
		$ps->closeCursor();
		return $res;
	}
	
	/*
	 * *** Add functions ***
	 */
}
?>
