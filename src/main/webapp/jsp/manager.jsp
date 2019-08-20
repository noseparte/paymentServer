<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
	href="<%=basePath%>asserts/xmbl/css/manager.css">
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
	<div class="container">
		<div class="mui-card">
			<!--页眉，放置标题-->
			<div class="mui-card-header mui-card-media">
				<img src="<%=basePath%>images/LOGO.png" />
				<div class="mui-media-body">
					菠萝游戏
					<p>发表于 2018-09-20 11:39</p>
				</div>
			</div>
			<!--内容区-->
			<div class="mui-card-content">
				<ul class="mui-table-view">
					<li class="mui-table-view-cell mui-collapse"><a
						class="mui-navigate-right" href="#">支付</a>
						<div class="mui-collapse-content">
							<ul class="mui-table-view">
								<li class="mui-table-view-cell">充值recharge<span
									class="mui-badge mui-badge-primary">11</span>
									<div class="mui-switch" id="recharge-switch"
										value="${pd.rechargeState}" onclick="switchService($(this))">
										<div class="mui-switch-handle"></div>
									</div>
								</li>
								<li class="mui-table-view-cell">提现transfer<span
									class="mui-badge mui-badge-success">22</span>
									<div class="mui-switch" id="transfer-switch"
										value="${pd.transferState}" onclick="switchService($(this))">
										<div class="mui-switch-handle"></div>
									</div>
								</li>
							</ul>
						</div></li>
				</ul>

			</div>
			<!--页脚，放置补充信息或支持的操作-->
			<div class="mui-card-footer">小米菠萝科技有限公司 | BeiJing Xiaomi Boluo
				Network Technology Co. Ltd. &nbsp;Copyright © 2016 - 2018 &nbsp;</div>
		</div>

	</div>

</body>

<script type="text/javascript">
	$(document).ready(function() {
		initSwitch();
		mui.init({
			gestureConfig : {
				tap : true, //默认为true
				doubletap : true, //默认为false
				longtap : true, //默认为false
				swipe : true, //默认为true
				drag : true, //默认为true
				hold : false,//默认为false，不监听
				release : false
			//默认为false，不监听
			}
		});
	});

	function initSwitch(){
		var className = (${pd.rechargeState} == 0) ? "mui-switch" : "mui-switch mui-active";
		$("#recharge-switch").attr("class",className);
		var className = (${pd.transferState} == 0) ? "mui-switch" : "mui-switch mui-active";
		$("#transfer-switch").attr("class",className);
	}
	
	function switchService(param) {
		//console.info(param.context);
		var id = param.attr("id");
		var value = param.attr("value");
		console.info("id,{},value,{}",id,value);
		//mui("#recharge-switch").switch().toggle();
		var isActive = document.getElementById(id).classList
				.contains("mui-active");
		if (isActive) {
			console.info(id + ":处于打开状态");
		} else {
			console.info(id + ":处于关闭状态");
		}
		$.post("/api/service/change_switch",{"serviceName":id,"status":value},function(data){
			if(data.msg == "ok"){
				console.info(data);
				var result = data.result;
				if(result.serviceName == "recharge"){
					var className = (result.status == 0) ? "mui-switch" : "mui-switch mui-active";
					$("#recharge-switch").attr("class",className);
					$("#recharge-switch").attr("value",result.status);
				}else if(data.result.serviceName == "transfer"){
					var className = (result.status == 0) ? "mui-switch" : "mui-switch mui-active";
					$("#transfer-switch").attr("class",className);
					$("#transfer-switch").attr("class",result.status);
				}
			}
		});
		
		
		
	}
</script>

</html>

