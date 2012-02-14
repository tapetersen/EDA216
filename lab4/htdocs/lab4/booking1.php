<?php
	require_once('init.php');
	
	$user_name = $_SESSION['user_name'];
	
	if(!$user_name)
		redirect('index.html');
	
	if(is_post() && isset($_POST['movie_name'])) {
		$_SESSION['movie_name'] = $_POST['movie_name'];
		redirect('booking2.php');
	}
	
	$db->openConnection();
	$movieNames = $db->getMovieNames();
	$db->closeConnection();
?>

<html>
<head><title>Booking 1</title><head>
<body><h1>Booking 1</h1>
	Current user: <?php print $user_name ?>
	<p>
	Movies showing:
	<p>
	<form method=post action="booking1.php">
		<select name="movie_name" size=10>
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
		<button type=submit value="submit" >
			Select movie
		</button>
	</form>
</body>
</html>
