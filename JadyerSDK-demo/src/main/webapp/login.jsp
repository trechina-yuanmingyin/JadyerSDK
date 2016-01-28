<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8">
<title>登录 - 半步多</title>
<link rel="icon" href="img/logo.ico" type="image/x-icon">
<link rel="stylesheet" href="css/basic.css"/>
<script src="js/jquery-1.11.3.min.js"></script>
<script src="js/pubs.js"></script>
<script src="js/common.js"></script>
<style>
.box_login { width:600px; height:330px; margin:0 auto;}
.box_login .u    { padding:25px 0; border-top:1px solid #3f4954; text-align:center;}
.box_login .m    { border-bottom:1px solid #3f4954;}
.box_login .d    { padding:15px 56px;}
.m li        { clear:both; display:block; width:100%; height:39px; padding:0 0 40px 56px;}
.m li i      { float:left; display:block; width:48px; height:38px; margin-top:1px; background:url(img/icon_login.gif) no-repeat;}
.m li p      { float:left; box-shadow:-1px 0 3px rgba(102,102,102,0.2);}
.m li .te    { width:420px; height:20px; padding:9px 10px; border:none; background:#fff; line-height:20px;}
.m li.l1 i   { background-position:0 0;}
.m li.l2 i   { background-position:0 -38px;}
.m li.l1 .te {  color:#28b779;}
.m li.l2 .te {  color:#ffb848;}
.d .p1         { float:left; padding-top:5px;}
.d .p2         { float:right; text-align:right;}
.d .p1 a       { color:#49afcd; vertical-align:middle;}
.d .p1 a:hover { text-decoration:underline;}
.d .p1 span    { display:inline-block; width:0; height:18px; margin:0 10px; border-left:1px solid #0e1720; border-right:1px solid #3f4954; vertical-align:middle; overflow:hidden;}
.btn        { width:125px; padding:6px 0; text-shadow:0 -1px 0 rgba(51,51,51,0.3);}
.btn:hover,
.btn:active { background:#28b779;}
</style>
<script>
$(function(){
	$("#username").focus();
	$.setBox("div.box_login");
});

/**
 * 设置Box居中
 */
$.setBox = function(o){
	var o = $(o),
		n = 0;
	$(window).resize(function(){
		setOH();
	});
	setOH();
	function setOH(){
		n = Math.floor(($(window).height() - o.height())/2);
		n = n>0 ? n : 0;
		o.css("paddingTop", n);
	}
}

/**
 * 登录
 */
var flag = true; 
function login(){
	var o        = $("#loginSubmit");
	var username = $("#username").val();
	var password = $("#password").val();
	var captcha  = $("#captcha").val();
	if(isEmpty(username)){
		$.promptBox("请输入用户名", "#ffb848");
		$("#username").focus();
		return false;
	}
	if(isEmpty(password)){
		$.promptBox("请输入密码", "#ffb848");
		$("#password").focus();
		return false;
	}
	if(isEmpty(captcha)){
		$.promptBox("请输入验证码", "#ffb848");
		$("#captcha").focus();
		return false;
	}
	if(!flag){
		return false;
	}
	flag = false;
	o.attr("disabled", true).text("登录中...").css({"background":"#28b779", "cursor":"auto"});
	$.get("${pageContext.request.contextPath}/user/login/" + username + "/" + password + "/" + captcha,
		function(data){
			if(1000 == data.code){
				window.location.href = "${pageContext.request.contextPath}/user/info";
			}else{
				o.attr("disabled", false).text("登录").css({"background":"#5bb75b", "cursor":"auto"});
 				$.promptBox(data.message, "#ffb848");
 				flag = true;
			}
		}
	);
}

/**
 * 重载验证码
 */
function reloadCaptcha(){
	document.getElementById("captchaImg").src = "captcha.jsp?time=" +  Math.random();
}
</script>
</head>
<body onkeydown="if(13==event.keyCode)login();">
	<div class="box_login">
		<div class="u"><img src="img/logo.png"/></div>
		<ul class="m">
			<li class="l1"><i></i><p><input id="username" type="text" class="te" placeholder="Username" maxlength="16"/></p></li>
			<li class="l2"><i></i><p><input id="password" type="password" class="te" placeholder="Password" maxlength="16"/></p></li>
			<li class="l2">
				<i></i>
				<p>
					<input id="captcha" name="captcha" type="text" class="te" placeholder="captcha" maxlength="4" style="width:190px; vertical-align:middle;"/>
					<img style="cursor:pointer; vertical-align:middle; height:38px;" id="captchaImg" src="captcha.jsp" onClick="this.src='captcha.jsp?time'+Math.random();">
					<a href="javascript:reloadCaptcha();" style="vertical-align:middle; color:#49afcd;">看不清，换一张！</a>
				</p>
			</li>
		</ul>
		<div class="d">
			<p class="p1"><a href="javascript:alert('暂未开放');" title="注册">注册</a><span></span><a href="javascript:alert('暂未开放');" title="忘记密码">忘记密码</a></p>
			<p class="p2"><button id="loginSubmit" onclick="login();" class="btn trans" type="button">登&nbsp;&nbsp;录</button></p>
			<div class="clear"></div>
		</div>
	</div>
</body>
</html>