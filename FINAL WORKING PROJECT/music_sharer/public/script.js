var ip = "192.168.137.187";
var typed = new Typed("#typed", {
  strings: ["anywhere", "anytime","with anyone"],
  smartBackspace: false, // Default value
  // startDelay: 1000,
  typeSpeed: 60,
  backSpeed: 60,
  // backDelay: 1000,
  loop: true,
  showCursor: false,
});
function isEmpty(str) {
    return (!str || 0 === str.length);
}
$(function(){
	$("#container1 .login").click(function(){
		
		var ele = $("#intro span").offset();
		$("#title").css("left", ele.left);
		$("#title").css("top", ele.top+13.5);
		$("#title").css("display", "block");
		$("#container1").fadeOut(800);
		$("#title").delay(500).animate({
			left: "50px",
			top: "50px"
		},{duration:700});
		$("#container3").css("opacity","0");
		$("#container3").css("display","flex");
		$("#container3").animate({opacity: 1},{duration:2000,});
	});
	$("#container1 .signup").click(function(){
		var ele = $("#intro span").offset();
		$("#title").css("left", ele.left);
		$("#title").css("top", ele.top+13.5);
		$("#title").css("display", "block");
		$("#container1").fadeOut(800);
		$("#title").delay(500).animate({
			left: "50px",
			top: "50px"
		},{duration:700});
		$("#container2").css("opacity","0");
		$("#container2").css("display","flex");
		$("#container2").animate({opacity: 1},{duration:2000,});
	});
	$("#title").click(function(){
		window.location.replace("index.html");
	});
	$("#container2 .signup").click(function(){
		// sign up validation
		$(".error").addClass("hidden");
		const imgLocaitn = $("#container2 .profilePhoto").val();
		const username = $("#container2 .username").val();
		const userEmail = $("#container2 .userEmail").val();
		const password = $("#container2 .password").val();
		if(isEmpty(username) || isEmpty(password)||isEmpty(userEmail)) {
			$("#container2 .error").removeClass("hidden");
			return;
		}
		$.get('http://'+ip+':8080/backend_newUser?userName=' + username + '&userEmail='+userEmail+'&password=' + password + "&imgLocation=" + imgLocaitn,function(data){
			console.log(data);
			if(data === 'WRONG') {
				$(".error").removeClass("hidden");
			}
			else {
				window.location.replace("lobby/index.html");
			}
		});
	});
	$("#container3 .login").click(function(){
		// login validation
		$(".error").addClass("hidden");
		const userEmail = $("#container3 .userEmail").val();
		const password = $("#container3 .password").val();
		if(isEmpty(password)||isEmpty(userEmail)) {
			$("#container3 .error").removeClass("hidden");
			return;
		}
		$.get('http://'+ip+':8080/backend_logIn?'+'userEmail='+userEmail+'&password=' + password,function(data){
			console.log(data);
			if(data === 'WRONG') {
				$(".error").removeClass("hidden");
			}
			else {
				window.location.replace("lobby/index.html");
			}
		});
		//window.location.href = "lobby/index.html";
	});
	$(".guest").click(function(){
		$.get('http://'+ip+':8080/backend_logIn?'+'userEmail='+'guest'+'&password=' + 'guest',function(data){
			console.log(data);
			if(data === 'WRONG') {
				$(".error").removeClass("hidden");
			}
			else {
				window.location.replace("guest/index.html");
			}
		});
		// window.location.replace("guest/index.html");
	});
});