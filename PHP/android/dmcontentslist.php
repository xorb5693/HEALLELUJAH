<?php
#두 유저 사이의 DB에서 쪽지를 읽어오는 php
include("../header.php");
#healthuser와 trainer의 아이디를 가져옴
$healthuser = $_POST['healthuser'];
#$healthuser = 'test1';
$trainer = $_POST['trainer'];
#$trainer = 'trainer8';

#dm 테이블에서 senderid가 healthuser의 아이디고 recieverid가 trainer의 아이디이거나 senderid가 trainer의 아이디고 reciverid가 healthuser의 아이디인 경우를 모두 가져 옴
$result = mysqli_query($db, "select contents, senderid, receiverid from dm where senderid = '$healthuser' and receiverid = '$trainer' or senderid = '$trainer' and receiverid = '$healthuser' order by number asc;");

#안드로이드 어플리케이션으로 해당 리스트를 모두 출력
if ($result) {
	while($ms = mysqli_fetch_array($result)) {
		$info = "$ms[0]|$ms[1]|$ms[2]\r\n";
        #$info = contents|senderid|receiverid
		echo $info;
	}
}
?>