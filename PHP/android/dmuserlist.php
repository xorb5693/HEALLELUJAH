<?php

include("../header.php");

$id = $_POST['id'];
#$id = 'test1';
#$id = 'trainer8';
$checkid = $_POST['checkid'];
#$checkid = 1;
#$checkid = 0;
$info = "";

if ($checkid == 1) {
	$result = mysqli_query($db, "select trainerid, name, age, sex from user, matching where trainerid = id and healthuserid = '$id';");
	if ($result) {
		while($list = mysqli_fetch_array($result)) {
			$info = "$list[0]|$list[1]|$list[2]|$list[3]\r\n";
			echo $info;
		}
	}
} else {
	$result = mysqli_query($db, "select healthuserid, name, age, sex from user, matching where healthuserid = id and trainerid = '$id'");
	if ($result) {
		while($list = mysqli_fetch_array($result)) {
			$info = "$list[0]|$list[1]|$list[2]|$list[3]\r\n";
			echo $info;
		}
	}
}

?>