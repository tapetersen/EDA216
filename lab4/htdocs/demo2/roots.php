<?php 
	session_start();
	$computationNbr = $_SESSION['computationNbr'];
	$computationNbr++;
	$_SESSION['computationNbr'] = $computationNbr;
?>

<html>
<head><title>Square Root Results</title><head>
<body>
<?php
	print "Root computation number $computationNbr<p>";
	$number = $_REQUEST['number'];
	if (is_numeric($number)) {
		if ($number >= 0) {
			print "The square root of $number is ";
			print sqrt($number);
		} else {
			print "The number must be >= 0.";
		}
	} else {
		print "$number isn't a number.";
	}
?>
	
<p>
Try another:
<p>

<form method = "get" action = "roots.php">
	<input type = "text" name = "number">
	<input type = "submit" value = "Compute root">
</form>
</body>
</html>
