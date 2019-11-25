<?php
include 'initiatedb.php';
// Create connection
$conn = new mysqli($servername, $username, $password, "id8154736_test");
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$response = array(); 
if (isset($_GET['lat']) && isset($_GET['name']) &&isset($_GET['lng'])) {
 
    $lat = $_GET['lat'];
    $lng = $_GET['lng'];
    $name = $_GET['name'];
    $sql = "UPDATE my_blood SET latitude ='".$lat."' , longitude ='".$lng."' WHERE Name = '".$name."'";
    	if ($conn->query($sql) === TRUE) {
        	$response["success"] = 1;
        	$response["message"] = "successfully updated location";
 
        	// echoing JSON response
        	echo json_encode($response);
    	} else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "update query failed";
 
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