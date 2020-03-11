<?php

include("../header.php");
$id = $_POST['id'];
#$id = 'trainer7';

$result = mysqli_query($db, "select name, age, sex, address1, address2, field1, field2, field3, pay, time, curriculum, career from user, trainer where user.id = trainer.id and user.id = '$id';");

$row = mysqli_fetch_array($result);

echo "$row[0]|$row[1]|$row[2]|$row[3] $row[4]|$row[5]|$row[6]|$row[7]|$row[8]|$row[9]|$row[10]|$row[11]";
?>