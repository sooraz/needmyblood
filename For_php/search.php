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
$sql1 = "SELECT * FROM my_blood WHERE Location_Permanent='";
$sql2 = "SELECT * FROM my_blood WHERE Blood_Group='";
$sql="";
//$sql1 = "SELECT * FROM my_blood WHERE Location_Permanent='".$name."'";
$location_check=false;
$group_check=false;

 if(isset($_GET['location']) && $_GET['location']!=""){
   $location_check=true;
 }
 if(isset($_GET['bgroup']) && $_GET['bgroup']!=""){
   $group_check=true;
 }
 if($location_check&&$group_check){
    $location=$_GET['location'];
    $group=$_GET['bgroup'];
    $sql=$sql1.$location."' && Blood_Group='".$group."'"; 
 }
 else if($location_check){
    $location=$_GET['location'];
    $sql=$sql1.$location."'";
 }
 else if($group_check){
    $group=$_GET['bgroup'];
    $sql=$sql2.$group."'";
 }
// array for JSON response
$response = array(); 
// check for required fields
if ($location_check||$group_check) {
 
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
        
        echo json_encode($response);
    }
}
$conn->close();
?>