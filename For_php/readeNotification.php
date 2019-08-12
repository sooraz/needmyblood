l<?php
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
if (isset($_GET['to']))  {
 
    $name = $_GET['to'];
    $sql = "SELECT * FROM `notification_blood` WHERE to_donor='".$name."'ORDER BY time DESC";
    // check if row inserted or not
    $result=mysqli_query($conn,$sql);
    $no=1;
    $rows = array();

//retrieve and print every record
while($r = $result->fetch_assoc()){
    // $rows[] = $r; has the same effect, without the superfluous data attribute
    if($r['read'] === "1")
        $rows[] = array('name' => $r["from_request"]
                      ,'time' =>$r["time"]);
    $no++;
}
$rows["tol_users"]=--$no;
$rows["success"] = 1;
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