<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

	$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( ($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit']) || $android)
    {

        $id=$_POST['id'];
        $password=$_POST['password'];
		$email=$_POST['email'];

        if(empty($id)){
            $errMSG = "ID(숫자)를 입력하세요.";
        }
        else if(empty($password)){
            $errMSG = "비밀번호를 입력하세요.";
        }
		else if(empty($email)){
            $errMSG = "이메일을 입력하세요.";
        }

        if(!isset($errMSG))
        {
            try{
                $stmt = $con->prepare('INSERT INTO person(id, password, email) VALUES(:id, :password, :email)');
                $stmt->bindParam(':id', $id);
                $stmt->bindParam(':password', $password);
				$stmt->bindParam(':email', $email);

                if($stmt->execute())
                {
                    $successMSG = "새로운 사용자를 추가했습니다.";
                }
                else
                {
                    $errMSG = "사용자 추가 에러";
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
			E-Mail: <input type = "text" name = "email" />
            <input type = "submit" name = "submit" />
        </form>
   
   </body>
</html>
<?php 
    }
?>