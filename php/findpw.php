<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');
   
   $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

   if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
		
		$id=$_POST['id'];
		$email=$_POST['email'];
        
		if(empty($id)){
            $errMSG = "id을 입력하세요.";
        }
        else if(empty($email)){
            $errMSG = "em을 입력하세요.";
        }
      
        if(!isset($errMSG))
        {
            $stmt = $con->prepare('select password from person where id=:id and email=:email');
			$stmt->bindParam(':id',$id);
			$stmt->bindParam(':email', $email);
			$stmt->execute();
	

    if ($stmt->rowCount() > 0)
    {
        $data = array(); 

        while($row=$stmt->fetch(PDO::FETCH_ASSOC))
        {
            extract($row);
    
            array_push($data, 
                array('pw'=>$password,
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
			ID: <input type = "text" name = "id" />
            EMAIL: <input type = "text" name = "email" />
            <input type = "submit" name = "submit" />
        </form>
       
       </body>
    </html>

<?php 
    }
?>