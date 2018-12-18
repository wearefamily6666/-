var pojo_groupPage = {
	url : {
		groupFirst: "http://localhost:8080/The_vertical_and_horizontal/admin_findGroupByFirstPage",
		groupLast: "http://localhost:8080/The_vertical_and_horizontal/admin_findGroupByLastPage",
		groupCurrentPage: "http://localhost:8080/The_vertical_and_horizontal/admin_findGroupByPage",
		findGroupTotalPage:"http://localhost:8080/The_vertical_and_horizontal/admin_findGroupTotalPage"
	},
	group_manager : {
		group_html:function(value){
			// 没被禁止
			var html = "								<tr>"+
				"<td>"+value.group_id+"</td>"
					+ "									<td>"
					+ value.group_name
					+ "</td>"
					+ "									<td>"
					+ value.online_sum
					+ "</td>"
					+ "									<td>"
					+ value.sum
					+ "</td>"
					+ "									<td>"
					+ "<span class=\"switch-off\" style=\"border-color: rgb(223, 223, 223); box-shadow: rgb(223, 223, 223) 0px 0px 0px 0px inset; background-color: rgb(255, 255, 255);\"><span class=\"slider\"></span></span>"
					+ "									</td>"
					+ "								</tr>";
				return html;
		},
		group_html2:function(value){
			// 被禁止
			var html2 = "								<tr>"+
			"<td>"+value.group_id+"</td>"
					+ "									<td>"
					+ value.group_name
					+ "</td>"
					+ "									<td>"
					+ value.online_sum
					+ "</td>"
					+ "									<td>"
					+ value.sum
					+ "</td>"
					+ "									<td>"
					+ "<span class=\"switch-on\" style=\"border-color: rgb(100, 189, 99); box-shadow: rgb(100, 189, 99) 0px 0px 0px 16px inset; background-color: rgb(100, 189, 99);\"><span class=\"slider\"></span></span>"
					+ "									</td>"
					+ "								</tr>";
			return html2;
		},
//		群组表头
		title2:function(){
			var title = "								<tr>"+
			"<th>ID</th>"
				+ "									<th>群名</th>"
				+ "									<th>在线人数</th>"
				+ "									<th>群总人数</th>"
				+ "									<th>是否禁群</th>"
				+ "								</tr>";
			return title;
		},
		//分页：首页
		First:function(params){ 
				$.post(pojo_groupPage.url.groupFirst,{pageSize:params.pageSize},function(result){
			
					params.table.empty();
					params.table.append(pojo_groupPage.group_manager.title2);
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

								
								if (!value.group_forbidden) {
									params.table
											.append(pojo_groupPage.group_manager.group_html(value));
								} else {
									params.table
											.append(pojo_groupPage.group_manager.group_html2(value));
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
				$.post(pojo_groupPage.url.groupLast,{pageSize:params.pageSize},function(result){

					params.table.empty();
					params.table.append(pojo_groupPage.group_manager.title2);
						
 
					$
					.each(
							result.list,
							function(index, value) {

								
								if (!value.group_forbidden) {
									params.table
											.append(pojo_groupPage.group_manager.group_html(value));
								} else {
									params.table
											.append(pojo_groupPage.group_manager.group_html2(value));
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
				$.post(pojo_groupPage.url.groupCurrentPage,{
					currentPage:params.currentPage,
					pageSize:params.pageSize
				},function(result){

					params.table.empty();
					params.table.append(pojo_groupPage.group_manager.title2);
 
					$
					.each(
							result.list,
							function(index, value) {

								
								if (!value.group_forbidden) {
									params.table
											.append(pojo_groupPage.group_manager.group_html(value));
								} else {
									params.table
											.append(pojo_groupPage.group_manager.group_html2(value));
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