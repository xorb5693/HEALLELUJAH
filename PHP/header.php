<?php
header('Content-Type: text/html; charset=UTF-8');
error_reporting(E_ALL);
ini_set("display_errors", 1);

#connect를 위한 데이터베이스의 정보를 헤더에 저장해 둡니다.
$db_ip = "";
$db_id = "";
$db_pass = "";
$db_select = "";
$db = mysqli_connect($db_ip, $db_id, $db_pass, $db_select);
$url = "";
mysqli_set_charset($db, 'utf8');
?>