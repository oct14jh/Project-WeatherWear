<form action='image_up.php' method='POST' enctype='multipart/form-data'>
< input TYPE=hidden name=mode value=insert>
< table>
< tr> <td>올릴 이미지:</td>
< td><input type='file' name='image'></td></tr>
< tr> <td>제목</td>
< td><input type='text' name='title'></td></tr>
< tr> <td colspan = 2>
<input type = "submit" value="uploaded" name = "upload"/>
< /table>
< /form>