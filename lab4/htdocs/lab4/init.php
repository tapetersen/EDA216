<?php

require_once('database.inc.php');
require_once('mysql_connect_data.inc.php');

session_start();

if(!$_SESSION['db'])
	$_SESSION['db'] = new Database($userName, $password, $database);

$db = $_SESSION['db'];

function is_post() {
	return $_SERVER['REQUEST_METHOD'] === 'POST';
}

function redirect($location) {
	header("Location: $location");
	exit();
}

?>