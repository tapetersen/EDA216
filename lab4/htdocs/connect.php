<?php
$mysqli = new mysqli("puccini.cs.lth.se", "phpuser", 
                     "notsecret");
if(mysqli_connect_error()) {
	$error = "Connection error: ".mysqli_connect_error();
	die($error);
}

$result = $mysqli->query("select now()");
if ($mysqli->error) {
	$error = "SQL error: ".$mysqli->error;
	die($error);
}
?>

<html>
<head><title>MySQL Connection Test</title><head>
<body>
<h2>MySQL Connection Test</h2>

Now is (fetched from puccini): 
<?php 
	$row = $result->fetch_row();
	print $row[0];
	print ".";
	$result->free_result();
	$mysqli->close();
?>
</body>
</html>
