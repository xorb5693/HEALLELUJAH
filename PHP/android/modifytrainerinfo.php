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
$address = "$address1 $address2";

exec("python coordinate.py $address", $output);
$lat = (float)$output[0];
$lng = (float)$output[1];

$phonenumber = $_POST['phonenumber'];

mysqli_query($db, "update user set password = '$pass', name = '$name', sex = '$sex', phonenumber = '$phonenumber' where id = '$id';");
mysqli_query($db, "update trainer set address1 = '$address1', address2 = '$address2', latitude = $lat, longtitude = $lng where id = '$id';");
echo "트레이너 기본 정보 수정 완료!";

?>