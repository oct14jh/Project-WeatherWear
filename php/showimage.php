<?php


$con=mysqli_connect("localhost","user","1234","testdb"); //Change it if required

// Check connection
if (mysqli_connect_errno())
{
echo "Failed to connect to MySQL: " . mysqli_connect_error();
}


$result = mysqli_query($con,"SELECT image FROM img ");


while($row = mysqli_fetch_array($result))
{
echo '<img src="' . $row['image'] . '" width="200" />';
echo'<br /><br />';  
}



mysqli_close($con);

?>