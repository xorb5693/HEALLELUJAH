<?php

include("../header.php");

$healthuserid = $_POST['healthuserid'];
$trainerid = $_POST['trainerid'];
$healthname = $_POST['healthname'];
$trainername = $_POST['trainername'];

$result = mysqli_query($db, "select exists (select * from matching where healthuserid = '$healthuserid' and trainerid = '$trainerid') as success;");
#echo "select exists (select * from matching where healthuserid = '$healthuserid' and trainerid = '$trainerid') as success;";
$overlap = mysqli_fetch_array($result);

if ($overlap[0] == 0) {
	mysqli_query($db, "insert into matching values('$healthuserid', '$trainerid');");
	mysqli_query($db, "insert into dm values('회원 ".$healthname."와(과) 트레이너 ".$trainername."이(가) 매칭되었습니다.', '$healthuserid', '$trainerid', 1, now());");

	echo "매칭되었습니다.";
} else {
	echo "이미 매칭된 사용자입니다.";
}


?>