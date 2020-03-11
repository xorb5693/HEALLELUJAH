<?php
	include("../header.php");

	$object1 = $_POST['object1'];
	#$object1 = "린매스업";
	$object2 = $_POST['object2'];
	#$object2 = "다이어트";
	$object3 = $_POST['object3'];
	#$object3 = "체형교정";
	$address = $_POST['address'];
	#$address = "강원도 원주시 흥업면 남원로 150";
	$pay = $_POST['pay'];
	#$pay = "10";

	#$pyquery = "python coordinate.py $address";
	exec("python coordinate.py $address", $out);
	#echo $pyquery."<br>";
	#echo $out[0]."<br>";
	$latitude = $out[0];
	$longtitude = $out[1];

	#$pyquery = "python similarity.py $object1 $object2 $object3 $latitude $longtitude $pay";
	exec("python similarity.py $object1 $object2 $object3 $latitude $longtitude $pay", $similarity);
	#echo $pyquery."<br>";
	#echo count($similarity)."<br>";
	#echo $similarity[0];
	$info = "";

	if (count($similarity) != 0) {
		$result = mysqli_query($db, "select id, name, age, sex from user where id = '$similarity[0]';");
		$row = mysqli_fetch_array($result);
		$info = "$row[0]|$row[1]|$row[2]|$row[3]";
	
		for($i = 1; $i < count($similarity); $i++) {
			#echo $similarity[$i]."<br>";
			$result = mysqli_query($db, "select id, name, age, sex from user where id = '$similarity[$i]';");
			$row = mysqli_fetch_array($result);
			
			$info = $info.":$row[0]|$row[1]|$row[2]|$row[3]";
		}
	}

	echo $info;
?>