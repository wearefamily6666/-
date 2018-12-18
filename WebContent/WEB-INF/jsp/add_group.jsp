<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		  <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
		<title></title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.min.css"/>
		 <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/add_group.css"/>
	</head>
	<body>
		<div id="add_group">
			<!--左侧-->
			<div id="add_group_left">`
				<h2>加入群</h2>
				<!--搜索框-->
				<div id="add_group_search">
					<input type="text" value="搜索群名"/>
					
				</div>
				<!--群列表-->
				<div id="add_group_list">
					<ul>
					</ul>
				</div>
			</div>
			<div id="add_group_divider">
				
			</div>
				<!--右侧-->
				<div id="add_group_right">
	 				<table></table>
					<ul>
					<li>取消</li>
					<li>确定</li>
				</ul>
				</div>
				
			</div>
				<div id="tip"></div>
	</body>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.1.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.1.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
		<script src="${pageContext.request.contextPath}/js/交互/addGroup.js"></script>
		<script src="${pageContext.request.contextPath}/js/交互/websocket.js"></script>
	<script type="text/javascript">
 	var websocket=websocket.init();
	//搜索框获得焦点
		$("#add_group_search input").focus(function(){
			$(this).val("")
		})
		//搜索框失去焦点
		$("#add_group_search input").blur(function(){
			$(this).val("搜索群名")
		})
		//动态添加的html添加事件用到
	 $(document).on('click','#add_group_list li',function()
				{  
			 pojo.addGroup.findDetail({
				 "table":$("div#add_group_right table"),
				 "group_id":$(this).find("input:hidden").val(),
				 "ul":$("#add_group_right ul")
			 }); 
		})
	//开始加载
		$(document).ready(function(){
			pojo.addGroup.findAllNotJoin(
					{ul:$("div#add_group_list ul:eq(0)")
					 }		
					);	 
				$("#add_group_right ul").hide();
		})
		//搜索监听
		$("#add_group_search input").bind('input propertychange',function(){
			pojo.addGroup.findAllNotJoinByKey(
					{ul:$("div#add_group_list ul:eq(0)"),
						key:$(this).val(),
						 ul2:$("#add_group_right ul")
					}		
					);	
		})
		//返回键
		$(document).on('click','#add_group_right li:eq(0)',function(){
			history.go(-1)
		})
		//申请加群
			$(document).on('click','#add_group_right li:eq(1)',function(){ 
				pojo.addGroup.applyToJoin({
				sender_id:"${session.existUser.user_id}",
				group_id:$("input:hidden").last().val(),
				websocket:websocket
			});
			
		})
		//接收消息
		$(document).ready(function(){
			pojo.addGroup.receive({
				tip:$("#tip"),
				websocket:websocket,
				user_id:"${existUser.user_id}"
			})
		})
	</script>
</html>