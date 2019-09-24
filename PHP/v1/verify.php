<?php
  header("Access-Control-Allow-Origin: *");
  header("Content-Type: application/json");

  require_once '../includes/Database.php';
  require_once '../includes/Post.php';

  $database = new Database();
  $db = $database->connect();

  $post = new Post($db);
  $result = $post->read($_GET['iban']);
  $num = $result->rowCount();
  if($num > 0){
    $posts_arr = array();
    $posts_arr['data'] = array();

    while($row = $result->fetch(PDO::FETCH_ASSOC)){
      extract($row);
      $post_item = array('user_pin'=>$user_pin);
      array_push($posts_arr['data'], $post_item);
    }
    echo json_encode($posts_arr);
  }else{
    echo json_encode(array('message'=> 'No Posts'));
  }
