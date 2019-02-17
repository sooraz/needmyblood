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
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array(); 
// check for required fields
if (isset($_POST['name']) && isset($_POST['password'])) {
 
    $name = $_POST['name'];
    $roll = $_POST['password'];
    $sql = "SELECT * FROM my_blood WHERE Name='".$name."' && Password='".$roll."'";
    // check if row inserted or not
    $result=mysqli_query($conn,$sql);
   $num= mysqli_num_rows($result);
    if ($num > 0) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "valid login";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "invalid login";
 
        // echoing JSON response
        echo json_encode($response);
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