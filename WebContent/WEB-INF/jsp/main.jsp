<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title></title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/main.css" />
</head>
<body>
	<!--正文布局-->
	<div class="content">
		<input id="PageContext" type="hidden"
			value="${pageContext.request.contextPath}" />
		<input type="hidden" value="" id="group_id" />
		<div id="main">
			<div id="left">
				<!--顶部-->
				<div id="personal">
					<ul>
						<li><img
							src="<s:property value='%{#session.existUser.user_image}'/>" /></li>
						<li><s:property value="%{#session.existUser.user_account}" /></li>
						<li><img
							src="${pageContext.request.contextPath }/img/ellipsis.png" /></li>
					</ul>

				</div>
				<hr />
				<!--搜索框-->
				<div id="search">
					<input type="text" value="输入搜索" />

				</div>
				<!--按钮切换-->
				<div id="icon_change">
					<ul>
						<li><img
							src="${pageContext.request.contextPath }/img/speech-bubble.png" />
							<span id="news_count" class="hasNum" style="display:none">0</span> 
							<input type="hidden" value="0"></input></li>
						<li><img
							src="${pageContext.request.contextPath }/img/community (1).png" /></li>
					</ul>
				</div>
				<!--群列表、-->
				<div id="list">
					<ul>
						<li></li>
					</ul>

					<!--消息列表-->
					<ul>
						<%-- 					<li>
							<div class="info_wraper">
								<img src="../img/头像2.jpg" />
								<h4>群名</h4>
								<span class="last_news">你被移出本群</span>
							<span class="time">时间</span>
							<span class="newsCount">1</div>
							
							 
						</li>
								<li>
							<div class="info_wraper">
								<img src="../img/头像2.jpg" />
								<h4>群名</h4>
								<span class="last_news">小明邀请**进入群</span>
							<span class="time">时间</span>
							<span class="newsCount">1</div>
						</li> --%>



					</ul>
				</div>

				<!--更多按钮-->
				<div id="img_more">

					<ul>
						<li class="more">创建群</li>
						<li class="more">加入群</li>
					</ul>

					<img src="${pageContext.request.contextPath }/img/plus.png" />

				</div>
			</div>
			<!--右布局-->
			<div id="right">
				<iframe src="" width="" height="" name="iframea" scrolling="no"></iframe>
			</div>
			<div id="top_ul">
				<ul>
					<li class="more">修改个人资料</li>
					<li class="more">退出登录</li>
					<!-- Button trigger modal -->
				</ul>
			</div>
		</div>
	</div>

	<script type="text/javascript"
		src="${pageContext.request.contextPath }/js/jquery-1.11.1.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath }/js/jquery-1.11.1.min.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath }/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath }/js/交互/user.js"></script>
	<script src="${pageContext.request.contextPath }/js/交互/websocket.js"></script>
	<script type="text/javascript">
		var websocket = websocket.init(); 
		//保存websocket
		function getWebsocket(){
			return websocket;
		}
		$("#personal li:eq(2)").click(function() {
			$("#top_ul").toggle();
		})
		$("#img_more img").click(function() {
			$("#img_more ul").toggle();
		})
		$("#icon_change ul li:eq(0) img")
				.click(
						function() {
							$(this)
									.attr("src",
											"${pageContext.request.contextPath }/img/speech-bubble (1).png");
							$("#icon_change ul li:eq(1) img")
									.attr("src",
											"${pageContext.request.contextPath }/img/community.png");
							$("#list ul:eq(0)").hide();
							$("#list ul:eq(1)").show();

						})
		$("#icon_change ul li:eq(1) img")
				.click(
						function() {
							$(this)
									.attr("src",
											"${pageContext.request.contextPath }/img/community (1).png");
							$("#icon_change ul li:eq(0) img")
									.attr("src",
											"${pageContext.request.contextPath }/img/speech-bubble.png");
							$("#list ul:eq(0)").show();
							$("#list ul:eq(1)").hide();
						})

		/*群列表点击监听*/
		$(document).on(
				'click',
				'div#list ul:eq(0) li',
				function() {
					var group_id = $(this).next(".group_id").val()
				//设置隐藏元素group_id
					$("#group_id").val(group_id)
					var index = $(this).find(".index").val()
					
					setTimeout(function() {
						$("div#right iframe").attr(
								"src",
								"${pageContext.request.contextPath }/chatRecord_communicate?group_id="
										+ group_id + "&newsCount=" + 0);
					}, 0);
				})
		/* 消息列表点击监听 */
		$(document).on(
				'click',
				'div#list ul:eq(1) li',
				function() {
					//点击的是消息列表
					var commonMessage=$(this).find("#commonMessage");
					if (commonMessage.length==0) {
					 
						var group_id = $(this).find(".group_id").val()
						//设置隐藏元素group_id
						$("#group_id").val(group_id)
					}
					
					
					var newsCount = $(this).find(".hideNewsCount").text()
					
					//获得单项计数
					if (newsCount == "") {
						newsCount = 0;
					}
					//总数减少
					$("#news_count").next().val(
							$("#news_count").next().val() - newsCount)

					if ($("#news_count").next(":hidden").val() <= 0) {
						$("#news_count").hide();
					} else if ($("#news_count").next(":hidden").val() > 0
							&& $("#news_count").next(":hidden").val() < 10) {
						$("#news_count").removeClass("noNum")
								.addClass("hasNum")
						$("#news_count").text($("#news_count").next().val())
								.show()
					}
					//取消消息计数标记
					$(this).find(".newsCount").hide();
					if(commonMessage.length>0){
						setTimeout(
								function() {
									$("div#right iframe").attr(
											"src",
											"${pageContext.request.contextPath }/commonMessage_information");
								}, 0);
					}else{
						setTimeout(
								function() {
									$("div#right iframe").attr(
											"src",
											"${pageContext.request.contextPath }/chatRecord_communicate?group_id="
													+ group_id + "&newsCount="
													+ newsCount);
								}, 0);
					}
					//设置该项消息为0
					$(this).find(".hideNewsCount").text(0)
				})
		$("div#top_ul li:eq(0)").click(
				function() {
					$("div#right iframe").attr(
							"src",
							pojo.url.modify_user
									+ "?user_id=${existUser.user_id}")
					//				$("div#right iframe").load("modify_personal.html");
				})
		/*是否退出登录*/
		$("div#top_ul li:eq(1)").click(
				function() {
					var c = confirm("是否退出登录");
					if (c == true) {
						window.location.href = pojo.url.user_logout
								+ "?user_account=${existUser.user_account}";
					}
				})

		$("div#img_more li:eq(0)")
				.click(
						function() {
							$("div#right iframe")
									.attr("src",
											"${pageContext.request.contextPath }/chatGroup_createGroup");
							//				 $("div#right").load("create_group.html");
						})

		$("div#img_more li:eq(1)")
				.click(
						function() {
							$("div#right iframe")
									.attr("src",
											"${pageContext.request.contextPath }/chatGroup_addGroup");
							//				 $("div#right").load("add_group.html");
						})

		/*搜索框监听*/
		$("div#search input").bind('input propertychange', function() {
			pojo.chatGroup.query({
				ul : $("div#list ul:eq(0)"),
				key : $(this).val()
			});

		})
		/* 	搜索框获取焦点 */
		$("div#search input").focus(function() {
			$(this).val("")
		})
		/* 搜索框失去焦点 */
		$("div#search input").blur(function() {
			$(this).val("输入群名搜索")
		})
		$("div#list ul:eq(1) li")
				.click(
						function() {
							$("div#right iframe")
									.attr("src",
											"${pageContext.request.contextPath }/chatGroup_information");
						})
		/*初始化已加入群列表和消息列表*/
		$(document).ready(function() {
			$("div#list ul:eq(1)").hide();
			pojo.chatGroup.loadJoined({
				ul : $("div#list ul:eq(0)"),
				iframe : $("iframe"),
				root : $("#PageContext").val(),
				websocket : getWebsocket(),
				getNewsul : $("div#list ul:eq(1)"),
				getGroupsul:$("div#list ul:eq(0)"),
				getImgCount : $("#news_count"),
				getImgCountHide : $("#news_count").next(":hidden"),
				user_id:"${existUser.user_id}"
			});
		})
		//离开页面或者刷新页面提示
		var beginTime = 0;//执行onbeforeunload的开始时间
var differTime = 0;//时间差
window.onunload = function (){
        differTime = new Date().getTime() - beginTime;
        if(differTime <= 5) {
            console.log("浏览器关闭")
        	pojo.user.close_window("${existUser.user_account}");
        }else{
            console.log("浏览器刷新")
        }
    }
    window.onbeforeunload = function (){
    	       beginTime = new Date().getTime();
    };
	</script>

</body>

</html>