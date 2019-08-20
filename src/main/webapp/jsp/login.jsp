<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="utf-8">
	<title>UGC平台管理系统</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description" content="">
	<meta name="keywords" content="">
	<meta name="author" content="">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
    <link rel="shortcut icon" href="<%=basePath%>images/favicon.ico">
<!-- 引用样式文件开始 -->
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>asserts/xmbl/css/login.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>asserts/dist/css/mui.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>asserts/bootstrap-3.3.4/dist/css/bootstrap.min.css">

<!-- 引用脚本文件开始-->
<script type="text/javascript"
	src="<%=basePath%>asserts/dist/js/mui.min.js"></script>
<script typet="text/javascript"
	src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript"
	src="<%=basePath%>asserts/bootstrap-3.3.4/dist/js/bootstrap.min.js"></script>
</head>
<body>
<img src="" alt="" id="rainyDay" width="100%" height="100%" />
<div class="menu01"></div><script type="text/javascript"></script>

<div id="login-form" class="login-div">
	<img class="logo" style="visibility: hidden;" src="<%=basePath %>images/logo1.png" alt="" />
	<canvas id="particle"></canvas>
	<div class="form-horizontal DengLuBiaoDan bv-form" novalidate="novalidate">
		<!-- <button type="submit" class="bv-hidden-submit" style="display: none; width: 0px; height: 0px;"></button> -->
		<form id="loginForm">
			<div class="form-group has-feedback">
				<div class="col-sm-12"> 
					<input type="text" class="form-control account" placeholder="请输入账号" id="loginId" name="loginId">
					<span class="glyphicon glyphicon-user green"></span>
				</div>
			</div>
			<div class="form-group has-feedback">
				<div class="col-sm-12"> 
					<input type="password" class="form-control password" placeholder="请输入密码" id="password" name="password">
					<span class="glyphicon glyphicon-lock red"></span>
				</div>
			</div>
			<div class="form-group YanZhengMa has-feedback">				  
				<div class="col-sm-12">
					<input class="form-control security-code" placeholder="请输入验证码" name="securityCode" id="securityCode" required> 
				</div>
					<span class="glyphicon glyphicon-pencil yellow"></span>
					<img id="regis-img" title="点击刷新验证码" height="" style="right: 10px" width="27%" src="<%=basePath%>login/securityCode" onclick="refresh()">				
				</div>
			</div>
			<div class="form-group" ><!-- onclick="submit();" -->
				<button type="button" class="btn login-btn flex-grow" onclick="login();">立即登录</button>
			</div>
		</form>
</div> 

<script type="text/javascript">
$(document).ready(function() {
	
});
    function refresh() {  
        var url = "login/securityCode?nocache=" + new Date().getTime();
        $("#regis-img").attr("src",url);  
    }  

    function login(){
   		console.info("开始登录");
    	var loginId = $("#loginId").val();
    	var password = $("#password").val();
    	var securityCode = $("#securityCode").val();
    	$.post("/login/userLogin",{
    		"loginId" : loginId,
    		"password" : password,
    		"veryCode" : securityCode
    	},function(data){
    		console.info(data);
    		if(data.meta.message == "ok"){
    			window.location.href = "<%=basePath%>api/service/to_manager_view"
    		}else{
    			alert(data.meta.message);
    		}
    	})
    }
    
    
</script>


</body>
</html>

