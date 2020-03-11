<?php

include("../header.php");
$id = $_POST['id'];
$pass = $_POST['password'];
$pass = hash('sha256', $pass);
$name = $_POST['name'];
$sex = $_POST['sex'];
$age = $_POST['age'];
$address1 = $_POST['address1'];
$address2 = $_POST['address2'];
$phonenumber = $_POST['phonenumber'];

mysqli_query($db, "update user set password = '$pass', name = '$name', sex = '$sex', phonenumber = '$phonenumber' where id = '$id';");
mysqli_query($db, "update healthuser set address1 = '$address1', address2 = '$address2' where id = '$id';");
echo "회원 기본 정보 수정 완료!";

?>