<?php
 $servername = "localhost";
$username = "id8154736_root";
$password = "123456789";
// Create connection
$conn = new mysqli($servername, $username, $password, "id8154736_test");
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$response = array(); 
if (isset($_POST['name']) && isset($_POST['password'])&& isset($_POST['mail'])&& isset($_POST['gender'])&& isset($_POST['bgroup'])&& isset($_POST['location'])) {
 
    $name = $_POST['name'];
    $pass = $_POST['password'];
    $mail = $_POST['mail'];
    $gender = $_POST['gender'];
    $bgroup = $_POST['bgroup'];
    $loc = $_POST['location'];

    $image = $_POST['image'];
    $decodedImage = base64_decode("$image");
 
    $return = file_put_contents("uploadedFiles/".$name.".JPG", $decodedImage);
    if($return!=false){
        $img_path="uploadedFiles/".$name.".JPG";
    }
    else{
        $img_path="uploadedFiles/default.JPG";   
    }

    $sql = "SELECT * FROM my_blood WHERE Name='".$name."'";
    $result=mysqli_query($conn,$sql);
    $num= mysqli_num_rows($result);
    if($num>0){
    	$response["success"] = 0;
    	$response["message"] = "already registered";

    	// echoing JSON response
        echo json_encode($response);
    }    
    else{
    $sql = "INSERT INTO my_blood (Name,Password,Location_Permanent,Blood_Group,Email,Gender,Location_temp,fcm_key,image_path) VALUES ('".$name."','".$pass."','".$loc."','".$bgroup."','".$mail."','".$gender."','".NULL."','".NULL."','".$img_path."')";
    	if ($conn->query($sql) === TRUE) {
        	$response["success"] = 1;
        	$response["message"] = "successfully registered";
 
        	// echoing JSON response
        	echo json_encode($response);
    	} else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "invalid registration";
 
        // echoing JSON response
        echo json_encode($response);
    	}
	}
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "invalid arguments";
 
    // echoing JSON response
    echo json_encode($response);
}
$conn->close();
?>