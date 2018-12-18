<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/information.css" />
</head>
<body>
	<div id="information">
		<h2>消息列表</h2>
		<!--内容-->
		<div id="main">
			<ul>
			</ul>
		</div>
	</div>

	<script type="text/javascript"
		src="${pageContext.request.contextPath }/js/jquery-1.11.1.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath }/js/交互/information.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath }/js/交互/websocket.js"></script>
	<script type="text/javascript">
		var websocket=websocket.init();
		//加载页面内容
		pojo.commonMessage.loadInformation({
			ul:$("ul")
		})
			 //点击接受时的监听
			 $(document).on('click','button',function(){
				 $(this).addClass("isReceive");
				 pojo.commonMessage.agree({
					 type:"information",
					 commonMessage_id:$(this).next(":hidden").val(),
					 sender_id:"${existUser.user_id}",
					 websocket:websocket
				 })
			 }) 
				 pojo.commonMessage.receive({
					 websocket:websocket,
					 user_id:"${existUser.user_id}"
			 	}) 
					
		</script>
</body>
</html>

