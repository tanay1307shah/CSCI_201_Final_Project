var music_urls = ["../assets/songs/Made_in_China_Higher_Brothers.mp3",
    "../assets/songs/Fire_Gavin_DeGraw.mp3",
    "../assets/songs/Believer_Imagine_Dragons.mp3"];
var song_index = 0;
var playing = 0;
var num_chats = 2;
var current_chat = 0;
var isProfileShowing = 0;
var userInfo;
var currentLobby = null;
var ip = "localhost";
var socket;
$(function () {
    // get all user info
    getAllUserInfo();

});

function connectToServer() {
    socket = new WebSocket("ws://" +ip+ ":8080/ws");

    socket.onopen = function (ev) {
        console.log("Connected to Server!");

    };
    socket.onmessage = function (event) {
        var msgData = event.data.split("~");
        console.log("msgData: " + msgData);
        var action = msgData[0];

        var peopleToRecieve = msgData[1];
        console.log(peopleToRecieve);
        var userInts = peopleToRecieve.split(",");
        console.log("User's Id in lobby" + userInts);
        var currentUserId = userInfo.id;
        console.log("current User ID: " + currentUserId);

        var lobbyID = msgData[2];
        console.log("Current Lobby Id: " + lobbyID);

        console.log("LobbyID("+ lobbyID + "):::(" + userInfo.currentLobby + ") Your currentLobby");
        //alert("LobbyID("+ lobbyID + "):::(" + userInfo.currentLobby + ") Your currentLobby");




        if(lobbyID == userInfo.currentLobby) {
            for (var ID in userInts) {
                console.log("CeckID(" + userInts[ID] + "):::(" + currentUserId + ") YourID");
                if (userInts[ID] == currentUserId) {
                    console.log(action);
                    if (action === "PlayMusic") {
                        $("#audio")[0].currentTime = msgData[3];
                        console.log("WE ARE NOW PLAYING MUSIC!!");
                        $("#audio")[0].play();
                        $("#play_button").attr("src", "../assets/images/stop_button.png");
                        playing = 1;
                        return;
                    } else if (action === "StopMusic") {
                        $("#audio")[0].currentTime = msgData[3];
                        console.log("WE ARE HAVE STOPED THE MUSIC!!");
                        $("#play_button").attr("src", "../assets/images/play_button.png");
                        $("#audio")[0].pause();
                        playing = 0;
                        return;
                    } else if (action === "SendMessage") {
                        var msgToSend = msgData[3];
                        console.log(msgToSend);
                        var m = msgToSend.split(":");
                        $("#message" + current_chat).append('<div class="text"><span>'+m[0]+': </span><br>' + m[1] + '</div>');
                    }
                }
            }
        }

    };

    socket.onclose = function (ev) {
        document.getElementById("mychat").innerHTML += "Disconnected!";
    };
}

function bindAllEvent() {
    // music control
    $("#play_button").click(function () {
            if (playing == 0) {
                console.log("------------PLAY BUTTON EVENT------------");
                console.log("current ID (" + userInfo.id + "):::(" + currentLobby.host + ") currentLobbyHost");
                if (currentLobby.host === userInfo.id) {
                    socket.send("MusicControl~PlayMusic~" + currentLobby.name);
                }
            }
            else {
                if (currentLobby.host === userInfo.id) {
                    console.log("------------STOP BUTTON EVENT------------");
                    console.log("current ID (" + userInfo.id + "):::(" + currentLobby.host + ") currentLobbyHost");
                    socket.send("MusicControl~StopMusic~" + currentLobby.name);
                }
            }
    });
    $("#previous_button").click(function () {
        song_index--;
        if (song_index < 0) {
            song_index = music_urls.length - 1;
        }
        $("#audio")[0].src = music_urls[song_index];
        $("#audio")[0].load();
        $("#audio")[0].play();
        $("#play_button").attr("src", "../assets/images/stop_button.png");
        playing = 1;
    });
    $("#next_button").click(function () {
        song_index++;
        if (song_index >= music_urls.length) {
            song_index = 0;
        }
        $("#audio")[0].src = music_urls[song_index];
        $("#audio")[0].load();
        $("#audio")[0].play();
        $("#play_button").attr("src", "../assets/images/stop_button.png");
        playing = 1;
    });
    // chatting manipulation
    $(".text_input input").on('keyup', function (e) {
        if (e.keyCode == 13) {
            var str = $(".text_input input").val();
            sendMsg(str);
            scrollToBottom("#message" + current_chat);
            $(".text_input input").val("");
        }
    });
    $("#search_bar .search").focus(function () {
        showSeachContent();
    });
    $("#hide_content_btn").click(function () {
        hideSeachContent();
    });
    $("#hide_profile_btn").click(function () {
        hideProfile();
    });
    $("#side_bar .image").click(function () {
        showProfile();
    });
    $("#lobby_setting .edit").click(function () {
        showEditModal();
    });
    $("#lobby_setting .create").click(function () {
        showCreateModal();
    });
    window.onclick = function (event) {
        if (event.target == $("#editModal .to_flex")[0]) {
            hideEditModal();
        } else if (event.target == $("#createLobbyModal .to_flex")[0]) {
            hideCreateModal();
        } else if (event.target == $("#otherUserModal .to_flex")[0]) {
            hideOtherUserModal();
        }
    }
    $(".createLobby").click(function () {
        if (!isEmpty($(".lobby_name").val()) && !isEmpty($(".lobby_password").val())) {
            createLobby();
            hideCreateModal();
        }
    });
    $(".saveProfile").click(function () {
        if (!isEmpty($(".username_edit").val()) && !isEmpty($(".password_edit").val()) && !isEmpty($(".profilePhoto_edit").val())) {
            modifyInfo();
            hideEditModal();
        }
    });
    $("#lobby_list .name").click(function (event) {
        var lobbyName = event.target.innerHTML;
        switchLobby(lobbyName);
    });
    $("#friends_list .name").click(function (event) {
        var username = event.target.innerHTML;
        showOtherUserModal(username);
    });
    $("#search_list .name").on("click", function (event) {
        var username = event.target.innerHTML;
        showOtherUserModal(username);
    });
    $(".addFriendBtn").click(function () {
        var username = $("#otherUserModal .username").text();
        addFriend(username);
    });
    $(".log_out").click(function(){
        fantasticEnding();
    });
}

function addFriend(friendName) {
    // send an ajax call to add the friend
    var hostId = userInfo.id;
    userInfo.friendsListStrings.push(friendName);
    $("#friends_list .names").append('<div class="name">' + friendName + '</div>');
    $.get('http://' + ip + ':8080/handleEvent?event=addFriend&friendName=' + friendName + '&hostId=' + hostId, function (data) {
        hideOtherUserModal();
    });
}

function addLobby(_lobbyName) {
    var hostId = userInfo.id;
    // var _lobbyName = $(".lobby_name").val();
    // var lobbyPassword = $(".lobby_password").val();
    userInfo.favoriteLobbiesString.push(_lobbyName);
    $("#lobby_list .names").append('<div class="name">' + _lobbyName + '</div>');
    $.get('http://' + ip + ':8080/handleEvent?event=addLobby&hostId=' + hostId + '&lobbyName=' + _lobbyName, function (data) {
        console.log(data);
        unbindEvent("#search_list .name");
        $("#lobby_list .name").click(function (event) {
            var lobbyName = event.target.innerHTML;
            switchLobby(lobbyName);
        });
    });
}

function unbindEvent(str) {
    $(str).unbind();
}

function isEmpty(str) {
    return (!str || 0 === str.length);
}

function showSeachContent() {
    $("#search_content").css("display", "block");
    $("#profile").animate({
        opacity: "0"
    }, 400);
    $("#lobby_control").animate({
        opacity: "0"
    }, 400);
    $("#search_content").animate({
        top: '0'
    }, 500);
}

function hideSeachContent() {
    $("#search_content").animate({
        top: '-100%'
    }, 500);
    $("#lobby_control").animate({
        opacity: "1"
    }, 600);
    $("#profile").animate({
        opacity: "1"
    }, 600);
}

function showProfile() {
    if (isProfileShowing == 1) {
        return;
    }
    isProfileShowing = 1;
    $("#search_content").animate({
        top: '-100%'
    }, 500);
    $("#lobby_control").animate({
        left: "-100%",
        opacity: "0"
    }, 600);
    $("#profile").animate({
        left: '0',
        opacity: "1"
    }, 600);
}

function hideProfile() {
    isProfileShowing = 0;
    $("#lobby_control").animate({
        left: "0",
        opacity: "1"
    }, 600);
    $("#profile").animate({
        left: '-100%',
        opacity: "0"
    }, 600);
}

function searching() {
    $("#search_content .names").empty();
    var searchType = "";
    var searchStr = $(".search").val();
    if (isEmpty(searchStr)) {
        return false;
    }
    if ($('#radio1').is(':checked')) {
        searchType = "user";
    } else {
        searchType = "lobby";
    }
    // call ajax to get searched info

    $.get('http://' + ip + ':8080/handleEvent?event=search&searchType=' + searchType + '&searchStr=' + searchStr, function (data) {
        var results = data.split(',');
        for (var i = 0; i < results.length; i++) {
            var $div = $("<div>", {class: "name"});
            $div.append(results[i]);
            $("#search_list .names").append($div);
        }
        unbindEvent("#search_list .name");
        if (searchType == "user") {
            $("#search_list .names").click(function (event) {
                var username = event.target.innerHTML;
                showOtherUserModal(username);
            });
        } else {
            console.log(searchType);
            $("#search_list .names").click(function (event) {
                var lobbyName = event.target.innerHTML;
                hideSeachContent();
                addLobby(lobbyName);
                switchLobby(lobbyName);
            });
        }
    });
    //display the results
    // var results = ["testuser1","testuser2"];

    return false;
}

function changeChat(i) {
    if (i == current_chat) {
        return;
    }
    $(".tabs #tab" + current_chat).removeClass("active");
    $("#message" + current_chat).addClass("hidden");
    $(".tabs #tab" + i).addClass("active");
    $("#message" + i).removeClass("hidden");
    current_chat = i;
}

function sendMsg(str) {
    socket.send("Message~Send~" + currentLobby.name + "~" + userInfo.username +"~"+ str);
    $("#message" + current_chat).append('<div class="text my_text"><span>'+userInfo.username+': </span><br>' + str + '</div>');
}

function scrollToBottom(str) {
    $(str).animate({
        scrollTop: $(str)[0].scrollHeight
    }, 500);
}

function getAllUserInfo() {
    $.get('http://' + ip + ':8080/backend_getUserInfo', function (data) {
        userInfo = JSON.parse(data);
        console.log(userInfo);

        populateUserInfo();
        populateFriendsList();
        populateLobbyList();
        // populateCurrentLobby();
        populateProfile();
        bindAllEvent();
        // userInfo = '{"username":"AlexVal","password":"test1323","userEmail":"alex@gmail.com","friendsList":[],"songLocation":null,"favoriteLobbiesStrings":[],"hostedLobbies":[],"platinumUser":false,"chatFilesLocation":null,"imgLocation":null,"currentLobby":null}';
        // console.log(userInfo);
    });
    // userInfo = JSON.parse('{"username":"AlexVal","password":"test1323","userEmail":"alex@gmail.com","friendsList":[{"username":"Joe"},{"username":"Daniel"},{"username":"Tanay"}],"songLocation":null,"favoriteLobbiesStrings":["Joe\'s"],"hostedLobbies":["Alex\'s"],"platinumUser":false,"chatFilesLocation":null,"imgLocation":"https://scontent-lax3-2.xx.fbcdn.net/v/t1.0-1/14095730_1479406308752132_1902501536827789351_n.jpg?_nc_cat=0&oh=0bc43133d027d62ddcae8ffca1529208&oe=5B68892F","currentLobby":null}');
    // userInfo = JSON.parse('{"username":"avalante","password":"swim","friendsList":[{"username":"Joe"},{"username":"Daniel"},{"username":"Tanay"}],"imgLocation":"https://scontent-lax3-2.xx.fbcdn.net/v/t1.0-1/14095730_1479406308752132_1902501536827789351_n.jpg?_nc_cat=0&oh=0bc43133d027d62ddcae8ffca1529208&oe=5B68892F","favoriteLobbiesStrings":["Joe\'s","Daniel\'s","Tanay\'s"],"hostedLobbies":["Alex\'s"],"platinumUser":false,"id":2}');
    // console.log(userInfo);
    // populateUserInfo();
    // populateFriendsList();
    //    populateLobbyList();
    //    // populateCurrentLobby();
    //    populateProfile();
    //    populateChats();
}

function populateUserInfo() {
    if (userInfo.imgLocation == null||userInfo.imgLocation=="") {
        userInfo.imgLocation = "../assets/images/no_image.jpg";
    }
    $("#side_bar img").attr("src", userInfo.imgLocation);
    $(".profilePhoto_edit").val(userInfo.imgLocation);
    $("#side_bar .username").text(userInfo.username);
    // $("#side_bar .email").text(userInfo.userEmail);
}

function populateFriendsList() {
    if (userInfo.friendsListStrings == null || userInfo.friendsListStrings.length === 0) {
        var $div = $("<div>", {class: "name"});
        $div.append("No Friends Yet!");
        $("#friends_list .names").append($div);
    } else {
        for (var i = 0; i < userInfo.friendsListStrings.length; i++) {
            var $div = $("<div>", {class: "name"});
            $div.append(userInfo.friendsListStrings[i]);
            $("#friends_list .names").append($div);
        }
    }
}

function populateLobbyList() {
    if ((userInfo.hostedLobbies == null || userInfo.hostedLobbies.length === 0)
        && (userInfo.favoriteLobbiesString == null || userInfo.favoriteLobbiesString.length === 0)) {
    } else {
        if (userInfo.hostedLobbies == null || userInfo.hostedLobbies.length === 0) {
        } else {
            for (var i = 0; i < userInfo.hostedLobbies.length; i++) {
                $("#lobby_list .names").append('<div class="name">' + userInfo.hostedLobbies[i] + '</div>');
            }
        }
        if (userInfo.favoriteLobbiesString.length === 0) {
        } else {
            for (var i = 0; i < userInfo.favoriteLobbiesString.length; i++) {
                $("#lobby_list .names").append('<div class="name">' + userInfo.favoriteLobbiesString[i] + '</div>');
            }
        }
    }
}

function populateCurrentLobby() {
    $("#lobby_control .member_list").empty();
    $("#lobby_control .username span").text(currentLobby.name);
    $("#lobby_control .password span").text(currentLobby.password);
    if (currentLobby.peopleInLobbyString != null) {
        // $("#lobby_control .number_member span").text(currentLobby.peopleInLobbyString.length);
        for (var i = 0; i < currentLobby.peopleInLobbyString.length; i++) {
            $("#lobby_control .member_list").append('<div class="name">' + currentLobby.peopleInLobbyString[i] + '</div>');
        }
    }
}

function populateProfile() {
    $("#profile .username span").text(userInfo.username);
    $("#profile .password span").text(userInfo.password);
    $(".username_edit").val(userInfo.username);
    $(".password_edit").val(userInfo.password);
    if (userInfo.platinumUser) {
        $("#profile .membership span").text("Platinum");
    } else {
        $("#profile .membership span").text("Standard");
    }
}

function populateChats() {
    var lobbyName = currentLobby.name;
    $.get('http://' + ip + ':8080/handleEvent?event=getChat&lobbyName=' + lobbyName, function (data) {
        // var data = "Joe:hello,Alex:hello2,Daniel:Hello3,";
        var chats = data.split(",");
        // console.log(chats.length);
        for (var i = 0; i < chats.length-1; i++) {
            var msg = chats[i].split(":");
            if (msg[0]=="Joe") {
                $("#message" + current_chat).append('<div class="text my_text"><span>'+msg[0]+': </span><br>' + msg[1] + '</div>');
            } else {
                $("#message" + current_chat).append('<div class="text"><span>'+msg[0]+': </span><br>' + msg[1] + '</div>');
            }
        }
    });
}

function showEditModal() {
    $(".username_edit").val(userInfo.username);
    $(".password_edit").val(userInfo.password);
    $(".profilePhoto_edit").val(userInfo.imgLocation);
    $("#editModal").css("opacity", 0);
    $("#editModal").removeClass("hidden");
    $("#editModal").animate({
        opacity: "1"
    }, 500);
}

function hideEditModal() {
    $("#editModal").addClass("hidden");
}

function createLobby() {
    var hostId = userInfo.id;
    var _lobbyName = $(".lobby_name").val();
    var lobbyPassword = $(".lobby_password").val();
    userInfo.favoriteLobbiesString.push(_lobbyName);
    $("#lobby_list .names").append('<div class="name">' + _lobbyName + '</div>');
    $.get('http://' + ip + ':8080/handleEvent?event=createLobby&hostId=' + hostId + '&lobbyName=' + _lobbyName + '&lobbyPassword=' + lobbyPassword, function (data) {
        console.log(data);
        unbindEvent("#search_list .name");
        $("#lobby_list .name").click(function (event) {
            var lobbyName = event.target.innerHTML;
            switchLobby(lobbyName);
        });
        switchLobby(_lobbyName);
    });
    // unbindEvent("#search_list .name");
    // $("#lobby_list .name").click(function(event){
    //  var lobbyName = event.target.innerHTML;
    //  switchLobby(lobbyName);
    // });
    // switchLobby(lobbyName);
}

function showCreateModal() {
    $(".lobby_name").val("");
    $(".lobby_password").val("");
    $("#createLobbyModal").css("opacity", 0);
    $("#createLobbyModal").removeClass("hidden");
    $("#createLobbyModal").animate({
        opacity: "1"
    }, 500);
}

function hideCreateModal() {
    $("#createLobbyModal").addClass("hidden");
}

function showOtherUserModal(username) {
    // check if the user is a friend
    var isFriend = 0;
    for (var i = 0; i < userInfo.friendsListStrings.length; i++) {
        if (userInfo.friendsListStrings[i]==username) {
            isFriend = 1;
        }
        
    }
    if (isFriend==1) {
        $(".addFriendBtn").css("display","hidden");
    } else {
        $(".addFriendBtn").css("display","block");
    }
    
    // get a user's info when clicking a friend in friends list, need the user's info in write back
    $.get('http://' + ip + ':8080/handleEvent?event=getUserInfo&username=' + username, function (data) {
        var that_user = JSON.parse(data);
        console.log(that_user);
        // populate the modal
        if (that_user.imgLocation == null||userInfo.imgLocation=="") {
            that_user.imgLocation = "../assets/images/no_image.jpg";
        }
        $("#otherUserModal img").attr("src", that_user.imgLocation);
        $("#otherUserModal .username").text(that_user.username);
        // $("#otherUserModal .email").text(that_user.userEmail);
        $("#other_friends_list .names").empty();
        if (that_user.friendsListStrings == null || that_user.friendsListStrings.length === 0) {
            // var $div = $("<div>", {class: "name"});
            // $div.append("No Friends Yet!");
            // $("#other_friends_list .names").append($div);
        } else {
            for (var i = 0; i < that_user.friendsListStrings.length; i++) {
                var $div = $("<div>", {class: "name"});
                $div.append(that_user.friendsListStrings[i]);
                $("#other_friends_list .names").append($div);
            }
        }
        $("#other_lobby_list .names").empty();
        if ((that_user.hostedLobbies == null || that_user.hostedLobbies.length === 0)
            && (that_user.favoriteLobbiesString == null || that_user.favoriteLobbiesString.length === 0)) {
        } else {
            if (that_user.hostedLobbies == null || that_user.hostedLobbies.length === 0) {
            }
            else {
                for (var i = 0; i < that_user.hostedLobbies.length; i++) {
                    $("#other_lobby_list .names").append('<div class="name">' + that_user.hostedLobbies[i] + '</div>');
                }
            }
            if (that_user.favoriteLobbiesString == null || that_user.favoriteLobbiesString.length === 0) {
            } else {
                for (var i = 0; i < that_user.favoriteLobbiesString.length; i++) {
                    $("#other_lobby_list .names").append('<div class="name">' + that_user.favoriteLobbiesString[i] + '</div>');
                }
            }
        }

        $("#otherUserModal").css("opacity", 0);
        $("#otherUserModal").removeClass("hidden");
        $("#otherUserModal").animate({
            opacity: "1"
        }, 500);
    });
    // var that_user = JSON.parse('{"username":"Shi Zeng","password":"zeng","friendsList":[{"username":"Alex"},{"username":"Daniel"},{"username":"Tanay"}],'+
    //  '"imgLocation":null,"favoriteLobbiesStrings":["Alex\'s","Daniel\'s","Tanay\'s"],"hostedLobbies":["Joe\'s"],"platinumUser":false,"id":1}');
    // // populate the modal
    // if (that_user.imgLocation==null) {
    //  that_user.imgLocation = "../assets/images/no_image.jpg";
    // }
    // $("#otherUserModal img").attr("src", that_user.imgLocation);
    //    $("#otherUserModal .username").text(that_user.username);
    //    // $("#otherUserModal .email").text(that_user.userEmail);
    //    $("#other_friends_list .names").empty();
    //    if (that_user.friendsList==null||that_user.friendsList.length === 0) {
    //        var $div = $("<div>", {class: "name"});
    //        $div.append("No Friends Yet!");
    //        $("#other_friends_list .names").append($div);
    //    } else {
    //        for (var i = 0; i < that_user.friendsList.length; i++) {
    //          var $div = $("<div>", {class: "name"});
    //            $div.append(that_user.friendsList[i].username);
    //            $("#other_friends_list .names").append($div);
    //        }
    //    }
    //    $("#other_lobby_list .names").empty();
    //    if ((that_user.hostedLobbies==null||that_user.hostedLobbies.length === 0)
    //  && (that_user.favoriteLobbiesStrings==null||that_user.favoriteLobbiesStrings.length === 0)) {
    //    } else {
    //        if(that_user.hostedLobbies.length === 0){
    //        }else{
    //          for (var i = 0; i < that_user.hostedLobbies.length; i++) {
    //              $("#other_lobby_list .names").append('<div class="name">'+that_user.hostedLobbies[i]+'</div>');
    //          }
    //        }
    //        if(that_user.favoriteLobbiesStrings.length === 0){
    //        }else{
    //      for (var i = 0; i < that_user.favoriteLobbiesStrings.length; i++) {
    //              $("#other_lobby_list .names").append('<div class="name">'+that_user.favoriteLobbiesStrings[i]+'</div>');
    //          }
    //        }
    //    }


    // $("#otherUserModal").css("opacity",0);
    // $("#otherUserModal").removeClass("hidden");
    // $("#otherUserModal").animate({
    //  opacity:"1"
    // },500);
}

function hideOtherUserModal() {
    $("#otherUserModal").addClass("hidden");
}

function switchLobby(lobbyName) {
    console.log("calling ajax to get lobby info");
    // get a lobby's info when clicking a friend in friends list, need the lobby's info in write back
    $.get('http://' + ip + ':8080/handleEvent?event=getLobbyInfo&lobbyName=' + lobbyName, function (data) {
        // console.log(data);
        // data = '{"name":"firstTestLobby","password":"test","host":2,"peopleInLobbyString":[1,2,4,3],"publicBool":true,"ints":[]}';
        // console.log(data);
        currentLobby = JSON.parse(data);
        console.log(currentLobby);
        userInfo.currentLobby = currentLobby.lobbyID;
        console.log("In current lobby (ID): " + userInfo.currentLobby);
        ////
        //if user is host show controle buttons here
        //
        ////
        if (currentLobby === null) {
            currentLobby = JSON.parse('{"name":"no lobby selected","password":"","host":0,"peopleInLobbyString":[],"ints":[],"publicBool":true}');
        }
        // currentLobby.name = lobbyName;
        // currentLobby.password = data.password;
        // currentLobby.peopleInLobbyString = data.peopleInLobbyString;
        populateCurrentLobby();
        populateChats();
    });
    // var data = JSON.parse('{"name":"firstTestLobby","password":"test","host":2,"peopleInLobbyString":["Joe","Joe","Joe"],"ints":[],"publicBool":true}');
    // console.log(data);
    // if(currentLobby === null){
    //  currentLobby = JSON.parse('{"name":"no lobby selected","password":"","host":1,"peopleInLobbyString":[],"ints":[],"publicBool":true}');
    //    }
    // currentLobby.name = lobbyName;
    // currentLobby.password = data.password;
    // currentLobby.peopleInLobbyString = data.peopleInLobbyString;
    // populateCurrentLobby();
}

function modifyInfo() {
    var new_username = $(".username_edit").val();
    var new_password = $(".password_edit").val();
    var new_profile_photo = $(".profilePhoto_edit").val();
    userInfo.username = new_username;
    userInfo.password = new_password;
    userInfo.imgLocation = new_profile_photo;
    populateUserInfo();
    populateProfile();
    // send the change to the server
    var hostId = userInfo.id;
    $.get('http://'+ ip +':8080/handleEvent?event=editCurrentUser&hostId=' + hostId +
        '&newUsername=' + new_username +
        '&newPassword=' + new_password +
        '&newImgLocation=' + new_profile_photo, function (data) {
        console.log(data);
    });
}

function fantasticEnding(){
    $(".container").animate({
        opacity:"0"
    },1000, function() {
        $(".container").addClass("hidden");
    });
    $("#thankYouEnding").css("opacity","0");
    $("#thankYouEnding").removeClass("hidden").animate({
        opacity: "1"
    },3000, function() {
        $("#thankYouEnding").animate({
            opacity:"0"
        },2000, function() {
            $("#thankYouEnding").addClass("hidden");
            var height = 0-($("#fantasticEnding").height()+200);
            console.log(height);
            $("#fantasticEnding").removeClass("hidden").animate({
                top: height
            }, 30000, "linear", function() {
                window.location.replace("../index.html");
            });
        });
    });
    
}



