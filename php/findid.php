<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');
   
   $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

   if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

		$email=$_POST['email'];
            
        if(empty($email)){
            $errMSG = "em을 입력하세요.";
        }
      
        if(!isset($errMSG))
        {
            $stmt = $con->prepare('select id from person where email=:email');
	$stmt->bindParam(':email', $email);
    $stmt->execute();
	

    if ($stmt->rowCount() > 0)
    {
        $data = array(); 

        while($row=$stmt->fetch(PDO::FETCH_ASSOC))
        {
            extract($row);
    
            array_push($data, 
                array('id'=>$id,
            ));
        }

        header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("find"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
        echo $json;
    }
        }

    }
?>
<?php 
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;


    if (!$android)
    {
?>
    <html>
       <body>

            <form action="<?php $_PHP_SELF ?>" method="POST">
            EMAIL: <input type = "text" name = "email" />
            <input type = "submit" name = "submit" />
        </form>
       
       </body>
    </html>

<?php 
    }
?>
