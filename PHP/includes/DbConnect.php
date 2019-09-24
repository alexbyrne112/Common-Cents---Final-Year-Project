<?php
  class DbConnect{

    private $con;

    function __consrtuct(){

    }
    function connect(){
      require_once dirname(__FILE__).'/Constants.php';
      $this->con = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

      if(mysqli_connect_errno()){
        echo "Failed to connect to the database".mysqli_connect_err();
      }
      return $this->con;
    }
  }
