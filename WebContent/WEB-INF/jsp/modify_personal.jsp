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
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/modify_personal.css" />
</head>
<body>
	<!--修改个人资料-->

	<div id="modify_personal">
		<h2>修改个人资料</h2>
		 <form   action="user_modifyUserSuccess.action" method="post" enctype="multipart/form-data" id="form">
			<table border="0" cellspacing="" cellpadding="">
				<tr>
					<th>头像</th>
					<td><input type="file" name="some"
						/></td>
				</tr>
				<tr>
					<th>昵称</th>
					<td><input type="text"
						value="<s:property value='%{model.user_name}'/>" name="user_name" /></td>
				</tr>
				<tr>
					<th>手机号</th>
					<td><input type="text" name="user_phone" id=""
						value="<s:property value='%{model.user_phone}'/>" /></td>
				</tr>
				<tr>
					<th>邮箱</th>
					<td><input type="email" name="user_email"
						value="<s:property value='%{model.user_email}'/>" /></td>
				</tr>
			</table>
		
		<ul>
			<li>取消</li>
			 <li>保存</li>
		</ul> 
		</form>
	</div>
	<div id="modify_personal_tip">修改成功!</div>
</body>
<script src="http://static.runoob.com/assets/jquery-validation-1.14.0/lib/jquery.js"></script>
<script src="http://static.runoob.com/assets/jquery-validation-1.14.0/dist/jquery.validate.min.js"></script>
<script src="http://static.runoob.com/assets/jquery-validation-1.14.0/dist/localization/messages_zh.js"></script>
	<script src="${pageContext.request.contextPath }/js/交互/user.js"></script>
<script>
//返回
	$("div#modify_personal ul li:eq(0)").click(function() {
		history.go(-1);
	})
//表单 提交验证
	$("#form li:eq(1)").click(function() {  
		if (	 pojo.user.modify_user({
			"form" : $("#form"),
			"tip" : $("#modify_personal_tip"),
			"formData":new FormData(document.getElementById("form"))
		}).form()  ) 
			
			$("#form").submit();
		
	})
</script>
</html>
