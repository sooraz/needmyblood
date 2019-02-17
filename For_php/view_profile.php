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
if (isset($_POST['name'])) {
 
    $name = $_POST['name'];
    $sql = "SELECT * FROM my_blood WHERE Name='".$name."'";
    // check if row inserted or not
    $result=mysqli_query($conn,$sql);
   while($row = $result->fetch_assoc()) {
        echo "id: " . $row["Name"]. " - Name: " . $row["Location_Permanent"]. " " . $row["Blood_Group"]. "<br>";
    }
}
$conn->close();
?>