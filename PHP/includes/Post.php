 <?php
  class Post{
    private $conn;
    //private $iban = 'IE1BOFI1';

    public $user_pin;

    public function __Construct($db){
      $this->conn = $db;
    }

    public function read($iban){
      //prepare bind and execute the query
      $stmt = $this->conn->prepare('SELECT user_pin FROM account WHERE iban = \'' . $iban . '\';');
      $stmt->execute();
      return $stmt;
    }
  }
