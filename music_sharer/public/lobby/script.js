var music_urls=["../assets/songs/Made_in_China_Higher_Brothers.mp3", 
"../assets/songs/Fire_Gavin_DeGraw.mp3", 
"../assets/songs/Believer_Imagine_Dragons.mp3"];
var song_index=0;
var playing = 0;
var num_chats = 2;
var current_chat = 0;
var isProfileShowing = 0;
var userInfo;

$(function() {
	// get all user info
	getAllUserInfo();
	// music control
	$("#play_button").click(function(){
		if (playing==0) {
			$("#audio")[0].play();
			$("#play_button").attr("src", "../assets/images/stop_button.png");
			playing=1;
		} else {
			$("#play_button").attr("src", "../assets/images/play_button.png");
			$("#audio")[0].pause();
			playing=0;
		}
	});
	$("#previous_button").click(function(){
		song_index--;
		if (song_index<0) {
			song_index = music_urls.length-1;
		}
		$("#audio")[0].src = music_urls[song_index];
		$("#audio")[0].load();
		$("#audio")[0].play();
		$("#play_button").attr("src", "../assets/images/stop_button.png");
		playing = 1;
	});
	$("#next_button").click(function(){
		song_index++;
		if (song_index>=music_urls.length) {
			song_index = 0;
		}
		$("#audio")[0].src = music_urls[song_index];
		$("#audio")[0].load();
		$("#audio")[0].play();
		$("#play_button").attr("src", "../assets/images/stop_button.png");
		playing = 1;
	});
	// chatting manipulation
	$(".text_input input").on('keyup',function(e){
		if (e.keyCode == 13) {
			var str = $(".text_input input").val();
			sendMsg(str);
			scrollToBottom("#message"+current_chat);
			$(".text_input input").val("");
		}
	});
	$("#search_bar .search").focus(function(){
		showSeachContent();
	});
	$("#hide_content_btn").click(function(){
		hideSeachContent();
	});
	$("#hide_profile_btn").click(function(){
		hideProfile();
	});
	$("#side_bar .image").click(function(){
		showProfile();
	});
	$("#lobby_setting .edit").click(function(){
		showEditModal();
	});
	$("#lobby_setting .create").click(function(){
		showCreateModal();
	});
	window.onclick = function(event) {
	    if (event.target == $("#editModal .to_flex")[0]) {
	        hideEditModal();
	    } else if (event.target == $("#createLobbyModal .to_flex")[0]) {
	        hideCreateModal();
	    } else if(event.target == $("#otherUserModal .to_flex")[0]){
	    	hideOtherUserModal();
	    }
	}
	$(".createLobby").click(function(){
		if (!isEmpty($(".lobby_name").val())&&!isEmpty($(".lobby_password").val())) {
			createLobby();
			hideCreateModal();
		}
	});
	$(".saveProfile").click(function(){
		if (!isEmpty($(".username_edit").val())&&!isEmpty($(".password_edit").val())&&!isEmpty($(".profilePhoto_edit").val())) {
			modifyInfo();
			hideEditModal();
		}
	});
	$("#lobby_list .name").click(function(event){
		console.log(event.target);
	});
	$("#friends_list .name").click(function(event){
		var username = event.target.innerHTML;
		showOtherUserModal(username);
	});
});

function isEmpty(str) {
    return (!str || 0 === str.length);
}
function showSeachContent(){
	$("#search_content").css("display","block");
	$("#profile").animate({
		opacity: "0"
	},400);
	$("#lobby_control").animate({
		opacity: "0"
	},400);
	$("#search_content").animate({
		top: '0'
	},500);
}
function hideSeachContent(){
	$("#search_content").animate({
		top: '-100%'
	},500);
	$("#lobby_control").animate({
		opacity: "1"
	},600);
	$("#profile").animate({
		opacity: "1"
	},600);
}
function showProfile(){
	if (isProfileShowing==1) {
		return;
	}
	isProfileShowing = 1;
	$("#search_content").animate({
		top: '-100%'
	},500);
	$("#lobby_control").animate({
		left: "-100%",
		opacity: "0"
	},600);
	$("#profile").animate({
		left: '0',
		opacity: "1"
	},600);
}
function hideProfile(){
	isProfileShowing = 0;
	$("#lobby_control").animate({
		left: "0",
		opacity: "1"
	},600);
	$("#profile").animate({
		left: '-100%',
		opacity: "0"
	},600);
}
function searching(){
	var searchType = "";
	var searchStr = $(".search").val();
	if (isEmpty(searchStr)) {
		return false;
	}
	if($('#radio1').is(':checked')) {
		searchType = "user";
	} else {
		searchType = "lobby";
	}
	// call ajax to get searched info

	$.get('http://192.168.137.125:8080/handleEvent?event=search&searchType='+searchType+'&searchStr='+searchStr,function(data){
		console.log(data);
	});

	//display the results
	var results = ["testuser1","testuser2"];
	for (var i = 0; i < results.length; i++) {
		$("#search_list .names").append('<div class="name">'+results[i]+'</div>');
	}
	return false;
}
function changeChat(i){
	if (i==current_chat) {
		return;
	}
	$(".tabs #tab"+current_chat).removeClass("active");
	$("#message"+current_chat).addClass("hidden");
	$(".tabs #tab"+i).addClass("active");
	$("#message"+i).removeClass("hidden");
	current_chat = i;
}
function sendMsg(str){
	$("#message"+current_chat).append('<div class="text my_text"><span>Joe: </span><br>'+str+'</div>');
}
function scrollToBottom(str){
	$(str).animate({
		scrollTop: $(str)[0].scrollHeight
	}, 500);
}
function getAllUserInfo(){
	// $.get('http://192.168.137.125:8080/backend_getUserInfo',function(data){
	// 	console.log(data);
	// 	userInfo = '{"username":"AlexVal","password":"test1323","userEmail":"alex@gmail.com","friendsList":[],"songLocation":null,"favoriteLobbies":[],"hostedLobbies":[],"platinumUser":false,"chatFilesLocation":null,"avatar":null,"currentLobby":null}';
	// 	console.log(userInfo);
	// });
	userInfo = JSON.parse('{"username":"AlexVal","password":"test1323","userEmail":"alex@gmail.com","friendsList":[{"username":"Joe"},{"username":"Daniel"},{"username":"Tanay"}],"songLocation":null,"favoriteLobbies":["Joe\'s"],"hostedLobbies":["Alex\'s"],"platinumUser":false,"chatFilesLocation":null,"avatar":"https://scontent-lax3-2.xx.fbcdn.net/v/t1.0-1/14095730_1479406308752132_1902501536827789351_n.jpg?_nc_cat=0&oh=0bc43133d027d62ddcae8ffca1529208&oe=5B68892F","currentLobby":null}');
	console.log(userInfo);
	populateUserInfo();
	populateFriendsList();
    populateLobbyList();
    populateCurrentLobby();
    populateProfile();
    populateChats();
}
function populateUserInfo(){
	$("#side_bar img").attr("src", userInfo.avatar);
	$(".profilePhoto_edit").val(userInfo.avatar);
    $("#side_bar .username").text(userInfo.username);
    $("#side_bar .email").text(userInfo.userEmail);
}
function populateFriendsList(){
	if (userInfo.friendsList.length === 0||userInfo.friendsList==null) {
        var $div = $("<div>", {class: "name"});
        $div.append("No Friends Yet!");
        $("#friends_list .names").append($div);
    } else {
        for (var i = 0; i < userInfo.friendsList.length; i++) {
        	var $div = $("<div>", {class: "name"});
            $div.append(userInfo.friendsList[i].username);
            $("#friends_list .names").append($div);
        }
    }
}
function populateLobbyList(){
	if (userInfo.hostedLobbies.length === 0 && userInfo.favoriteLobbies.length === 0) {
        $("#lobby_list .names").append('<div class="name noInfoMsg">No lobbies yet</div>');
    }else{
        if(userInfo.hostedLobbies.length === 0){
        }else{
        	for (var i = 0; i < userInfo.hostedLobbies.length; i++) {
        		$("#lobby_list .names").append('<div class="name">'+userInfo.hostedLobbies[i]+'</div>');
        	}
        }
        if(userInfo.favoriteLobbies.length === 0){
        }else{
			for (var i = 0; i < userInfo.favoriteLobbies.length; i++) {
        		$("#lobby_list .names").append('<div class="name">'+userInfo.favoriteLobbies[i]+'</div>');
        	}
        }
    }
}
function populateCurrentLobby(){
	if(userInfo.currentLobby === null){
    }else{
        $("#lobby_control .username").text(userInfo.currentLobby.name);
        $("#lobby_control .password").text(userInfo.currentLobby.password);
        $("#lobby_control .number_member").text(userInfo.currentLobby.peopleInLobby.length);
        for (var i = 0; i < userInfo.currentLobby.peopleInLobby.length; i++) {
        	$("#lobby_control .member_list").append('<div class="name">'+userInfo.currentLobby.peopleInLobby[i]+'</div>');
        }
    }
}
function populateProfile(){
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
function populateChats(){

}
function showEditModal(){
	$(".username_edit").val(userInfo.username);
	$(".password_edit").val(userInfo.password);
	$(".profilePhoto_edit").val(userInfo.avatar);
	$("#editModal").css("opacity",0);
	$("#editModal").removeClass("hidden");
	$("#editModal").animate({
		opacity:"1"
	},500);
}
function hideEditModal(){
	$("#editModal").addClass("hidden");
}
function createLobby(){
	var lobbyName = $(".lobby_name").val();
	userInfo.hostedLobbies.push(lobbyName);
	$("#lobby_list .names").append('<div class="name">'+lobbyName+'</div>');
	// $.get('http://192.168.137.125:8080/handleEvent?event=createLobby&lobbyName=',function(data){
	// 	console.log(data);
	// });
	switchLobby(lobbyName);
}
function showCreateModal(){
	$(".lobby_name").val("");
	$(".lobby_password").val("");
	$("#createLobbyModal").css("opacity",0);
	$("#createLobbyModal").removeClass("hidden");
	$("#createLobbyModal").animate({
		opacity:"1"
	},500);
}
function hideCreateModal(){
	$("#createLobbyModal").addClass("hidden");
}
function showOtherUserModal(username){
	//get a user's info when clicking a friend in friends list, need the user's info in write back
	// $.get('http://192.168.137.125:8080/handleEvent?event=getUserInfo&username=somename',function(data){
	// 	console.log(data);
	// });
	$(".lobby_name").val("");
	$(".lobby_password").val("");
	$("#createLobbyModal").css("opacity",0);
	$("#createLobbyModal").removeClass("hidden");
	$("#createLobbyModal").animate({
		opacity:"1"
	},500);
}
function switchLobby(lobbyName){

}
function modifyInfo(){
	var new_username = $(".username_edit").val();
	var new_password = $(".password_edit").val();
	var new_profile_photo = $(".profilePhoto_edit").val();
	userInfo.username = new_username;
	userInfo.password = new_password;
	userInfo.avatar = new_profile_photo;
	populateUserInfo();
	populateProfile();
	// send the change to the server
	// $.get('http://192.168.137.125:8080/handleEvent?event=createLobby&lobbyName=',function(data){
	// 	console.log(data);
	// });
}