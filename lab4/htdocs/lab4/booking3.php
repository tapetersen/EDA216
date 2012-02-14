<?php
	require_once('init.php');
	
	$user_name = $_SESSION['user_name'];
	$movie_name = $_SESSION['movie_name'];
	$performance_date = $_SESSION['performance_date'];
	
	if(!$user_name)
		redirect('index.html');
	
	if(!$movie_name)
		redirect('booking1.html');

	if(!$performance_date)
		redirect('booking2.html');
	
	$db->openConnection();
	if(is_post() && $_POST['submit']) {
		$_SESSION['booking_nbr'] = $db->bookTicket($user_name, $movie_name, $performance_date);
		redirect('booking4.php');
	}
	
	$performance = $db->getPerformance($movie_name, $performance_date);
	$full = $performance->free_seats == 0;
	$db->closeConnection();
?>

<html>
<head><title>Booking 3</title><head>
<body><h1>Booking 3</h1>
	Current user: <?php print $user_name ?>
	<p>
	Data for selected performance:
	<p>
	<dl>
		<dt>Movie: </dt>
		<dd><?php echo $performance->movie_name ?></dd>
		<dt>Date: </dt>
		<dd><?php echo $performance->show_date ?></dd>
		<dt>Theatre: </dt>
		<dd><?php echo $performance->theatre_name ?></dd>
		<dt>Free seats: </dt>
		<dd><?php echo $performance->free_seats ?></dd>
	</dl>
	<form method=post action="booking3.php">
		<button type="submit" <?php echo $full ? 'disabled="disabled"' : '' ?> name="submit" value="submit">book ticket</button>
	</form>
</body>
</html>
