<?php

include("../header.php");
$id = $_POST['id'];
$career = $_POST['career'];
$curriculum = $_POST['curriculum'];

mysqli_query($db, "update trainer set career = '$career', curriculum = '$curriculum' where id = '$id';");
echo "커리큘럼 수정 완료!";

?>