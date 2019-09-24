<?php

  class DbOperations{

    private $con;

    function  __construct(){
      require_once dirname(__FILE__).'/DbConnect.php';

      $db = new DbConnect();

      $this->con = $db->connect();
    }

    function createTransaction($from_iban, $to_iban, $amount, $trans_date)
    {
      $stmt = $this->con->prepare("INSERT INTO `transaction` (`from_iban`, `to_iban`, `amount`, `trans_date`) VALUES (?, ?, ?, ?);");

      $stmt->bind_param("ssss",$from_iban, $to_iban, $amount, $trans_date);

      if($stmt->execute()){
        return true;
      }else{
        return false;
      }
    }

    function updateCurrentAccount($amount, $from_iban)
    {
      ///UPDATE account SET Balance = Balance + 3 WHERE iban = "IE1BOFI2";
      $stmtCurrent = $this->con->prepare("UPDATE account SET Balance = Balance - ? WHERE iban = ?;");
      $stmtCurrent->bind_param("ss", $amount, $from_iban);

      if($stmtCurrent->execute()){
        return true;
      }else{
        return false;
      }
    }

    function updateSavingsAccount($amount, $to_iban)
    {
      ///UPDATE account SET Balance = Balance - 3 WHERE iban = "IE1BOFI2";
      $stmtSavings = $this->con->prepare("UPDATE account SET Balance = Balance + ? WHERE iban = ?;");
      $stmtSavings->bind_param("ss", $amount, $to_iban);

      if($stmtSavings->execute()){
        return true;
      }else{
        return false;
      }
    }

  }
