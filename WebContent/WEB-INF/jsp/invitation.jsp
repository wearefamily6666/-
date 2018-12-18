<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		  <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
		<title></title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/invitation.css"/>
	</head>
	
	<body>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.1.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.1.min.js" ></script>
		<div id="invitation">
			<!--左侧-->
			<div id="invitation_left">`
				<h2>邀请新成员</h2>
				<!--搜索框-->
				<div id="invitation_search">
					<input type="text" value="输入群名搜索..."/>
					
				</div>
				<!--群列表-->
				<div id="invitation_list">
					<ul>
					
					</ul>
				</div>
			</div>
			<div id="invitation_divider">
				
			</div>
				<!--右侧-->
				<div id="invitation_right">
				<div id="invitation_right_top">
						
					</div>
				<div id="member_list">
					<ul>
						
						 
					</ul>
					<ul>
						
					</ul>
				</div>
				<ul id="last_ul" style="display:none">
					<li>取消</li>
					<li>邀请</li>
				</ul>
				</div>
			</div><div id="tip"></div>
	</body>
	<script src="${pageContext.request.contextPath}/js/交互/communication.js"></script>
	<script src="${pageContext.request.contextPath}/js/交互/websocket.js"></script>
	<script type="text/javascript">
	var websocket=websocket.init();
	//初始化群列表
	$(document).ready(function(){
		 pojo.groupDetail.invitation({
			 ul:$("#invitation_list ul"),
			group_id:'<%=request.getParameter("group_id")%>'
		 })
		 	pojo.communicate.receive({
				  tip:$("#tip"),
				  websocket:websocket,
				  user_id:"${existUser.user_id}"
			})
	})
	//群列表选择
	 $(document).on('click','#invitation_list li',function()
				{ 
			pojo.groupDetail.group_click({
				group_id:$(this).find("input:hidden").val(),
				group_id2:'<%=request.getParameter("group_id")%>',
				ul2:$("#last_ul"),
				member_list_ul1:$("#member_list ul:eq(0)"),
				member_list_ul2:$("#member_list ul:eq(1)")
			})
		})
		
		 /* 搜索框获取焦点 */
		$("div#invitation_search input").focus(function(){
			$(this).val("")
		})
		/* 搜索框失去焦点 */
		$("div#invitation_search input").blur(function(){
			$(this).val("输入群名搜索...")
		})
		/*搜索监听*/
		 $("#invitation_search input").bind('input propertychange',function(){
			pojo.groupDetail.search({
				key:$(this).val(),
				ul:$("#invitation_list ul"),
				group_id:'<%=request.getParameter("group_id")%>'
			}) 
		 })
		//成员列表选择
		$(document).on('click','#member_list ul:eq(0) li',function(){  
			var index=$(this).find("input:hidden").val()
			console.log("click"+index)
			$("#member_list ul:eq(1)").find("input:eq("+index+")").attr("checked",true) 
		})
	 
		//返回监听
		$("#last_ul li:eq(0)").click(function(){
			history.go(-1)
		})
		//邀请监听
		$(document).on('click','#last_ul li:eq(1)',function(){
			if ($("#member_list ul:eq(1) input:checked").val()==null) {
				$("#tip").text("请选择成员").fadeIn().fadeOut("show");
				return false;
			}
			var index=$("#member_list ul:eq(1) input:checked").val();
			 pojo.groupDetail.doInvite({
				user_account:$.trim($("#member_list ul:eq(0) li:eq("+index+")").find("span").text()),
				group_id:<%=request.getParameter("group_id")%>,
				type:"inviteMember",
				websocket:websocket,
				user_id:"${session.existUser.user_id}"
			 })
		})
 
	</script>
</html>