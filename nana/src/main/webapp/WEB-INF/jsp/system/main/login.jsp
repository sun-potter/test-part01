<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%  
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%> 
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<%=basePath%>" >
<meta charset="UTF-8">
<title>系统登录</title>
<script type="text/javascript"  src="static/jQuery/jquery-1.8.3.min.js"></script>
<script type="text/javascript"  src="static/camera/scripts/jquery.easing.1.3.js"></script>
<script type="text/javascript"  src="static/camera/scripts/camera.js"></script>
<script type="text/javascript"  src="static/tooltips/js/jquery-tips.js"></script>
<link rel="stylesheet" href="static/camera/css/camera.css" type="text/css" media="all">
<link rel="stylesheet" href="static/bootstrap-3.3.7-dist/css/bootstrap.css" type="text/css">
<link rel="shortcut icon" href="static/images/favicon.ico" type="image/x-icon" />
<style>
html,body {
	height: 100%;
	margin: 0;
	padding: 0;
}
a {
	color: #09f;
}
a:hover {
	text-decoration: none;
}
#back_to_camera {
	background: rgba(255,255,255,.9);
	clear: both;
	display: block;
	height: 40px;
	line-height: 40px;
	padding: 20px;
	position: relative;
	z-index: 1;
}
.fluid_container {
	bottom: 0;
	height: 100%;
	left: 0;
	position: fixed;
	right: 0;
	top: 0;
	z-index: 0;
}
#camera_wrap {
	bottom: 0;
	height: 100%;
	left: 0;
	margin-bottom: 0!important;
	position: fixed;
	right: 0;
	top: 0;
}
.camera_bar {
	z-index: 2;
}
.camera_thumbs {
	margin-top: -100px;
	position: relative;
	z-index: 1;
}
.camera_thumbs_cont {
	border-radius: 0;
	-moz-border-radius: 0;
	-webkit-border-radius: 0;
}
.camera_overlayer {
	opacity: .1;
}
</style>
</head>
<body>
<div style="width:30%;height:290px;text-align: center;margin: 0 auto;position: absolute;left:35%;top:25%;z-index:1;background-color:rgba(0,0,0,0.4); ">
	<form action="" method="post" id="loginForm" class="form-horizontal">
		<div class="control-group">
			<h4 style="font:bold 28px sans-serif;color:white;">管理系统</h4>
		</div>
		<div class="control-group" style="margin:20px 5px;padding:12px;">
			<label for="loginname" style="color:white;padding:0;font-size:16px;line-height:32px;" class="col-sm-2 control-label">账号：</label>
			<div class="col-sm-10">
				<input  class="form-control" type="text" name="loginname" id="loginname" value="" placeholder="请输入用户名" data-toggle="tooltip"/>
			</div>
		</div>
		<div class="control-group" style="margin: 20px 5px;padding:12px;" >
			<label for="password" style="color:white;padding:0;font-size:16px;line-height:32px;"  class="col-sm-2 control-label">密码：</label>
			<div class="col-sm-10">
				<input class="form-control" type="password" name="password" id="password" placeholder="请输入密码" value=""/>
			</div>
		</div>
		<div class="control-group" style="margin-top: 60px">
			<div style="padding:0 20px;" >
				<label style="color:white;font-size:14px;line-height:32px;" class="control-label">验证码：</label>
				<div style="display: inline-block;width:70px;">
					<input type="text" name="randomCode" id="code" class="login_code form-control" style="height:27px;"/>
				</div>
				<img style="height:26px;position: relative;bottom:1px;" id="codeImg" alt="点击更换" 
					title="点击更换" src="" />
				<a href="javascript:clean();" class="btn btn-success" style="margin-left: 20px;">清空</a>
				<a href="javascript:severCheck();" class="flip-link btn btn-info" id="to-recover">登录</a>
			</div>
		</div>
		<div class="control-group">
			<div style="padding-top: 30px;">
				<label style="color:white">Copyright © 程序员：Simon 2016</label>
			</div>
		</div>
	</form>
</div>
<div class="fluid_container">
	<div class="camera_wrap" id="camera_wrap">
	   <div data-thumb="static/camera/images/slides/thumbs/bridge.jpg" data-src="static/camera/images/slides/bridge.jpg"></div>
	   <div data-thumb="static/camera/images/slides/thumbs/leaf.jpg" data-src="static/camera/images/slides/leaf.jpg"></div>
	   <div data-thumb="static/camera/images/slides/thumbs/road.jpg" data-src="static/camera/images/slides/road.jpg"></div>
	   <div data-thumb="static/camera/images/slides/thumbs/sea.jpg" data-src="static/camera/images/slides/sea.jpg"></div>
	   <div data-thumb="static/camera/images/slides/thumbs/shelter.jpg" data-src="static/camera/images/slides/shelter.jpg"></div>
	   <div data-thumb="static/camera/images/slides/thumbs/tree.jpg" data-src="static/camera/images/slides/tree.jpg"></div>
	</div>
</div>
<script type="text/javascript">
$(function() {
	$('#camera_wrap').camera({
		height: 'auto',
		loader: 'bar',
		playPause:false,
		navigation:false,
		pagination: false,
		thumbnails: true,
		hover: false,
		opacityOnGrid: false,
		imagePath: "static/images/camera/"
	});
	changeCode();
	$("#codeImg").bind("click", changeCode);
});
function changeCode() {
	$("#codeImg").attr("src", "<%=basePath%>code.do?t=" + genTimestamp());
}

function genTimestamp() {
	var time = new Date();
	return time.getTime();
}
//客户端校验
function check() {
	if($("#loginname").val()==""){
		return false;
	}
	if($("#password").val()==""){
		return false;
	}
	if($("#code").val()==""){
		return false;
	}
	return true;
}
function severCheck(){
	if(check()){
		$.ajax({
			url:"login.do",
			data:$("#loginForm").serialize(),
			type:"post",
			dataType:"text",
			success:function(data){
				if(data=="loginFailure"){
					alert("账号或密码错误！");
				}else if(data=="validateCodeError"){
					alert("验证码输入错误！");
				}else if(data=="regain"){
					severCheck();
				}else if(data=="loginSuccess"){
					window.location.href = "index.do";
				}
			},
		});
	}
}
function clean(){
	$("#loginname").val("");
	$("#password").val("");
	$("#code").val("");
}
</script>
</body>
</html>