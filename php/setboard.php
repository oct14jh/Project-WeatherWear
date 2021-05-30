<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

	$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( ($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit']) || $android)
    {
        $id=$_POST['id'];
		$content=$_POST['content'];  
		
		if(empty($id)){
            $errMSG = "ID(숫자)를 입력하세요.";
        }
        else if(empty($content)){
            $errMSG = "내용을 입력하세요.";
        }

        if(!isset($errMSG))
        {
            try{
                $stmt = $con->prepare('UPDATE board SET content=:content WHERE id=:id');
				$stmt->bindParam(':content', $content);
				$stmt->bindParam(':id', $id);
				

                if($stmt->execute())
                {
                    $successMSG = "수정완료";
                }
                else
                {
                    $errMSG = "Error";
                }

            } catch(PDOException $e) {
                die("Database error"); 
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
            content: <input type = "text" name = "content" />
            <input type = "submit" name = "submit" />
        </form>
   
   </body>
</html>
<?php 
    }
?>