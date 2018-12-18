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
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/communication.css" />
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css"
	rel="stylesheet">
<!-- 加载bootstrap3 -->
</head>

<body>
	<div id="communication">
		<!--顶部布局-->
		<div id="top">
			<s:hidden value="" id="group_id" />
			<s:hidden value="" id="user_id" />
			<h3>群名</h3>
			<!--选择框-->
			<select name="com">
				<option value="" id="option1">聊天界面</option>
				<option value="" id="option2">实时位置</option>
				<option value="" id="option3">群详情</option>
			</select>

		</div>
		<!--聊天界面-->
		<div id="communitcate" class="group_info">

			<!--聊天主界面-->
			<div id="talk"></div>
			<!--发送消息框-->
			<div id="send">
				<textarea name="sendMsg" rows="" cols="">
							输入内容发送
						</textarea>
				<button>发送</button>
			</div>
		</div>

		<!--群详情-->
		<div id="detail" class="group_info">
			<img src="${pageContext.request.contextPath}/img/头像2.jpg" />
			<hr />
			<table cellpadding="0" cellspacing="0">
				<tr>
					<th>群简介:</th>
					<td>这是群简介</td>
				</tr>
				<tr>
					<th>群主:</th>
					<td>张三</td>
				</tr>
				<tr>
					<th>移除/邀请：</th>
					<td>
						<ul>
							<li><img
								src="${pageContext.request.contextPath}/img/remove.png" /> <img
								src="${pageContext.request.contextPath}/img/plus.png" /></li>
						</ul>
					</td>
				</tr>

			</table>
			<button>删除并退出</button>
		</div>
		<!--实时位置-->
		<div id="allmap" class="group_info"></div>
		<input type="hidden" value="3" />
		<div id="newMsgTip"></div>
		<input type="hidden" value="" id="newsHeight"/>
		<div id="tip">请输入内容!</div>
		<div id="removeMember">
			<!--搜索框-->
			<div id="search">
				<input type="text" value="输入搜索成员" />
			</div>
			<ul>

			</ul>
			<button>取消</button>
			<button>移除该成员</button>
			<div id="removeTip"></div>
		</div>
	</div>
	<script
		src="${pageContext.request.contextPath }/js/jquery-1.11.1.js"></script>
	<!-- 加载bootstrap3 -->
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	<script type="text/javascript"
		src="http://api.map.baidu.com/api?v=2.0&ak=ERtM3YQEAaV3hVRZ67KUTyE6Of0GQni8"></script>
	<script type="text/javascript"
		src="http://api.map.baidu.com/library/TextIconOverlay/1.2/src/TextIconOverlay_min.js"></script>
	<script type="text/javascript"
		src="http://api.map.baidu.com/library/MarkerClusterer/1.2/src/MarkerClusterer_min.js"></script>

	<script src="${pageContext.request.contextPath}/js/交互/websocket.js"></script>
	<script src="${pageContext.request.contextPath}/js/交互/communication.js"></script>

	<script type="text/javascript">
			$("select").change(function()

				{
					if($("select option:selected").text() == "实时位置") {
						$("div#communitcate").hide();
						$("div#detail").hide();
						$("div#allmap").show();
						/* 加载地图中群成员实时位置 */
					 	pojo.map.realLocation({
					 		group_id:<%=request.getParameter("group_id")%>,
					 		user_account:"${existUser.user_account}",
					 		user_image:"${existUser.user_image}"
					 	})
					} else if($("select option:selected").text() == "群详情") {
						$("div#communitcate").hide();
						$("div#detail").show();
						$("div#allmap").hide();
					} else {
						$("div#communitcate").show();
						$("div#detail").hide();
						$("div#allmap").hide();
					}
				})
		</script>
	<!-- 开始加载 -->
	<script>
 	var websocket=websocket.init();
	//显示顶部群名	 	
	pojo.groupDetail.findDetail({
		group_name:$("div#top h3:eq(0)"),
		group_id:<%=request.getParameter("group_id")%>,	
 		groupNotice:$("table td:eq(0)"),
 		groupOwner:$("table td:eq(1)"),
 		img:$("td:eq(2) img:eq(0)"),  //移除按钮
 		hidden:$("#group_id"), //隐藏元素
 		select:$("select"),
 		user_id:$("#user_id")//群主id
	})
				/* 加载聊天界面：隐藏尚未查看消息 */
				 	pojo.communicate.getNewMsg({
				 		user_id:"${existUser.user_id}",
				 		group_id:<%=request.getParameter("group_id")%>,
				 		div:$("#talk"),
				 		newMsgTip:$("#newMsgTip"),
				 		select:$("select"),
				 		newsCount:<%=request.getParameter("newsCount")%>,
				 		newsHeight:$("#newsHeight")
				 	})
		 /*接收聊天信息*/
		 pojo.communicate.receive({
			 websocket:websocket,
			 user_id:"${existUser.user_id}",
			 group_id:<%=request.getParameter("group_id")%>,
			 div:$("#talk"),
	 		  removeTip:$("#removeTip")
		 })
	 	/* 邀请按钮监听 */
	 	$("td:eq(2) img:eq(1)").click(function(){
	 		window.location.href="${pageContext.request.contextPath}/chatRecord_invitation?group_id="+<%=request.getParameter("group_id")%>;
	 	})
	 	/*打开移除成员界面监听*/
	 		$("td:eq(2) img:eq(0)").click(function(){
	 			/*加载群成员*/
	 			pojo.groupDetail.removeMember({
	 				group_id:<%=request.getParameter("group_id")%>,
	 				ul:$("#removeMember ul")
	 			})
	 	$("#removeMember img").css("border","1px solid #9393FF");//初始化移除界面图片框
	 	 $("#removeMember").animate({"right":0},"slow");
	 	})
	 	/*取消移除监听*/
	 		$("#removeMember button:eq(0)").click(function(){
	 	 $("#removeMember").animate({"right":"-300px"},"slow");
	 	})
	 	/*确定移除监听*/
	 		$("#removeMember button:eq(1)").click(function(){ 
	 	  if (!$("#removeMember img").hasClass("remove")) {
	 				  	$("#removeTip").text("请选择成员！").fadeIn().fadeOut("slow");
				return false;
		}
	 	  pojo.groupDetail.removeClick({
				user_account:$("#removeMember img[class='remove']").attr("title"),
				group_id:<%=request.getParameter("group_id")%>,
				websocket:websocket
	 	  })
	 	})
	 	/*移除界面搜索获取焦点监听*/
	 	$("#removeMember input").focus(function(){
	 		$(this).val("")
	 	})
	 	/*移除界面搜索获取焦点监听*/
	 	$("#removeMember input").blur(function(){
	 		$(this).val("输入搜索成员")
	 	})
	 	/*移除界面搜索获取关键字*/
	 	$("#removeMember input").bind('input propertychange',function(){
	 		pojo.groupDetail.search2({
	 			group_id:<%=request.getParameter("group_id")%>,
	 			key:$(this).val(),
	 			ul:$("#removeMember ul")
	 		})
	 	})
	 	/*移除界面头像点击*/
	 	$(document).on('click',"#removeMember img",function(){
	 		$("#removeMember img").removeClass("remove");
	 		$("#removeMember img").css("border","1px solid #9393FF")
	 		
	 		$(this).addClass("remove");
	 		$(this).css("border","2px solid red")
	 	})
	 		 		//发送内容
	 		$(document).ready(function(){
	 				
	 			 	/*聊天发送按钮监听*/
	 			 	$("div#send button").click(function(){ 
	 			 		if ($.trim($("#send textarea").val()).length==0) {
	 			 			$("#tip").fadeIn().fadeOut("slow");
	 						return false;
	 					}
	 			 		pojo.communicate.send({
	 			 			 record_content:$("#send textarea").val(),
	 			 			websocket:websocket,
	 			 			div:$("#talk"),
	 			 			user_image:"${existUser.user_image}",
	 			 			user_id:"${existUser.user_id}",
	 			 			group_id:<%=request.getParameter("group_id")%>
	 			 		})
	 			 	})
	 })
	 	//加载未查看消息
	 		 $(document).on('click','#newMsgTip',function(){
		 	$(this).hide()
		 	 //计算应该滚动的高度
		 	 var top=Number($("#newsHeight").val());
		 	$("#talk").scrollTop(top)
		 })
	 	/*聊天输入框获取焦点*/
	 	$("#send textarea").focus(function(){
	 		 $(this).text("");
	 	})
	 	/*删除并退出群聊*/
		$("div#detail button").click(function(){
			 if ($("#user_id").val()=="${existUser.user_id}") {
			var c=	confirm("将解散该群！");
			if (c) {
				pojo.groupDetail.exitGroup({
					user_id:"${existUser.user_id}",
					group_id:<%=request.getParameter("group_id")%>,
					websocket:websocket
				})
			}
			}else{
				var c=	confirm("将退出该群！");
				if (c) {
					pojo.groupDetail.exitGroup({
						user_id:"${existUser.user_id}",
						group_id:<%=request.getParameter("group_id")%>,	
						websocket:websocket
					})
				}
			}
		})
	</script>


</body>

</html>