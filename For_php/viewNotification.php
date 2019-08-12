<?php
include 'initiatedb.php';
// Create connection
$conn = new mysqli($servername, $username, $password, "id8154736_test");
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
// array for JSON response
$response = array(); 
// check for required fields
if (isset($_GET['from'])&&isset($_GET['to'])&&isset($_GET['time']))  {
 
    $name1 = $_GET['from'];
    $name2 = $_GET['to'];
    $time = $_GET['time'];
    $sql = "SELECT * FROM `notification_blood` WHERE from_request='".$name1."'& to_donor='".$name2."'& time='".$time."'";
    // check if row inserted or not
    $result=mysqli_query($conn,$sql);
    $response = array(); 

//retrieve and print every record
while($r = $result->fetch_assoc()){
    $response["success"] = 1;
    $response["message"] = $r["message"];
    ///update read to 1 *******************************************************
    if($r["read"]==="0"){
    $sql = "UPDATE notification_blood SET read='1'
            WHERE from_request='".$name1."'& to_donor='".$name2."'& time='".$time."'";
    mysqli_query($conn,$sql);
    }
}
echo json_encode($rows);
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "invalid arguments";
 
    // echoing JSON response
    echo json_encode($response);
}
$conn->close();
?>