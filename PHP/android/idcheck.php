<?php
  include("../header.php");

  $id = $_POST["id"];

  $result = mysqli_query($db, "select exists (select * from user where id = '$id') as success;");
  $overlap = mysqli_fetch_array($result);

  if ($overlap[0] == 1) {
    echo"중복된 아이디입니다.";
      exit();
  }else{
    echo"중복된 아이디가 아닙니다.";
    exit();
  }
 ?>
