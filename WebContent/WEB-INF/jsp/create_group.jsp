<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta charset="UTF-8">
		  <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
		<title></title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/create_group.css"/>
	</head>
	<body>
						<div id="create_group">
				<h2>创建群</h2>		
				<form action="" type="post" enctype="multipart/form-data" id="form">
			<table border="0" cellspacing="" cellpadding="">
				<tr><th>群头像</th><td><input type="file" name="some"/></td></tr>
				<tr><th>群名称</th><td><input type="text" value="输入群名称" name="group_name"/></td></tr>
				<tr><th>群简介</th><td><input type="text" value="输入群简介" name="group_notice"/></td></tr>
			</table>
			<ul>
					 	<li>取消</li>
					 	<li>确定</li> 	 	
					 	
					 </ul>
					 </form>
				</div>
					<div id="tip"></div>
	</body>
<script src="http://static.runoob.com/assets/jquery-validation-1.14.0/lib/jquery.js"></script>
<script src="http://static.runoob.com/assets/jquery-validation-1.14.0/dist/jquery.validate.min.js"></script>
<script src="http://static.runoob.com/assets/jquery-validation-1.14.0/dist/localization/messages_zh.js"></script>
	<script src="${pageContext.request.contextPath }/js/交互/createGroup.js"></script>
 <script>
//返回
	$("#form ul li:eq(0)").click(function() {
		history.go(-1);
	})
//表单 提交验证
 	$("#form ul li:eq(1)").click(function(){
 		if (pojo.chatGroup.createGroup({
 			"form":$("#form"),
 			"tip":$("#tip"),
 			"formData":new FormData(document.getElementById("form"))
 		}).form())  
			$("#form").submit();
		 
 	})
 //	输入框获得焦点
 var sum=0;
 	$("#form table input:eq(1)").focus(function(){
 		if (sum==0) {
 			$(this).val("");
 			sum++;
		}
 		
 	})
 	var count=0;
 	$("#form table input:eq(2)").focus(function(){
 		if (count==0) {
 			$(this).val("");
 			count++;
		}
 	})
 </script>
</html>