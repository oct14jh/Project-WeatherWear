<?php
header('Content-Type: text/html; charset=UTF-8');

$target_Folder = "upload/";

$top = $_POST['top'];
$bottom = $_POST['bottom'];
$uid = $_POST['id'];

$target_Path = $target_Folder.basename( $_FILES['uploadimage']['name'] );

$savepath = $target_Path.basename( $_FILES['uploadimage']['name'] );

    $file_name = $_FILES['uploadimage']['name'];

    if(file_exists('upload/'.$file_name))
{
    echo "That File Already Exisit";
    }
    else
    {

        // Database 
    $con=mysqli_connect("localhost","user","1234","testdb"); //Change it if required

//Check Connection
        if(mysqli_connect_errno())
        {
            echo "Failed to connect to database" .     mysqli_connect_errno();
        }

        $sql = "INSERT INTO img (image, top, bottom)
                    VALUES     ('$target_Folder$file_name','$top', '$bottom') ";

        if (!mysqli_query($con,$sql))
        {
            die('Error: ' . mysqli_error($con));
        }
        echo "1 record added successfully in the database";
        echo '<br />';
        mysqli_close($con);

        // Move the file into UPLOAD folder

        //move_uploaded_file( $_FILES['uploadimage']['tmp_name'],     $target_Path );
		
		if (is_uploaded_file($_FILES['uploadimage']['tmp_name'])) { 
		move_uploaded_file($_FILES['uploadimage']['tmp_name'], 'upload/'.basename($_FILES['uploadimage']['name']));
		}
		
        echo "File Uploaded <br />";
        echo 'File Successfully Uploaded to:&nbsp;' . $target_Path;
        echo '<br />';  
        echo 'File Name:&nbsp;' . $_FILES['uploadimage']['name'];
        echo'<br />';
        echo 'File Type:&nbsp;' . $_FILES['uploadimage']['type'];
        echo'<br />';
        echo 'File Size:&nbsp;' . $_FILES['uploadimage']['size'];

    }
?>

<a href="showimage.php">Show Image</a>