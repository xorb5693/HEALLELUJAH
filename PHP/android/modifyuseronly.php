<?php

include("../header.php");
$id = $_POST['id'];
$height = $_POST['height'];
$weight = $_POST['weight'];
$object1 = $_POST['object1'];
$object2 = $_POST['object2'];
$object3 = $_POST['object3'];
$pay = $_POST['pay'];

mysqli_query($db, "update healthuser set pay = $pay, height = $height, weight = $weight, object1 = '$object1', object2 = '$object2', object3 = '$object3' where id = '$id';");

echo "회원 정보 수정 완료!";
?>