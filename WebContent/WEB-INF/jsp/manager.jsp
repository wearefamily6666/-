<%@page import="com.sun.jndi.toolkit.url.Uri"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<title></title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/honeySwitch.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/manager.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/dist/autocomplete.css" />
</head>

<body>
	<script src="${pageContext.request.contextPath}/js/jquery-1.11.1.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/honeySwitch.js"></script>
	<script src="https://code.highcharts.com/highcharts.js"></script>
	<script
		src="${pageContext.request.contextPath}/js/dist/autocomplete.js"></script>
	<div class="wraper_main">
		<!--导航栏-->
		<header>
			<nav>
				<button>天下纵横后台管理系统</button>
				<h2>用户管理</h2>
				<h2>群管理</h2>
			</nav>
		</header>
		<!--表格-->
		<div class="content">
			<!--搜索框-->
			<div class="wrapper">
				<div id="search-form"></div>
				<div id="message"></div>
			</div>
			<!--用户管理-->
			<div class="user_manager">

				<table border="1" cellpadding="0" cellspacing="0"
					class="table-responsive">


				</table>
			</div>
			<!--群管理-->
			<div class="group_manager">
				<ul>
					<li>
						<table border="1" cellspacing="0" cellpadding="0">
						</table>
					</li>
					<!--曲线图-->
					<li>
						<div id="container"
							style="width: 550px; height: 400px; margin: 0 auto"></div>
					</li>
				</ul>

			</div>


		</div>

		<footer>
			<div class="page">
				<ul>
					<li>每页 <select name="pageSize">
							<option value="5">5</option>
							<option value="8">8</option>
					</select> 条记录
					</li>
					<li>共<span id="totalCount"></span> 条记录，
						<div id="page"></div> /<span id="totalPage"></span>页
					</li>
					<li>

						<button class="up">首页</button>
						<button class="up">上一页</button>
						<button class="next">下一页</button>
						<button class="next">尾页</button>
					</li>
				</ul>
			</div>
		</footer>
		<div id="tip"></div>
	</div>
	<script src="${pageContext.request.contextPath }/js/交互/user_page.js"></script>
	<script src="${pageContext.request.contextPath }/js/交互/group_page.js"></script>
	<script src="${pageContext.request.contextPath }/js/交互/search_user_page.js"></script>
	<script src="${pageContext.request.contextPath }/js/交互/search_group_page.js"></script>
	<script src="${pageContext.request.contextPath }/js/交互/manager_forbidden.js"></script>

	<script type="text/javascript">
		var i = 0;//判断切换
		var isSearch=0;//判断是否有搜索
		var key;//保存关键词，防止点击提交搜索后，删除搜索框内容，导致继续点击分页按钮无效
		$(document).ready(function() {
			$("#search-form input").attr("placeHolder", "输入用户名搜索")

			//分页：访问用户首页
			pojo_userPage.user_manager.First({
				pageSize : 5,
				table : $(".user_manager table"),
				totalCountSpan : $("#totalCount"),
				totalPageSpan : $("#totalPage"),
				optionPage : $("div #page"),
				//判断是否该隐藏4个按钮
				upButton : $(".up"),
				nextButton : $(".next")
			})

		})
		$("h2:eq(0)").click(function() {
			i = 1;//更改切换变量
			$("div.user_manager").show();
			$("div.group_manager").hide();
			$(this).css("color", "white");
			$("h2:eq(1)").css("color", "#c7bcbc");
			$("#search-form input").attr("placeHolder", "输入用户名搜索")
		})
		$("h2:eq(1)").click(function() {
			i = 2;//更改切换变量
			$("div.user_manager").hide();
			$("div.group_manager").show();
			$(this).css("color", "white");
			$("h2:eq(0)").css("color", "#c7bcbc");
			$("#search-form input").attr("placeHolder", "输入群名搜索")

		})
		$("h2:eq(0)").click(function(){
			//分页：访问群首页
			pojo_userPage.user_manager.First({
				pageSize : 5,
				table : $(".user_manager table"),
				totalCountSpan : $("#totalCount"),
				totalPageSpan : $("#totalPage"),
				optionPage : $("div #page"),
				//判断是否该隐藏4个按钮
				upButton : $(".up"),
				nextButton : $(".next")
			})
		})
				$("h2:eq(1)").click(function(){
			//分页：访问群首页
			pojo_groupPage.group_manager.First({
				pageSize : 5,
				table : $(".group_manager table"),
				totalCountSpan : $("#totalCount"),
				totalPageSpan : $("#totalPage"),
				optionPage : $("div #page"),
				//判断是否该隐藏4个按钮
				upButton : $(".up"),
				nextButton : $(".next")
			})
		})
		/*滑动开关*/
		/*用户管理*/
		$(document).on('click', '.user_manager [class^=switch]', function() {
			var val = $(this).parent().parent().find("td:eq(0)").text();
			console.log("滑动： user_id:" + val)
			forbidden.user_forbidden({
				user_id : val,
				tip : $("#tip")
			})
		})

		/*群管理*/
		$(document).on('click', '.group_manager [class^=switch]', function() {
			var val = $(this).parent().parent().find("td:eq(0)").text();
			forbidden.group_forbidden({
				group_id : val,
				tip : $("#tip")
			})
		})
		/*搜索框*/
		var proposals = [ '软件162', '百度2', '百度3', '百度4', 'a1', 'a2', 'a3', 'a4',
				'b1', 'b2', 'b3', 'b4' ,"张三",'张三2','张三3',"nba2konline",'nba2konline交流群2'];
		$('#search-form').autocomplete({
			hints : proposals,
			width : 300,
			height : 30,
			onSubmit : function(text) {
				isSearch=2;//有搜索标志
				$('#message').html('Selected: <b>' + text + '</b>');
				key=text;
				/* 搜索监听 */
				if (i == 1 || i == 0) {
					console.log("submit: " + i)
					pojoSearch_userPage.user_manager.First({
					pageSize : $(".page li:eq(0) select").val(),
					table : $(".user_manager table"),
					//判断是否该隐藏4个按钮
					upButton : $(".up"),
					nextButton : $(".next"),
					key:text,
					totalCountSpan : $("#totalCount"),
					totalPageSpan : $("#totalPage"),
					optionPage : $("div #page")
					
				})
				} else if (i == 2) {
					console.log("submit: " + i)
				pojoSearch_groupPage.group_manager.First({
					pageSize : $(".page li:eq(0) select").val(),
					table : $(".group_manager table"),
					//判断是否该隐藏4个按钮
					upButton : $(".up"),
					nextButton : $(".next"),
					key:text,
					totalCountSpan : $("#totalCount"),
					totalPageSpan : $("#totalPage"),
					optionPage : $("div #page")
				})
				}

			}
		});
		//i=1：用户 i=2群
		/*分页：首页*/
		$(document).on('click', ".page button:eq(0)", function() {
			/*点击4个分页按钮切换当前页数显示option*/
			$("#page select ").get(0).selectedIndex = 0;//此处应该减1
			/* 请求首页 */
			//判断当前是否在用户列表界面
			if (i == 0 || i == 1) {
				//判断是否有搜索
				if (isSearch==0||isSearch==1) {
					
				pojo_userPage.user_manager.First({
					pageSize : $(".page li:eq(0) select").val(),
					table : $(".user_manager table"),
					//判断是否该隐藏4个按钮
					upButton : $(".up"),
					nextButton : $(".next")
				})
				}else{
					pojoSearch_userPage.user_manager.First({
						pageSize : $(".page li:eq(0) select").val(),
						table : $(".user_manager table"),
						//判断是否该隐藏4个按钮
						upButton : $(".up"),
						nextButton : $(".next"),
						key:key
					})
				}
			} else {
				//判断是否有搜索
				if (isSearch==0||isSearch==1) {
					pojo_groupPage.group_manager.First({
						pageSize : $(".page li:eq(0) select").val(),
						table : $(".group_manager table"),
						//判断是否该隐藏4个按钮
						upButton : $(".up"),
						nextButton : $(".next")
					})
				}else{
					pojoSearch_groupPage.group_manager.First({
						pageSize : $(".page li:eq(0) select").val(),
						table : $(".group_manager table"),
						//判断是否该隐藏4个按钮
						upButton : $(".up"),
						nextButton : $(".next"),
						key:key
					})
					
				}
				
			}

		})
		/*根据设置每页数量获得页数*/
		$(document).on('change','select[name="pageSize"]',function(){console.log("change: ")
			//判断当前是否在用户列表界面
			if (i == 0 || i == 1) {
				//判断是否有搜索
				if (isSearch==0||isSearch==1) {
					
					//分页：访问群首页
					pojo_userPage.user_manager.First({
						pageSize : $("select[name='pageSize']").val(),
						table : $(".user_manager table"),
						totalCountSpan : $("#totalCount"),
						totalPageSpan : $("#totalPage"),
						optionPage : $("div #page"),
						//判断是否该隐藏4个按钮
						upButton : $(".up"),
						nextButton : $(".next")
					})
 
				}else{
					//分页：访问群首页
					pojoSearch_userPage.user_manager.First({
						pageSize : $("select[name='pageSize']").val(),
						table : $(".user_manager table"),
						totalCountSpan : $("#totalCount"),
						totalPageSpan : $("#totalPage"),
						optionPage : $("div #page"),
						//判断是否该隐藏4个按钮
						upButton : $(".up"),
						nextButton : $(".next"),
						key:key
					})
 
				}
			} else {
				//判断是否有搜索
				if (isSearch==0||isSearch==1) {
					//分页：访问群首页
					pojo_groupPage.group_manager.First({
						pageSize : $("select[name='pageSize']").val(),
						table : $(".group_manager table"),
						totalCountSpan : $("#totalCount"),
						totalPageSpan : $("#totalPage"),
						optionPage : $("div #page"),
						//判断是否该隐藏4个按钮
						upButton : $(".up"),
						nextButton : $(".next")
					})
 
				}else{
					//分页：访问群首页
				pojoSearch_groupPage.group_manager.First({
					pageSize : $("select[name='pageSize']").val(),
						table : $(".group_manager table"),
						totalCountSpan : $("#totalCount"),
						totalPageSpan : $("#totalPage"),
						optionPage : $("div #page"),
						//判断是否该隐藏4个按钮
						upButton : $(".up"),
						nextButton : $(".next"),
						key:key
					})
				}
			}
		})
		
	 
		/*分页：尾页*/
		$(document).on(
				'click',
				".page button:eq(3)",
				function() {
					console.log("尾页")
					/*点击4个分页按钮切换当前页数显示option*/
					$("#page select ").get(0).selectedIndex = $("#totalPage")
							.text() - 1;//此处应该减1
					/* 请求尾页  */
					//判断当前是否在用户列表界面
					if (i == 0 || i == 1) {
						//是否有搜索
						if (isSearch==0||isSearch==1) {
							pojo_userPage.user_manager.Last({
								pageSize : $(".page li:eq(0) select").val(),
								table : $(".user_manager table"),
								//判断是否该隐藏4个按钮
								upButton : $(".up"),
								nextButton : $(".next")
							})
						}else{
							pojoSearch_userPage.user_manager.Last({
								pageSize : $(".page li:eq(0) select").val(),
								table : $(".user_manager table"),
								//判断是否该隐藏4个按钮
								upButton : $(".up"),
								nextButton : $(".next"),
								key:key
							})
						}
					
					} else {
						//是否有搜索
						if (isSearch==0||isSearch==1) {
							pojo_groupPage.group_manager.Last({
								pageSize : $(".page li:eq(0) select").val(),
								table : $(".group_manager table"),
								//判断是否该隐藏4个按钮
								upButton : $(".up"),
								nextButton : $(".next")
							})
						}else{
							pojoSearch_groupPage.group_manager.Last({
								pageSize : $(".page li:eq(0) select").val(),
								table : $(".group_manager table"),
								//判断是否该隐藏4个按钮
								upButton : $(".up"),
								nextButton : $(".next"),
								key:key
							})
						}

					}

				})

		/*分页：上一页*/
		$(document).on(
				'click',
				".page button:eq(1)",
				function() {
					console.log("上一页")
					/*点击4个分页按钮切换当前页数显示option*/
					$("#page select ").get(0).selectedIndex = $("#page select")
							.val() - 2;//此处应该减1
					/* 	请求上一页 */
					if (i == 0 || i == 1) {
						if (isSearch==0||isSearch==1) {
							pojo_userPage.user_manager.CurrentPage({
								currentPage : $("#page select").val()-1,
								pageSize : $(".page li:eq(0) select").val(),
								table : $(".user_manager table"),
								//判断是否该隐藏4个按钮
								upButton : $(".up"),
								nextButton : $(".next")
							})
						}else{
							pojoSearch_userPage.user_manager.CurrentPage({
								currentPage : $("#page select").val()-1,
								pageSize : $(".page li:eq(0) select").val(),
								table : $(".user_manager table"),
								//判断是否该隐藏4个按钮
								upButton : $(".up"),
								nextButton : $(".next"),
								key:key
							})
						}
						
					} else {
						if (isSearch==0||isSearch==1) {
							pojo_groupPage.group_manager.CurrentPage({
								currentPage : $("#page select").val()-1,
								pageSize : $(".page li:eq(0) select").val(),
								table : $(".group_manager table"),
								//判断是否该隐藏4个按钮
								upButton : $(".up"),
								nextButton : $(".next")
							})
						}else{
							pojoSearch_groupPage.group_manager.CurrentPage({
								currentPage : $("#page select").val()-1,
								pageSize : $(".page li:eq(0) select").val(),
								table : $(".group_manager table"),
								//判断是否该隐藏4个按钮
								upButton : $(".up"),
								nextButton : $(".next"),
								key:key
							})
						}
					
					}

				})

		/*分页：下一页*/
		$(document).on('click', ".page button:eq(2)", function() {
			console.log("下一页")
			/*点击4个分页按钮切换当前页数显示option*/
			$("#page select ").get(0).selectedIndex = $("#page select").val();//此处应该减1
			/* 请求下一页 */
			if (i == 0 || i == 1) {
				if (isSearch==0||isSearch==1) {
					pojo_userPage.user_manager.CurrentPage({
						currentPage : $("#page select").val()-1,
						pageSize : $(".page li:eq(0) select").val(),
						table : $(".user_manager table"),
						//判断是否该隐藏4个按钮
						upButton : $(".up"),
						nextButton : $(".next")
					})
				}else{
					pojoSearch_userPage.user_manager.CurrentPage({
						currentPage : $("#page select").val()-1,
						pageSize : $(".page li:eq(0) select").val(),
						table : $(".user_manager table"),
						//判断是否该隐藏4个按钮
						upButton : $(".up"),
						nextButton : $(".next"),
						key:key
					})
				}
		
			} else {
				if (isSearch==0||isSearch==1) {
					pojo_groupPage.group_manager.CurrentPage({
						currentPage : $("#page select").val()-1,
						pageSize : $(".page li:eq(0) select").val(),
						table : $(".group_manager table"),
						//判断是否该隐藏4个按钮
						upButton : $(".up"),
						nextButton : $(".next")
					})
				}else{
					pojoSearch_groupPage.group_manager.CurrentPage({
						currentPage : $("#page select").val()-1,
						pageSize : $(".page li:eq(0) select").val(),
						table : $(".group_manager table"),
						//判断是否该隐藏4个按钮
						upButton : $(".up"),
						nextButton : $(".next"),
						key:key
					})
				}
				
			}

		})

		/*分页：根据当前页数搜索*/
		$(document).on('change', '#page select', function() {console.log("changeaaa: "+$("#page select").val())
			/* 请求当前页数 */
			if (i == 0 || i == 1) {
				if (isSearch==0||isSearch==1) {
					pojo_userPage.user_manager.CurrentPage({
						currentPage : $(this).val(),
						pageSize : $(".page li:eq(0) select").val(),
						table : $(".user_manager table"),
						//判断是否该隐藏4个按钮
						upButton : $(".up"),
						nextButton : $(".next")
					})
				}else{
					pojoSearch_userPage.user_manager.CurrentPage({
						currentPage : $(this).val(),
						pageSize : $(".page li:eq(0) select").val(),
						table : $(".user_manager table"),
						//判断是否该隐藏4个按钮
						upButton : $(".up"),
						nextButton : $(".next"),
						key:key
					})
				}
			
			} else {
				if (isSearch==0||isSearch==1) {
					pojo_groupPage.group_manager.CurrentPage({
						currentPage : $(this).val(),
						pageSize : $(".page li:eq(0) select").val(),
						table : $(".group_manager table"),
						//判断是否该隐藏4个按钮
						upButton : $(".up"),
						nextButton : $(".next")

					})
				}else{
					pojoSearch_groupPage.group_manager.CurrentPage({
						currentPage : $(this).val(),
						pageSize : $(".page li:eq(0) select").val(),
						table : $(".group_manager table"),
						//判断是否该隐藏4个按钮
						upButton : $(".up"),
						nextButton : $(".next"),
						key:key
					})
				}
			
			}

		})
	</script>

	<script>
		
	</script>
	<!--曲线图js-->
	<script language="JavaScript">
		$(document)
				.ready(
						function() {
							var title = {
								text : '天下纵横使用情况'
							};
							var subtitle = {
								text : ''
							};
							var xAxis = {
								categories : [ 'Jan', 'Feb', 'Mar', 'Apr',
										'May', 'Jun', 'Jul', 'Aug', 'Sep',
										'Oct', 'Nov', 'Dec' ]
							};
							var yAxis = {
								title : {
									text : '登录人数'
								},
								plotLines : [ {
									value : 0,
									width : 1,
									color : '#808080'
								} ]
							};

							var tooltip = {
								valueSuffix : '人'
							}

							var legend = {
								layout : 'vertical',
								align : 'right',
								verticalAlign : 'middle',
								borderWidth : 0
							};

							var series = [
									{
										name : 'pc端',
										data : [ 7.0, 6.9, 9.5, 14.5, 18.2,
												21.5, 25.2, 26.5, 23.3, 18.3,
												13.9, 9.6 ]
									},
									{
										name : 'android端',
										data : [ -0.2, 0.8, 5.7, 11.3, 17.0,
												22.0, 24.8, 24.1, 20.1, 14.1,
												8.6, 2.5 ]
									}

							];

							var json = {};

							json.title = title;
							json.subtitle = subtitle;
							json.xAxis = xAxis;
							json.yAxis = yAxis;
							json.tooltip = tooltip;
							json.legend = legend;
							json.series = series;

							$('#container').highcharts(json);
						});
	</script>
</body>

</html>