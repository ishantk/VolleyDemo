<?php
error_reporting(E_ERROR|E_PARSE);

// Input from Android Client
$name = $_POST['name'];
$phone = $_POST['phone'];
$email = $_POST['email'];


$servername = "";
$username = "";
$password = "";
$dbname = "";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$sql = "INSERT INTO User VALUES (null, '$name','$phone','$email')";

$response = array();

if ($conn->query($sql) === TRUE) {
    $response['success']=1;
 	$response['message']="User Registered Sucessfully";
}else{
 	$response['success']=0;
 	$response['message']="User Registration Failed";
}

$conn->close();

echo json_encode($response)
?>