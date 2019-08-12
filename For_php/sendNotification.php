<?php //Call Function where you want to send Push Notification.
#API access key from Google API's Console
define( 'API_ACCESS_KEY', 'AAAAgKt9fB4:APA91bEfVYKTmXGMjrAmGC3g9MBiX-RNizbM9mmVHYlQ0f3cETOP9mAATr5wtPitVofhg570eW3pn7zAkH9z24mw9QWwbSeWntbDtE_JDVh3xVXtq-x5mIzYIaK7UyACHeGeSMX6rzgc' );
    //$registrationIds = $_GET['id'];

include 'initiatedb.php';
// Create connection
$conn = new mysqli($servername, $username, $password, "id8154736_test");
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
else{
    if (isset($_GET['from_name'])&&isset($_GET['to_name'])&&isset($_GET['message'])) {
     
        $from = $_GET['from_name'];
        $name = $_GET['to_name'];
        $message = $_GET['message'];
        $sql = "SELECT * FROM my_blood WHERE Name='".$name."'";
    // check if row inserted or not
        $result=mysqli_query($conn,$sql);
        while($row = $result->fetch_assoc()) {
            $registrationIds=$row["fcm_key"];
        }
#prep the bundle
        $msg = array
        (
            'body'  => 'From'.$_GET['from_name'],
            'title' => 'Blood Request',
            'icon'  => 'myicon',/*Default Icon*/
            'sound' => 'mySound'/*Default sound*/
        );

        $fields = array
        (
            'to'        => $registrationIds,
            'notification'  => $msg
        );


        $headers = array
        (
            'Authorization: key=' . API_ACCESS_KEY,
            'Content-Type: application/json'
        );

#Send Reponse To FireBase Server    
        $ch = curl_init();
        curl_setopt( $ch,CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send' );
        curl_setopt( $ch,CURLOPT_POST, true );
        curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
        curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
        curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
        curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
        $result = curl_exec($ch );
        curl_close( $ch );

#Echo Result Of FireBase Server
        if($result){
            //date_default_timezone_set('Asia/Kolkata');
            //$date_time = date('m/d/Y h:i:s a', time());
            $sql = "INSERT INTO `notification_blood` (`from`, `to`, `message`, `read`, `time`) VALUES ('".$from."', '".$name."', '".$message."', '0', NOW());";
            if($conn->query($sql) === TRUE){
                //echo "vach";
                $response=array();
                $response["success"]=1;
                $response["serverResponce"]=3;
                echo $response;
            }
            
        }
        else{
            //fail tosend notification
        }
        //echo $result;
    }
}

?>