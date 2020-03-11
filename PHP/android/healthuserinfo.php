<?php

include("../header.php");
$id = $_POST['id'];
#$id = 'test4';

$result = mysqli_query($db, "select user.id, name, age, sex, object1, object2, object3, pay, height, weight from user, healthuser where user.id = healthuser.id and user.id = '$id';");

if ($result) {
	$row = mysqli_fetch_array($result);
	echo "$row[0]|$row[1]|$row[2]|$row[3]|$row[4]|$row[5]|$row[6]|$row[7]|$row[8]|$row[9]";
}

?>