var pojo_userPage={
		url:{
			userFirst: "http://localhost:8080/The_vertical_and_horizontal/admin_findUserByFirstPage",
			userLast: "http://localhost:8080/The_vertical_and_horizontal/admin_findUserByLastPage",
			userCurrentPage: "http://localhost:8080/The_vertical_and_horizontal/admin_findUserByPage",
			findUserTotalPage:"http://localhost:8080/The_vertical_and_horizontal/admin_findUserTotalPage"
		},
		user_manager:{
			user_html: function(value) {
				// 没被禁止
				var html = "						<tr>"+
				"<td>"+value.user_id+"</td>"
						+ "							<td>"+
						value.user_account
						+ "</td>"
						+ "							<td>"
						+ value.last_logout_time
						+ "</td>"
						+ "							<td>"
						+ value.user_online
						+ "</td>"
						+ "							<td>"
						+ "<span class=\"switch-off\" style=\"border-color: rgb(223, 223, 223); box-shadow: rgb(223, 223, 223) 0px 0px 0px 0px inset; background-color: rgb(255, 255, 255);\"><span class=\"slider\"></span></span>"
						+ "							</td>"
						
						+ "						</tr>";
				return html;
		
	},
	user_html2:function(value){
		// 被禁止
		var html2 = "						<tr>"+
			"<td>"+value.user_id+"</td>"
				+ "							<td>"
				+ value.user_account
				+ "</td>"
				+ "							<td>"
				+ value.last_logout_time
				+ "</td>"
				+ "							<td>"
				+ value.user_online
				+ "</td>"
				+ "							<td>"
				+ "<span class=\"switch-on\" style=\"border-color: rgb(100, 189, 99); box-shadow: rgb(100, 189, 99) 0px 0px 0px 16px inset; background-color: rgb(100, 189, 99);\"><span class=\"slider\"></span></span>"
				+ "							</td>"
				+ "						</tr>";
		return html2;
	},
	title:function(){
//		用户表头
		var title = "						<tr>"+
		"<th>ID</th>"
				+ "							<th>用户名</th>"
				+ "							<th>上次退登时间</th>"
				+ "							<th>是否在线</th>"
				+ "							<th>是否禁止登录</th>"
				+ "						</tr>";
		return title;
	},
			//分页：首页
			First:function(params){ 
					$.post(pojo_userPage.url.userFirst,{pageSize:params.pageSize},function(result){
						
						params.table.empty();
						params.table.append(pojo_userPage.user_manager.title);
						if (params.totalPageSpan!=null) {
							params.totalPageSpan.text(result.totalPage);
						}
						if (params.totalCountSpan!=null) {
							params.totalCountSpan.text(result.totalCount);
						}
						
						var html="<select name=\"pageSize\">";
						//添加可选页数
						if (params.optionPage!=null) {
							//清除所有可选页数
							params.optionPage.empty();
							for (var i = 0; i < result.totalPage; i++) {
								var index=i+1;
								html+="<option value="+index+">"+index+"</option>";
							 	if (i==result.totalPage-1) {
									html+="</select>";
								}
							}
						params.optionPage.append(html);
						}
						
						$
						.each(
								result.list,
								function(index, value) {

									
									if (!value.user_forbidden) {
										params.table
												.append(pojo_userPage.user_manager.user_html(value));
									} else {
										params.table
												.append(pojo_userPage.user_manager.user_html2(value));
									}

								})
							params.upButton.hide();
							params.nextButton.hide();
							if (result.totalPage>1) {
							params.nextButton.show();//按钮
						} 
					})
				 
			},
			//分页：尾页
			Last:function(params){
					$.post(pojo_userPage.url.userLast,{pageSize:params.pageSize},function(result){

						params.table.empty();
						params.table.append(pojo_userPage.user_manager.title);
							
	 
						$
						.each(
								result.list,
								function(index, value) {

									
									if (!value.user_forbidden) {
										params.table
												.append(pojo_userPage.user_manager.user_html(value));
									} else {
										params.table
												.append(pojo_userPage.user_manager.user_html2(value));
									}

								})
						params.upButton.hide();
						params.nextButton.hide();
						if (result.totalPage>1) {
						params.upButton.show();//按钮
					} 
					})
				 
			},
			//根据当前页数搜索
			CurrentPage:function(params){
					$.post(pojo_userPage.url.userCurrentPage,{
						currentPage:params.currentPage,
						pageSize:params.pageSize
					},function(result){

						params.table.empty();
						params.table.append(pojo_userPage.user_manager.title);
	 
						$
						.each(
								result.list,
								function(index, value) {

									
									if (!value.user_forbidden) {
										params.table
												.append(pojo_userPage.user_manager.user_html(value));
									} else {
										params.table
												.append(pojo_userPage.user_manager.user_html2(value));
									}

								})
								//先隐藏
								params.upButton.hide();
								params.nextButton.hide(); 
								//按钮
								if (params.currentPage!=1) {
									params.upButton.show();
								}
								if(params.currentPage!=result.totalPage){
									params.nextButton.show();
								}
					})
				 
			}

		}
		
}