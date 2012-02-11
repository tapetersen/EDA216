<?php
	session_start();
	$_SESSION['computationNbr'] = 0;
?>

<html>
<head><title>Square Roots</title></head>
<body>
<h1 align = "center">Fill in some data</h1>

<p>
This program works out square roots.
<p>

<form method = "get" action = "roots.php">
	<input type = "text" name = "number">
	<input type = "submit" value = "Compute root">
</form>
</body>
</html>
