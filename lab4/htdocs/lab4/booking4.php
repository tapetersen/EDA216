<?php
	require_once('init.php');
	
	$user_name = $_SESSION['user_name'];
	$movie_name = $_SESSION['movie_name'];
	$performance_date = $_SESSION['performance_date'];
	$booking_nbr = $_SESSION['booking_nbr'];
	
	if(!$user_name)
		redirect('index.html');
	
	if(!$movie_name)
		redirect('booking1.html');

	if(!$performance_date)
		redirect('booking2.html');
	
	if(is_post()) {
		$_SESSION = array();
		redirect('index.html');
	}
	
	
?>

<html>
<head><title>Booking 4</title><head>
<body><h1>Booking 4</h1>
	Current user: <?php print $user_name ?>
	<p>
	<?php 
		if($booking_nbr)
			echo "One ticket booked, nbr: $booking_nbr";
		else
		  	echo "Failed to book ticket";
	?>
	<form method=post action="booking4.php">
		<button type="submit" name="submit" value="submit">New ticket</button>
	</form>
</body>
</html>
