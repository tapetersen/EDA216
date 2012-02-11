<?php
	require_once('database.inc.php');
	
	session_start();
	$db = $_SESSION['db'];
	$userId = $_SESSION['userId'];
	$db->openConnection();
	
	$movieNames = $db->getMovieNames();
	$db->closeConnection();
?>

<html>
<head><title>Booking 1</title><head>
<body><h1>Booking 1</h1>
	Current user: <?php print $userId ?>
	<p>
	Movies showing:
	<p>
	<form method=post action="booking2.php">
		<select name="movieName" size=10>
		<?php
			$first = true;
			foreach ($movieNames as $name) {
				if ($first) {
					print "<option selected>";
					$first = false;
				} else {
					print "<option>";
				}
				print $name;
			}
		?>
		</select>		
		<input type=submit value="Select movie">
	</form>
</body>
</html>
