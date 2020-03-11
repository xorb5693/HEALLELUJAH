<?php
	include("../header.php");

	//공통 속성
	$id = $_POST["id"];
	$password = $_POST["password"];
	$name = $_POST["name"];
	$age = $_POST["age"];
	$checkid = $_POST["checkid"];
	$sex = $_POST["sex"];
	$phonenumber = $_POST["phonenumber"];

	//if(strcmp($pass1,$pass2) != 0) {echo"비밀번호와 비밀번호 확인이 서로 다릅니다";	exit();}
	//if (strlen($id) <= 4 || strlen($pass1) <= 4) {echo "아이디 혹은 비밀번호가 너무 짧습니다."; exit();}

	$password = hash('sha256', $password);

	mysqli_query($db, "insert into user values('$id', '$password', '$name', $age, $checkid, '$sex', '$phonenumber');");

	if($checkid == 1){
		//healthuser 속성
		$pay = $_POST["pay"];
	 	$address1 = $_POST["address1"];
		$address2 = $_POST["address2"];
		$height = $_POST["height"];
		$weight = $_POST["weight"];
		$object1 = $_POST["object1"];
		$object2 = $_POST["object2"];
		$object3 = $_POST["object3"];
		mysqli_query($db, "insert into healthuser values('$id', $pay, $height, $weight,'$object1','$object2','$object3', '$address1', '$address2');");
	}else{
		//trainer 속성
		$pay = $_POST["pay"];
		$address1 = $_POST["address1"];
		$address2 = $_POST["address2"];
		$address = $address1.$address2;
		$career = $_POST["career"];
		$curriculum = $_POST["curriculum"];
		
		exec("python coordinate.py $address", $output);
		$latitude = (float)$output[0];
		$longtitude = (float)$output[1];
		//$latitude = $_POST["latitude"];
		//$longtitude = $_POST["longtitude"];
		
		$time = $_POST["time"];
		$exp = $_POST["exp"];
		$field1 = $_POST["field1"];
		$field2 = $_POST["field2"];
		$field3 = $_POST["field3"];

		mysqli_query($db, "insert into trainer values('$id', $pay, '$career', $latitude, $longtitude, '$curriculum', now(), '$time', $exp, '$field1', '$field2', '$field3', '$address1', '$address2');");
	}

	echo "회원가입 성공!";

 ?>
