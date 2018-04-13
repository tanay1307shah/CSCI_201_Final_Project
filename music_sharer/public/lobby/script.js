var music_urls = ["../assets/songs/Made_in_China_Higher_Brothers.mp3",
    "../assets/songs/Fire_Gavin_DeGraw.mp3",
    "../assets/songs/Believer_Imagine_Dragons.mp3"];
var song_index = 0;
var playing = 0;
var num_chats = 2;
var current_chat = 0;
var isProfileShowing = 0;

$(function () {
    // get all user info
    // console.log(Session.get("userID"));
    getAllUserInfo();
    // music control
    $("#play_button").click(function () {
        if (playing == 0) {
            $("#audio")[0].play();
            $("#play_button").attr("src", "../assets/images/stop_button.png");
            playing = 1;
        } else {
            $("#play_button").attr("src", "../assets/images/play_button.png");
            $("#audio")[0].pause();
            playing = 0;
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
});

$("#editUserInfo").click(function () {
    if ($("#usernameInProfile").css('display') === 'none') {
        var that = $("#txtBoxUsername");
        $('#usernameInProfile').text(that.val()).show(); //updated text value and show text
        that.hide(); //hide textbox

        var that = $("#txtBox");
        $('#passwordInProfile').text(that.val()).show(); //updated text value and show text
        that.hide(); //hide textbox

        $("#userTypeRadio").hide();

        //AJAX CALL TO UPDATE USERINFO! MAYBE IGNORE

    } else {
        $("#usernameInProfile").hide(); //hide text
        $("#txtBoxUsername").show(); //show textbox

        $("#passwordInProfile").hide(); //hide text
        $("#txtBox").show(); //show textbox

        $("#userTypeRadio").show(); //show radio buttons
    }
});

$("#logOutButton").click(function () {

});

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
    console.log("here");
    // call ajax to display info
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
    $("#message" + current_chat).append('<div class="text my_text"><span>Joe: </span><br>' + str + '</div>');
}

function scrollToBottom(str) {
    $(str).animate({
        scrollTop: $(str)[0].scrollHeight
    }, 500);
}

function getAllUserInfo() {
    $.get('http://localhost:8080/backend_getUserInfo', function (data) {
        var currentUserJson = JSON.parse(data);
        console.log(currentUserJson);
        populateUserInfo(currentUserJson);
    });
}

function populateUserInfo(currentUserJson) {
    console.log(currentUserJson.avatar);
    console.log(currentUserJson.username);
    console.log(currentUserJson.userEmail);
    $("#userAvatar").attr("src", currentUserJson.avatar);
    $("#username").text(currentUserJson.username);
    $("#userEmail").text(currentUserJson.userEmail);
    populateLists(currentUserJson.friendsList, currentUserJson.hostedLobbies, currentUserJson.favoriteLobbies);
    $("#usernameInProfile").text(currentUserJson.username);
    $("#passwordInProfile").text(currentUserJson.password);
    $("#txtBoxUsername").val(currentUserJson.username);
    $("#txtBox").val(currentUserJson.password);
    if (currentUserJson.platinumUser) {
        $("#userType").text("Platinum");
        $("#userTypePlatinum").prop("checked", true);
    } else {
        $("#userType").text("Standard");
        $("#userTypeStandard").prop("checked", true);
    }
    if(currentUserJson.currentLobby === null){
        $("#curLobbyName").text("NO LOBBY SET!");
        $("#curLobbyPassword").text("NO LOBBY SET!");
        $("#curLobbyNumberOfPeople").text("ZERO!");
        $("#lobbyQuitButton").hide();
        populateUsersInCurLobby(currentUserJson.currentLobby);
    }else{
        $("#curLobbyName").text(currentUserJson.currentLobby.name);
        $("#curLobbyName").text(currentUserJson.currentLobby.password);
        $("#curLobbyName").text(currentUserJson.currentLobby.peopleInLobby.length);
        populateUsersInCurLobby(currentUserJson.currentLobby);
    }


}

function populateLists(friendsList, hostedLobbies, favoriteLobbies) {
    if (friendsList.length === 0) {
        var $div = $("<div>", {class: "name"});
        $div.append("No Friends Yet!");
        $("#friendNames").append($div);
    } else {
        for (var temp in friendsList) {
            var $div = $("<div>", {class: "name"});
            $div.append(temp.username);
            $("#friendNames").append($div);
        }
    }

    if (hostedLobbies.length === 0 && favoriteLobbies.length === 0) {
        var $div = $("<div>", {class: "name"});
        $div.append("No Lobbies Yet!");
        $("#lobbyListNames").append($div);

        var $div = $("<div>", {class: "name"});
        $div.append("Search or create your own!");
        $("#lobbyListNames").append($div);

    }else{
        if(hostedLobbies.length === 0){
            var $div = $("<div>", {class: "name"});
            $div.append("You have no lobbies you host.");
            $("#lobbyListNames").append($div);
        }else{

        }
        if(favoriteLobbies.length === 0){
            var $div = $("<div>", {class: "name"});
            $div.append("You have no favorited lobbies.");
            $("#lobbyListNames").append($div);
        }else{

        }
    }
}

function populateUsersInCurLobby(currentLobby) {
    // for(var temp in currentLobby.peopleInLobby){
    //     var $div = $("<div>", {class: "name"})
    //     $div.append(temp.username);
    //     $("#curLobbyMemberList").append($div);
    // }

    var $div = $("<div>", {class: "name"});
    $div.append("test1");
    $("#curLobbyMemberList").append($div);
    var $div = $("<div>", {class: "name"});
    $div.append("test2");
    $("#curLobbyMemberList").append($div);var $div = $("<div>", {class: "name"});
    $div.append("test1");
    $("#curLobbyMemberList").append($div);
    var $div = $("<div>", {class: "name"});
    $div.append("test2");
    $("#curLobbyMemberList").append($div);var $div = $("<div>", {class: "name"});
    $div.append("test1");
    $("#curLobbyMemberList").append($div);
    var $div = $("<div>", {class: "name"});
    $div.append("test2");
    $("#curLobbyMemberList").append($div);var $div = $("<div>", {class: "name"});
    $div.append("test1");
    $("#curLobbyMemberList").append($div);
    var $div = $("<div>", {class: "name"});
    $div.append("test2");
    $("#curLobbyMemberList").append($div);
}


function modifyInfo() {

}