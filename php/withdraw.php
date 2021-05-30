<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

	$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( ($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit']) || $android)
    {

        $id=$_POST['id'];
        $password=$_POST['password'];
		

        if(!isset($errMSG))
        {
            try{
                $stmt = $con->prepare('delete from person where id=:id and password=:password');
                $stmt->bindParam(':id', $id);
                $stmt->bindParam(':password', $password);

                if($stmt->execute())
                {
                    $successMSG = "해당 사용자 탈퇴 완료";
                }
                else
                {
                    $errMSG = "사용자 탈퇴 에러";
                }

            } catch(PDOException $e) {
                die("Database error: 중복체크 해주세요."); 
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
            password: <input type = "text" name = "password" />
            <input type = "submit" name = "submit" />
        </form>
   
   </body>
</html>
<?php 
    }
?>