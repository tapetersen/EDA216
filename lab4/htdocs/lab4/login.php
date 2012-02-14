<?php
	require_once('init.php');
	
	$db->openConnection();
	if (! $db->isConnected()) {
		//header("Location: cannotConnect.html");
		exit();
	}
	
	if(!is_post())
		redirect('index.html');
	
	$user_name = $_POST['user_name'];
	if (! $db->userExists($user_name)) 
		redirect('index.html');
	
	$_SESSION['user_name'] = $user_name;
	redirect("booking1.php");
?>
