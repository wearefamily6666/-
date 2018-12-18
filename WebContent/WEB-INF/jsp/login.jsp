<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  		  <meta name="viewport" content="width=device-width, initial-scale=1">
		<title></title>
		 <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css"/>
	</head>
	<body>
		<div class="content">
			<div class="header">
					<ul>
						<li><button>账号登录</button></li>
						<li></li>
						<li><button>扫码登录</button></li>
					</ul>
			</div>
			 <div class="login">
			 	<h2>用户登录</h2>
			 	<s:form  id="form" method="post" namespace="/" theme="simple">
			 		<table border="0" cellspacing="0" cellpadding="0">
			 		<tr><th></th><td style="color:red"></td></tr>
			 		<tr><th>用户名</th><td><input type="text" name="user_account"/></td></tr>
			 		<tr><th>密码</th><td><input type="password" name="user_password"/></td></tr>
			 	</table>
			 	
			 	<button>登录</button>
			 	</s:form>

			 </div>
			<div class="img" >
				<h3>用天下纵横app扫码登录</h3>
				<img src="${pageContext.request.contextPath}/img/二维码.PNG" alt="登录二维码" />
				
			</div>
		 
		</div>
<script src="${pageContext.request.contextPath }/js/jquery-1.11.1.js"></script>
<script src="http://static.runoob.com/assets/jquery-validation-1.14.0/dist/jquery.validate.min.js"></script>
<script src="http://static.runoob.com/assets/jquery-validation-1.14.0/dist/localization/messages_zh.js"></script>
<script src="${pageContext.request.contextPath }/js/交互/user.js"></script>
		<script type="text/javascript">
			$("button:eq(0)").click(function()
			{
				$(".login").show();
				$(".img").hide();
			})
			$("button:eq(1)").click(function()
			{
				$(".img").show();
				$(".login").hide();
			})
		</script>
		<script>
			$(".login button").click(function(){ 
			pojo.user.login({
			form:	$(".login #form"),
			error:$(".login td:eq(0)")
			})
			});
		</script>
	</body>

</html>