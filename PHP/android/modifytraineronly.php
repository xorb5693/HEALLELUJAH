<?php

include("../header.php");
$id = $_POST['id'];
$field1 = $_POST['field1'];
$field2 = $_POST['field2'];
$field3 = $_POST['field3'];
$pay = $_POST['pay'];
$time = $_POST['time'];
$exp = $_POST['exp'];

mysqli_query($db, "update trainer set pay = $pay, time = '$time', exp = $exp, field1 = '$field1', field2 = '$field2', field3 = '$field3' where id = '$id';");

echo "트레이너 정보 수정 완료!";

?>