<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');
   
   $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

   if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

        $id=$_POST['id'];
            
        if(empty($id)){
            $errMSG = "id을 입력하세요.";
        }

      
        if(!isset($errMSG))
        {
            try{
                $stmt = $con->prepare('select id, password from person where id=:id');
                $stmt->bindParam(':id', $id);
				$count=$stmt->rowCount($stmt->execute());
               
				$data = array();
                if($count==0)
                {
			   $successMSG = "중복된 아이디가 없습니다.";

               $code = '1565';
               
               array_push($data,array('code'=>$code));
               
                }
                else
                {
                    $errMSG = "중복된 아이디가 있습니다.";
					$code = '1234';
				array_push($data,array('code'=>$code));
                }
				header('Content-Type: application/json; charset=utf8');
               $json = json_encode(array("start"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
               echo $json;

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
            <input type = "submit" name = "submit" />
        </form>
       
       </body>
    </html>

<?php 
    }
?>
