<?php

header("Access-Control-Allow-Origin: *");
require_once '../includes/DbOperations.php';
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){

  if(isset($_POST['from_iban']) and isset($_POST['to_iban']) and isset($_POST['amount']) and isset($_POST['trans_date']))
  {
    $db = new DbOperations();
    if($db->createTransaction($_POST['from_iban'], $_POST['to_iban'], $_POST['amount'], $_POST['trans_date'])
    and $db->updateCurrentAccount($_POST['amount'], $_POST['from_iban'])
    and $db->updateSavingsAccount($_POST['amount'], $_POST['to_iban'])){
      $response['error'] = false;
      $response['message'] = "transaction successful";
    }else{
      $response['error'] = true;
      $response['message'] = "Error Occurred";
    }

  }else{
    $response['error'] = true;
    $response['message'] = "Fields Missing";
  }

}else{
  $response['error'] = true;
  $response['message'] = "Invalid Request";
}

echo json_encode($response);
