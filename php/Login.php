<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');
   
   $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

   if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

        $id=$_POST['id'];
        $password=$_POST['password'];
            
        if(empty($id)){
            $errMSG = "id을 입력하세요.";
        }
        else if(empty($password)){
            $errMSG = "비밀번호를 입력하세요.";
        }
      
        if(!isset($errMSG))
        {
            try{
                $stmt = $con->prepare('select id, password from person where id=:id and password=:password');
                $stmt->bindParam(':id', $id);
                $stmt->bindParam(':password', $password);
            $count=$stmt->rowCount($stmt->execute());
               
   
                if($count==1)
                {
               
               $_SESSION['login_user']=$id;
               
               //header("localtion:start.php"); //안드로이드에 메인화면을 요청하는 php 던지기?
               $successMSG = "로그인 성공.";
               $code = 'success';
               $data = array();
               array_push($data,array('code'=>$code));
               
               header('Content-Type: application/json; charset=utf8');
               $json = json_encode(array("start"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
               echo $json;
                }
                else
                {
                    $errMSG = "로그인 실패";
                }

            } catch(PDOException $e) {
                die("Database error: " . $e->getMessage()); 
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
            PASSWORD: <input type = "text" name = "password" />
            <input type = "submit" name = "submit" />
        </form>
       
       </body>
    </html>

<?php 
    }
?>
