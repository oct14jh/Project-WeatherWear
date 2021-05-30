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

    $stmt = $con->prepare('select image,top,bottom from img where id=:id');
	$stmt->bindParam(':id', $id);
    $stmt->execute();
	

    if ($stmt->rowCount() > 0)
    {
        $data = array(); 

        while($row=$stmt->fetch(PDO::FETCH_ASSOC))
        {
            extract($row);
    
            array_push($data, 
                array('image'=>$image,
				'top'=>$top,
				'bottom'=>$bottom
            ));
        }

        header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("img"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
        echo $json;
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