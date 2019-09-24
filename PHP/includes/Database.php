<?php
  class Database{
    //params
    private $host = 'us-cdbr-iron-east-03.cleardb.net';
    private $db_name = 'heroku_315bc8a1485586f';
    private $username = 'bc1e135ed48a0f';
    private $password = '9a757205';
    private $conn;

    //connect
    public function connect(){
      $this->conn = null;
      try{
        $this->conn = new PDO('mysql:host=' . $this->host . ';dbname=' . $this->db_name, $this->username, $this->password);
        $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
      }catch(PDOException $e){
        echo 'Connection Error: ' . $e->getMessage();
      }
      return $this->conn;
    }
  }
