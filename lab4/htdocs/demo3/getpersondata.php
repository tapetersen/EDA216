<?php
require_once("MDB2.php");
$host = "puccini.cs.lth.se";
$username = "USERNAME";
$password = "PASSWORD";
$database = "DATABASE";
$dsn = array(
	'phptype' => 'mysqli',
	'hostspec' => $host,
	'username' => $username,
	'password' => $password,
	'database' => $database
);
$conn = MDB2::connect($dsn);
if (PEAR::isError($conn)) {
	die("Error while connecting: " . $conn->getMessage());
}

$resultset = $conn->query("select * from PersonPhones order by name");
if (PEAR::isError($resultset)) {
	die("Failed to issue query, error: " . $resultset->getMessage());
}
?>

<html>
<head><title>PHP PEAR Test</title><head>
<body><h2>Data from the PersonPhones table</h2>

<table border=1>
<tr><th>Name</th><th>Phone</th></tr>
<?php
$rowcount = 0;
while ($row = $resultset->fetchRow()) {
	$rowcount++;
	print "<tr>";
	foreach ($row as $attr){
		print "<td>";
		print htmlentities($attr);
		print "</td>";
	}
	print "</tr>";
}

$resultset->free();
$conn->disconnect();
?>
</table>

<p>
A total of <?php print "$rowcount"; ?> rows.
</body>
</html>
