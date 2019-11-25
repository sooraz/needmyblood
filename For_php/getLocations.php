<?php
include 'initiatedb.php';
// Create connection
$conn = new mysqli($servername, $username, $password, "id8154736_test");
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$response = array(); 
if (isset($_GET['lat']) && isset($_GET['lng'])) {
 
    $lat = $_GET['lat'];
    $lng = $_GET['lng'];
    $sql = "SELECT
Name,latitude,longitude,Blood_Group
ACOS( SIN( RADIANS( latitude ) ) * SIN( RADIANS(". $lat .") ) + COS( RADIANS( latitude ) )
* COS( RADIANS( ".$lat." )) * COS( RADIANS( longitude ) - RADIANS( ".$lng." )) ) * 6380 AS distance
FROM my_blood
WHERE
ACOS( SIN( RADIANS( latitude ) ) * SIN( RADIANS( ".$lat." ) ) + COS( RADIANS( latitude  ) )
* COS( RADIANS( ".$lat." )) * COS( RADIANS( longitude ) - RADIANS( ".$lng." )) ) * 6380 < 10
ORDER BY distance";
    $result=mysqli_query($conn,$sql);
    $num=1;
    $rows = array();
//retrieve and print every record
while(($r = $result->fetch_assoc()) !== null){
    // $rows[] = $r; has the same effect, without the superfluous data attribute
    $rows[] = array('name' => $r["Name"],'bgroup' =>$r["Blood_Group"],'latitude' =>$r["latitude"],'longitude' => $r["longitude"]);
    $num++;
}
$rows["tol_users"]=--$num;
//$row["serverResponce"]=;//yet to be decided
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