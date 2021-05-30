<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

	$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( ($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit']) || $android)
    {
        $title=$_POST['title'];
		$content=$_POST['content'];
		$userid=$_POST['userid'];
       

        if(!isset($errMSG))
        {
            try{
                $stmt = $con->prepare('INSERT INTO board(title, content, userid) VALUES(:title, :content, :userid)');
                $stmt->bindParam(':title', $title);
				$stmt->bindParam(':content', $content);
				$stmt->bindParam(':userid', $userid);
				

                if($stmt->execute())
                {
                    $successMSG = "새 글 추가완료";
                }
                else
                {
                    $errMSG = "게시글 추가 에러";
                }

            } catch(PDOException $e) {
                die("Database error: 중복체크 해주세요."); 
            }
        }

    }
?>
