var music_urls=["../assets/songs/Made_in_China_Higher_Brothers.mp3", 
"../assets/songs/Fire_Gavin_DeGraw.mp3", 
"../assets/songs/Believer_Imagine_Dragons.mp3"];
var song_index=0;
var playing = 0;
var num_chats = 2;
var current_chat = 0;

$(function() {
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
});

function isEmpty(str) {
    return (!str || 0 === str.length);
}
function showSeachContent(){
	$("#search_content").css("display","block");
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
}
function searching(){
	console.log("here");
	// call ajax to display info
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