<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 4/12/18
  Time: 7:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>musicBackend</title>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script>
        function getUserInfo() {
            $.get('http://localhost:8080/handleEvent?event=getUserInfo&username=avalante',function(data){
                console.log(data);
                $("#userInfo").text = data;
            });
        }
        
        function getLobbyInfo() {
            $.get('http://localhost:8080/handleEvent?event=getLobbyInfo&lobbyName=firstTestLobby',function(data){
                console.log(data);
                $("#lobbyInfo").text = data;
            });
        }

        function search() {
            $.get('http://localhost:8080/handleEvent?event=search&searchType=user&searchStr=a',function(data){
                console.log(data);
            });
        }
        function getChat() {
            $.get('http://localhost:8080/handleEvent?event=getChat&lobbyName=firstTestLobby', function (data) {
                console.log(data);
            });
        }
    </script>

</head>
<body>
SERVER HAS STARTED! <br>
<input type="button" value="getUserInfo" onclick="getUserInfo()"/><br>
<div id="userInfo"></div><br>
<input type="button" value="getLobbyInfo" onclick="getLobbyInfo()"/><br>
<div id="lobbyInfo"></div>
<input type="button" value="searchUser" onclick="search()"/><br>
<input type="button" value="getChat" onclick="getChat()"/><br>

</body>
</html>
