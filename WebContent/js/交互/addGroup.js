var pojo = {
	url : {

		findAllNotJoin : "http://localhost:8080/The_vertical_and_horizontal/chatGroup_findAllNotJoin",
		findAllNotJoinByKey : "http://localhost:8080/The_vertical_and_horizontal/chatGroup_findAllNotJoinByKey",
		findDetail : "http://localhost:8080/The_vertical_and_horizontal/chatGroup_findDetailByGroupId",
		applyToJoin : "http://localhost:8080/The_vertical_and_horizontal/commonMessage_applyToJoin"
	},
	// 加入群页面
		addGroup:{
		// 查询所有未加入群
		findAllNotJoin : function(params) {
			$.post(pojo.url.findAllNotJoin, function(result) {
				params.ul.empty();
				$.each(result, function(index, value) {
					var html = "	<li><img src= " + result[index].group_image
							+ "/>" + "							<div class=\"add_group_wraper\">"
							+ "								<h3>" + result[index].group_name
							+ "</h3>" + "								<span class=\"count\">"
							+ +result[index].group_sum + "人" + "							</span>"
							+ "							</div>"
							+ "							<input type=\"hidden\" value="
							+ result[index].group_id + " id=" + index
							+ "></input>" + "						</li>";
					params.ul.append(html);
				})

			})
		},
		// 在未加入群搜索
		findAllNotJoinByKey : function(params) {
			$.post(pojo.url.findAllNotJoinByKey, {
				"key" : params.key
			}, function(result) {
				params.ul.empty();
				$.each(result, function(index, value) {
					var html = "	<li><img src= " + result[index].group_image
							+ "/>" + "							<div class=\"add_group_wraper\">"
							+ "								<h3>" + result[index].group_name
							+ "</h3>" + "								<span class=\"count\">"
							+ +result[index].group_sum + "人" + "							</span>"
							+ "							</div>"
							+ "							<input type=\"hidden\" value="
							+ result[index].group_id
							+ " class=\"hidden\"></input>" + "						</li>";
					if (result == null) {
						params.ul2.hide();
					}
					params.ul.append(html);
				})

			})
		},
		// 加载出现群详情
		findDetail : function(params) {
			$.post(pojo.url.findDetail, {
				"group_id" : params.group_id
			}, function(result) {
				params.table.empty();
				var html = "						<tr><th>群名称:</th><td>" + result.group_name
						+ "</td></tr>" + "						<tr><th>群简介：</th><td>"
						+ result.group_notice + "</td></tr>"
						+ "						<tr><th>群人数:</th><td>" + result.group_sum
						+ "人</td></tr>"
						+ "							<input type=\"hidden\" value="
						+ params.group_id + " class=\"hidden\"></input>";

				params.table.append(html);
				if (params.table.children().length > 0) {
					params.ul.show();
				}
			})

		},
		// 申请加群
		applyToJoin : function(params) {
			var str={
					"sender_id":params.sender_id,
					"group_id":params.group_id,
					"type":"addGroup"
			}
			var json=JSON.stringify(str);
			params.websocket.send(json);//发送

		},
		//接收消息
		receive:function(params){
			params.websocket.onmessage=function(event){
				var result=	event.data;
				 var json=eval('('+result+')');
				 if (json.type!="addGroup"||json.sender_id!=params.user_id) {
					return false;
				}
				if (!json.success) {
					params.tip.text("申请失败！").fadeIn().fadeOut("slow");
				} else {
					params.tip.text("申请成功，等待审核中！").fadeIn().fadeOut("slow");
				}
				}
		}
	}
}