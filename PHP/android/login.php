<?php

include("../header.php");
$id=$_POST['id'];
$pass=$_POST['pass'];
$pass = hash('sha256', $pass);
#$id = "test";
#$pass = "pass";

#$check="SELECT * from user_info WHERE userid=$id";
$result=mysqli_query($db, "select exists (select * from user where id = '$id') as success;");
$row = mysqli_fetch_array($result);

if($row[0] == 1)
{
	$result=mysqli_query($db, "select password from user where id = '$id';");
	$row = mysqli_fetch_array($result);

	if(strcmp($row[0], $pass) == 0){
		$check = mysqli_query($db, "select name, age, checkid, sex, phonenumber from user where id = '$id' and password = '$pass';");
		$data = mysqli_fetch_array($check);
		$info = "$data[2]|$id|$data[0]|$data[1]|$data[3]|$data[4]";
		#$info = checkid:id:name:age:sex:phonenumber

		if ($data[2] == 0) {
			$health = mysqli_query($db, "select field1, field2, field3, pay, address1, address2, curriculum, career, time, exp from trainer where id = '$id';");
			$trainer = mysqli_fetch_array($health);
			$info = $info."|$trainer[0]|$trainer[1]|$trainer[2]|$trainer[3]|$trainer[4]|$trainer[5]|$trainer[6]|$trainer[7]|$trainer[8]|$trainer[9]";
			#$info = checkid:id:name:age:sex:phonenumber:field1:fidel2:field3:pay:address1:address2:curriculum:carrer:time:exp
		} else if ($data[2] == 1) {
			$health = mysqli_query($db, "select object1, object2, object3, pay, address1, address2, height, weight from healthuser where id = '$id';");
			$user = mysqli_fetch_array($health);
			$info = $info."|$user[0]|$user[1]|$user[2]|$user[3]|$user[4]|$user[5]|$user[6]|$user[7]";
			#$info = checkid:id:name:age:sex:phonenumber:object1:object2:object3:pay:address1:address2:height:weight
		}

		echo $info;
	}
	else{
		echo "비밀번호가 틀립니다.";
	}
}
else{
  echo "가입되지 않은 아이디입니다.";
}

?>