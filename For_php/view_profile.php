<?php
include 'initiatedb.php';
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
if (isset($_GET['name'])) {
 
    $name = $_GET['name'];
    $sql = "SELECT * FROM my_blood WHERE Name='".$name."'";
    // check if row inserted or not
    $result=mysqli_query($conn,$sql);
   while($row = $result->fetch_assoc()) {
        // echo "id: " . $row["Name"]. " - Name: " . $row["Location_Permanent"]. " " . $row["Blood_Group"]. "<br>";
        $response["success"] = 1;
        $response["location"] = $row["Location_Permanent"];
        $response["mail"] = $row["Email"];
        $response["gender"] = $row["Gender"];
        $response["bgroup"] = $row["Blood_Group"];
        $response["location_temp"] = $row["Location_temp"];
        $response["image_path"]=$row["image_path"];
        $response["serverResponce"]=2;
        echo json_encode($response);
    }
}
$conn->close();
?>