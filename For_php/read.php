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

    $sql = "SELECT * FROM image_testing";
    // check if row inserted or not
    $result=mysqli_query($conn,$sql);
   while($row = $result->fetch_assoc()) {
        $response["success"] = 1;
        $path = $row["image_path"];
$type = pathinfo($path, PATHINFO_EXTENSION);
$data = file_get_contents($path);

        
        $base64 = 'data:image/' .$type. ';base64,' . base64_encode($data);
        //echo $base64;
        $response["image"] = $base64;
         
        echo json_encode($response);
    }
$conn->close();
?>