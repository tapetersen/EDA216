<?php
	require_once('init.php');
	
	$user_name = $_SESSION['user_name'];
	$movie_name = $_SESSION['movie_name'];
	
	if(!$user_name)
		redirect('index.html');
	
	if(!$movie_name)
		redirect('booking1.html');
	
	if(is_post() && $_POST['performance_date']) {
		$_SESSION['performance_date'] = $_POST['performance_date'];
		redirect('booking3.php');
	}
	
	$db->openConnection();
	$performanceDates = $db->getPerformanceDates($movie_name);
	$db->closeConnection();
?>

<html>
<head><title>Booking 2</title><head>
<body><h1>Booking 2</h1>
	Current user: <?php print $user_name ?>
	<p>
	Performances:
	<p>
	<form method=post action="booking2.php">
		<select name="performance_date" size=10>
		<?php
			$first = true;
			foreach ($performanceDates as $date) {
				if ($first) {
					print "<option selected>";
					$first = false;
				} else {
					print "<option>";
				}
				print $date;
				print "</option>";
			}
		?>
		</select>		
		<button type=submit value="submit">Select movie</button>
	</form>
</body>
</html>
