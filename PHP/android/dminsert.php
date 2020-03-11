<?php

#쪽지 전송을 위한 PHP입니다.
include("../header.php");

$contents = $_POST['contents'];
#$contents = 'test';
$senderid = $_POST['senderid'];
#$senderid = 'test1';
$receiverid = $_POST['receiverid'];
#$receiverid = 'trainer2';

#두 유저 사이에서 오간 쪽지 번호 중 가장 큰 숫자를 찾습니다.
$result = mysqli_query($db, "select ifnull(max(number), 0) from dm where senderid = '$senderid' and receiverid = '$receiverid' or senderid = '$receiverid' and receiverid = '$senderid';");
$max = mysqli_fetch_array($result);
$max = $max[0] + 1;

#senderid가 reciverid에게 보낸 contents를 max값의 순서 번호로 DB에 저장합니다.
mysqli_query($db,"insert into dm values('$contents','$senderid','$receiverid', $max, now());");
#echo "insert into dm values('$contents','$senderid', '$receiverid', $max, now());";

echo"쪽지를 보냈습니다";
?>
