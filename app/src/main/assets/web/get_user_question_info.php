<?php

$email = $_POST["email"];
$pass = $_POST["pass"];

$servername = "localhost";
$username = "sristior_2k16";
$password = "#jgecSristi11";
$dbname = "sristior_sristi_2k16";
$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("<OUTS><STATUS>0</STATUS><CAUSE>Database Access Problem</CAUSE></OUTS>");
} 

$sql="SELECT * FROM participants WHERE email='$email' AND password='$pass'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    
    $row = $result->fetch_assoc();
    $level = $row['level'];
    $fullname = $row['fname'] . " " . $row['lname'];
    $next_level = ((int)$level) + 1;
    $next_level_question = "";
    $next_level_answer = "";
    
    if(file_exists("../questions/" . $next_level)) {
        
        $file = fopen("../questions/" . $next_level . "/" . "data.txt", "r+");
        while(!feof($file)) {
            $next_level_question = $next_level_question . fgets($file) . "\n";
        }
        fclose($file);
        $file2 = fopen("../questions/" . $next_level . "/" . "ans.txt", "r+");
        $next_level_answer = fgets($file2);
        fclose($file2);
    } else {
        $next_level_question = "NULL";
        $next_level_answer = "NULL";
    }
    
    $outs = "<OUTS><STATUS>1</STATUS><CAUSE></CAUSE><CURRENTLEVEL>$level</CURRENTLEVEL><FULLNAME>$fullname</FULLNAME><NEXTLEVELQUESTION>$next_level_question</NEXTLEVELQUESTION><NEXTLEVELANSWER>$next_level_answer</NEXTLEVELANSWER></OUTS>";
    die($outs);
    
} else {
    die("<OUTS><STATUS>0</STATUS><CAUSE>Password or email wrong</CAUSE></OUTS>");
}















?>
